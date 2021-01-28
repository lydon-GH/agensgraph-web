//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

package net.bitnine.agensbrowser.bundle.persistence.outer.dao;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import net.bitnine.agensbrowser.bundle.persistence.outer.model.type.GraphType;
import net.bitnine.agensbrowser.bundle.persistence.outer.model.type.LabelType;
import net.bitnine.agensbrowser.bundle.persistence.outer.model.type.PropertyType;
import net.bitnine.agensbrowser.bundle.persistence.outer.model.type.LabelType.ElemType;
import org.postgresql.util.PSQLException;
import org.postgresql.util.PSQLState;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

@Repository
public class MetaDao {
    @Autowired
    @Qualifier("outerJdbcTemplate")
    private JdbcTemplate jdbcTemplate;
    @Value("${agens.outer.datasource.url}")
    private String graphUrl;
    @Value("${agens.outer.datasource.graph_path}")
    private String graphPath;

    public MetaDao() {
    }

    public String getGraphPath() {
        return this.graphPath;
    }

    public float checkAGVersion() {
        String query = "return pg_typeof(3.1)";
        String result = (String)this.jdbcTemplate.queryForObject(query, String.class);
        return result.equals("jsonb") ? 1.3F : 1.2F;
    }

    public GraphType getGraphType() {
        String query = "SELECT g.nspid as gr_oid, g.graphname as gr_name, pg_catalog.pg_get_userbyid(nspowner) as gr_owner, coalesce(null, pg_catalog.obj_description(g.nspid), '') as gr_desc FROM pg_catalog.ag_graph g LEFT JOIN pg_catalog.pg_namespace n on n.nspname = g.graphname WHERE g.graphname = '" + this.graphPath + "'";
        List results = null;

        try {
            results = this.jdbcTemplate.query(query, (rs, rowNum) -> {
                return new GraphType(rs.getString("gr_oid"), rs.getString("gr_name"), rs.getString("gr_owner"), rs.getString("gr_desc"));
            });
            if (results.size() > 0) {
                GraphType graph = (GraphType)results.get(0);
                graph.setJdbcUrl(this.graphUrl);
                graph.setIsDirty(false);
                return graph;
            }
        } catch (DataAccessException var4) {
            System.out.println("getGraph(): DataAccessException->" + var4.getMessage() + ", SQL Code->" + ((SQLException)var4.getCause()).getSQLState());
        }

        return null;
    }

    public List<LabelType> getLabelTypes() {
        String query = "SELECT l.labid as la_oid, l.labname as la_name, case when l.labkind='e' then 'edge' else 'node' end as la_type, pg_catalog.pg_get_userbyid(c.relowner) as la_owner, coalesce(null, pg_catalog.obj_description(l.oid, 'ag_label'), '') as la_desc FROM pg_catalog.ag_label l INNER JOIN pg_catalog.ag_graph g ON g.oid = l.graphid LEFT OUTER JOIN pg_catalog.pg_class c ON c.oid = l.relid WHERE g.graphname = '" + this.graphPath + "' and l.labname not in ('ag_vertex', 'ag_edge') ORDER BY l.labid;";
        Object results = new ArrayList();

        try {
            results = this.jdbcTemplate.query(query, (rs, rowNum) -> {
                return new LabelType(rs.getString("la_oid"), rs.getString("la_name"), rs.getString("la_type"), rs.getString("la_owner"), rs.getString("la_desc"));
            });
        } catch (DataAccessException var4) {
            System.out.println("getLabels(): DataAccessException->" + var4.getMessage() + ", SQL Code->" + ((SQLException)var4.getCause()).getSQLState());
        }

        return (List)results;
    }

    public LabelType getLabelType(ElemType type, String labelName) {
        String labKind = "v";
        if (ElemType.EDGE.equals(type)) {
            labKind = "e";
        }

        String query = "SELECT l.labid as la_oid, l.labname as la_name, case when l.labkind='e' then 'edge' else 'node' end as la_type, pg_catalog.pg_get_userbyid(c.relowner) as la_owner, coalesce(null, pg_catalog.obj_description(l.oid, 'ag_label'), '') as la_desc FROM pg_catalog.ag_label l INNER JOIN pg_catalog.ag_graph g ON g.oid = l.graphid LEFT OUTER JOIN pg_catalog.pg_class c ON c.oid = l.relid WHERE g.graphname = '" + this.graphPath + "' and l.labkind = '" + labKind + "' and l.labname = '" + labelName + "' LIMIT 1;";
        Object results = new ArrayList();

        try {
            results = this.jdbcTemplate.query(query, (rs, rowNum) -> {
                return new LabelType(rs.getString("la_oid"), rs.getString("la_name"), rs.getString("la_type"), rs.getString("la_owner"), rs.getString("la_desc"));
            });
        } catch (DataAccessException var7) {
            System.out.println("getLabel(" + labelName + "): DataAccessException->" + var7.getMessage() + ", SQL Code->" + ((SQLException)var7.getCause()).getSQLState());
        }

        LabelType result = null;
        if (((List)results).size() > 0) {
            result = (LabelType)((List)results).get(0);
        }

        return result;
    }

