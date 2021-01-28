//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

package net.bitnine.agensbrowser.bundle.persistence.outer.dao;

import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import net.bitnine.agensbrowser.bundle.message.RequestDto;
import net.bitnine.agensbrowser.bundle.message.ResultDto;
import net.bitnine.agensbrowser.bundle.message.ResponseDto.StateType;
import net.bitnine.agensbrowser.bundle.persistence.outer.model.Edge;
import net.bitnine.agensbrowser.bundle.persistence.outer.model.Graph;
import net.bitnine.agensbrowser.bundle.persistence.outer.model.Node;
import net.bitnine.agensbrowser.bundle.persistence.outer.model.type.ColumnType;
import net.bitnine.agensbrowser.bundle.persistence.outer.model.type.ColumnType.ValueType;
import net.bitnine.agensbrowser.bundle.util.Jsonb;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.dao.DataAccessException;
import org.springframework.dao.QueryTimeoutException;
import org.springframework.jdbc.BadSqlGrammarException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.ResultSetExtractor;
import org.springframework.stereotype.Repository;

@Repository
public class QueryDao {
    @Autowired
    @Qualifier("outerJdbcTemplate")
    private JdbcTemplate jdbcTemplate;
    @Value("${agens.outer.datasource.graph_path}")
    private String graphPath;
    @Value("${agens.outer.datasource.max_rows}")
    private int maxRows;
    @Value("${agens.config.query_timeout}")
    private int queryTimeout;

    public QueryDao() {
    }

    public String getNow() {
        String query = "select current_timestamp";
        Timestamp now = (Timestamp)this.jdbcTemplate.queryForObject(query, Timestamp.class);
        String dateString = (new SimpleDateFormat("yyyy-MM-dd HH:mm:ss")).format(now);

        try {
            Thread.sleep((long)this.queryTimeout);
        } catch (InterruptedException var5) {
            System.out.println(var5.getMessage());
        }

        return dateString;
    }

    public String getGraphPath() {
        return this.graphPath;
    }

    public ResultDto doUpdate(RequestDto req, String sqlType) {
        System.out.println("doUpdate: " + req.getSql());
        ResultDto result = new ResultDto(req);
        String message = "";

        try {
            this.jdbcTemplate.setQueryTimeout(this.queryTimeout);
            int rows = this.jdbcTemplate.update(req.getSql());
            message = "'" + sqlType + "' affected";
            result.setMessage(message);
            result.setState(StateType.SUCCESS);
            result.setRequest(req);
        } catch (BadSqlGrammarException var6) {
            message = "BadSqlGrammarException->" + var6.getMessage() + ", SQL Code->" + ((SQLException)var6.getCause()).getSQLState();
            result.setMessage(message);
            result.setState(StateType.FAIL);
        } catch (QueryTimeoutException var7) {
            message = "QueryTimeoutException->" + var7.getMessage() + ", SQL Code->" + ((SQLException)var7.getCause()).getSQLState() + "\n Timeout " + this.queryTimeout / 1000 + " seconds is over. If you want more timeout, modify agens-browser.config.yml";
            result.setMessage(message);
            result.setState(StateType.FAIL);
        } catch (DataAccessException var8) {
            message = "DataAccessException->" + var8.getMessage() + ", SQL Code->" + ((SQLException)var8.getCause()).getSQLState();
            result.setMessage(message);
            result.setState(StateType.FAIL);
        }

        System.out.println("==> " + message);
        return result;
    }

    public ResultDto doQuery(RequestDto req) {
        System.out.println("doQuery: " + req.getSql());
        ResultDto result = new ResultDto(req);
        String message = "";

        try {
            this.jdbcTemplate.setMaxRows(this.maxRows);
            this.jdbcTemplate.setQueryTimeout(this.queryTimeout);
            result = (ResultDto)this.jdbcTemplate.query(req.getSql(), new QueryDao.ResultExtractor());
            message = "return " + result.getRecord().getRows().size() + " rows (cols=" + result.getRecord().getMeta().size() + ")";
            result.setMessage(message);
            result.setState(StateType.SUCCESS);
            result.setRequest(req);
        } catch (BadSqlGrammarException var5) {
            message = "BadSqlGrammarException->" + var5.getMessage() + ", SQL Code->" + ((SQLException)var5.getCause()).getSQLState();
            result.setMessage(message);
            result.setState(StateType.FAIL);
        } catch (QueryTimeoutException var6) {
            message = "QueryTimeoutException->" + var6.getMessage() + ", SQL Code->" + ((SQLException)var6.getCause()).getSQLState() + "\n Timeout " + this.queryTimeout / 1000 + " seconds is over. If you want more timeout, modify agens-browser.config.yml";
            result.setMessage(message);
            result.setState(StateType.FAIL);
        } catch (DataAccessException var7) {
            message = "DataAccessException->" + var7.getMessage() + ", SQL Code->" + ((SQLException)var7.getCause()).getSQLState();
            result.setMessage(message);
            result.setState(StateType.FAIL);
        }

        System.out.println("==> " + message);
        return result;
    }

    private static final class ResultExtractor implements ResultSetExtractor<ResultDto> {
        private ResultExtractor() {
        }