    public Long[] getLabelCount(String label) throws SQLException {
        Long[] counts = new Long[]{0L, 0L};
        if (label == null) {
            return counts;
        } else {
            String query = "SELECT count(properties) as tot_cnt, coalesce( sum(case when properties = '{}' then 0 else 1 end), -1) as json_cnt from " + this.graphPath + "." + label + ";";

            try {
                List<Long[]> result = this.jdbcTemplate.query(query, (rs, rowNum) -> {
                    Long[] cols = new Long[]{rs.getLong(1), rs.getLong(2)};
                    return cols;
                });
                if (result.size() > 0) {
                    counts = (Long[])result.get(0);
                }
            } catch (DataAccessException var6) {
                String sqlErrCode = ((SQLException)var6.getCause()).getSQLState();
                System.out.println("getLabelCount(): DataAccessException->" + var6.getMessage() + ", SQL Code->" + sqlErrCode);
                if (sqlErrCode == null || sqlErrCode.equals("08006")) {
                    throw new PSQLException("getLabelCount() failed.", PSQLState.CONNECTION_FAILURE, var6);
                }

                counts[0] = -1L;
                counts[1] = -1L;
            }

            return counts;
        }
    }

    public List<PropertyType> getLabelProperties(String label) throws SQLException {
        if (label == null) {
            return null;
        } else {
            String query = "SELECT json_data.key as json_key, jsonb_typeof(json_data.value) as json_type, count(*) as key_count FROM ( select properties from " + this.graphPath + "." + label + " where " + this.graphPath + "." + label + ".properties <> '{}' order by id desc limit 10000 ) tmp, jsonb_each(tmp.properties) AS json_data WHERE jsonb_typeof(json_data.value) <> 'null' group by 1, 2 order by 1, 2;";
            List results = null;

            try {
                results = this.jdbcTemplate.query(query, (rs, rowNum) -> {
                    return new PropertyType(rs.getString("json_key"), rs.getString("json_type"), rs.getLong("key_count"));
                });
            } catch (DataAccessException var6) {
                String sqlErrCode = ((SQLException)var6.getCause()).getSQLState();
                System.out.println("getLabelProperties(): DataAccessException->" + var6.getMessage() + ", SQL Code->" + sqlErrCode);
                if (sqlErrCode == null || sqlErrCode.equals("08006")) {
                    throw new PSQLException("getLabelProperties() failed.", PSQLState.CONNECTION_FAILURE, var6);
                }

                results = null;
            }

            return results;
        }
    }

    public List<Long[]> getRelationCount(String label) throws SQLException {
        List<Long[]> result = new ArrayList();
        if (label == null) {
            return (List)result;
        } else {
            String query = "match (a)-[r:" + label + "]->(b) with graphid_labid(start(r)) as s_oid, graphid_labid(id(r)) as e_oid, graphid_labid(\"end\"(r)) as t_oid return e_oid, s_oid, t_oid, count(*) as cnt;";

            try {
                result = this.jdbcTemplate.query(query, (rs, rowNum) -> {
                    Long[] cols = new Long[]{rs.getLong(1), rs.getLong(2), rs.getLong(3), rs.getLong(4)};
                    return cols;
                });
            } catch (DataAccessException var6) {
                String sqlErrCode = ((SQLException)var6.getCause()).getSQLState();
                System.out.println("getRelationCount(): DataAccessException->" + var6.getMessage() + ", SQL Code->" + sqlErrCode);
                if (sqlErrCode == null || sqlErrCode.equals("08006")) {
                    throw new PSQLException("getRelationCount() failed.", PSQLState.CONNECTION_FAILURE, var6);
                }
            }

            return (List)result;
        }
    }
}