        @Override
        public ResultDto extractData(ResultSet rs) throws SQLException {
            ResultDto result = new ResultDto();
            ResultSetMetaData rsmd = rs.getMetaData();
            List<ColumnType> meta = result.getRecord().getMeta();
            int columnCount = rsmd.getColumnCount();

            for(int i = 0; i < columnCount; ++i) {
                String colLabel = rsmd.getColumnLabel(i + 1).toLowerCase();
                String colType = rsmd.getColumnTypeName(i + 1).toLowerCase();
                ColumnType column = new ColumnType(colLabel, convertTypeName(colType), i);
                meta.add(column);
                System.out.println(column.toString() + " <== " + colType);
            }

            List rows = result.getRecord().getRows();

            while(rs.next()) {
                List<Object> row = new ArrayList();

                for(int i = 0; i < columnCount; ++i) {
                    Object value = convertValue(result.getGraph(), ((ColumnType)meta.get(i)).getType(), rs.getObject(i + 1));
                    row.add(value);
                }

                rows.add(row);
            }

            return result;
        }

        private static final ValueType convertTypeName(String colType) {
            String[] numberTypes = new String[]{"numeric", "decimal", "int", "int2", "int4", "int8", "long", "serial2", "serial4", "serial8", "float", "float4", "float8", "double"};
            String[] stringTypes = new String[]{"string", "text", "varchar", "char"};
            String[] idTypes = new String[]{"graphid"};
            String[] nodeTypes = new String[]{"vertex", "node"};
            String[] edgeTypes = new String[]{"edge"};
            String[] graphTypes = new String[]{"graphpath", "graph"};
            String[] objectTypes = new String[]{"jsonb", "json", "object", JSONObject.class.getTypeName()};
            String[] arrayTypes = new String[]{"array", JSONArray.class.getTypeName()};
            String[] booleanTypes = new String[]{"boolean", "bool"};
            if (Arrays.asList(nodeTypes).contains(colType)) {
                return ValueType.NODE;
            } else if (Arrays.asList(edgeTypes).contains(colType)) {
                return ValueType.EDGE;
            } else if (Arrays.asList(graphTypes).contains(colType)) {
                return ValueType.GRAPH;
            } else if (Arrays.asList(numberTypes).contains(colType)) {
                return ValueType.NUMBER;
            } else if (Arrays.asList(stringTypes).contains(colType)) {
                return ValueType.STRING;
            } else if (Arrays.asList(idTypes).contains(colType)) {
                return ValueType.ID;
            } else if (Arrays.asList(objectTypes).contains(colType)) {
                return ValueType.OBJECT;
            } else if (Arrays.asList(arrayTypes).contains(colType)) {
                return ValueType.ARRAY;
            } else {
                return Arrays.asList(booleanTypes).contains(colType) ? ValueType.BOOLEAN : ValueType.NULL;
            }
        }

        private static final Object convertValue(Graph result, ValueType colType, Object value) {
            if (colType.equals(ValueType.NODE)) {
                Node node = new Node();

                try {
                    node.setValue(value.toString());
                    result.getNodes().add(node);
                } catch (SQLException var5) {
                    System.out.println("convertValue<" + colType.toString() + "> fail: " + var5.getCause());
                    System.out.println(value.toString() + "\n");
                }

                return node.toJson();
            } else if (colType.equals(ValueType.EDGE)) {
                Edge edge = new Edge();

                try {
                    edge.setValue(value.toString());
                    result.getEdges().add(edge);
                } catch (SQLException var6) {
                    System.out.println("convertValue<" + colType.toString() + "> fail: " + var6.getCause());
                    System.out.println(value.toString() + "\n");
                }

                return edge.toJson();
            } else if (colType.equals(ValueType.GRAPH)) {
                Graph graph = new Graph();

                try {
                    graph.setValue(value.toString());
                    result.getNodes().addAll(graph.getNodes());
                    result.getEdges().addAll(graph.getEdges());
                } catch (SQLException var7) {
                    System.out.println("convertValue<" + colType.toString() + "> fail: " + var7.getCause());
                    System.out.println(value.toString() + "\n");
                }

                return graph.toJson();
            } else if (colType.equals(ValueType.ID)) {
                return value.toString();
            } else if (!colType.equals(ValueType.BOOLEAN) && !colType.equals(ValueType.NUMBER) && !colType.equals(ValueType.STRING)) {
                if (!colType.equals(ValueType.ARRAY) && !colType.equals(ValueType.OBJECT)) {
                    System.out.println("convertValue<" + colType.toString() + "> pass --> " + value.toString());
                    return value;
                } else {
                    Jsonb props = new Jsonb();

                    try {
                        if (value == null) {
                            System.out.println("convertValue<" + colType.toString() + "> pass --> value is null");
                            return null;
                        }

                        props.setValue(value.toString());
                    } catch (SQLException var8) {
                        System.out.println("convertValue<" + colType.toString() + "> fail: " + var8.getCause());
                        System.out.println(value.toString() + "\n");
                    }

                    return props.getJsonValue();
                }
            } else {
                return value;
            }
        }
    }
}
