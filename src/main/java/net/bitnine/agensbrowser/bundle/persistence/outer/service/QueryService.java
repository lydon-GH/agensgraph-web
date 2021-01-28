//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

package net.bitnine.agensbrowser.bundle.persistence.outer.service;

import net.bitnine.agensbrowser.bundle.message.ClientDto;
import net.bitnine.agensbrowser.bundle.message.LabelDto;
import net.bitnine.agensbrowser.bundle.message.RequestDto;
import net.bitnine.agensbrowser.bundle.message.ResultDto;
import net.bitnine.agensbrowser.bundle.message.RequestDto.RequestType;
import net.bitnine.agensbrowser.bundle.message.ResponseDto.StateType;
import net.bitnine.agensbrowser.bundle.persistence.inner.dao.AgensLogDao;
import net.bitnine.agensbrowser.bundle.persistence.inner.model.AgensLog;
import net.bitnine.agensbrowser.bundle.persistence.outer.dao.QueryDao;
import net.bitnine.agensbrowser.bundle.persistence.outer.model.type.LabelType;
import net.bitnine.agensbrowser.bundle.storage.ClientStorage;
import net.bitnine.agensbrowser.bundle.storage.MetaStorage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class QueryService {
    private QueryDao queryDao;
    private AgensLogDao logDao;
    private ClientStorage clients;
    private MetaStorage metaStorage;
    private MetaService metaService;

    @Autowired
    public QueryService(QueryDao queryDao, AgensLogDao logDao, ClientStorage clients, MetaStorage metaStorage, MetaService metaService) {
        this.queryDao = queryDao;
        this.logDao = logDao;
        this.clients = clients;
        this.metaStorage = metaStorage;
        this.metaService = metaService;
    }

    public String getNow() {
        return this.queryDao.getNow();
    }

    public ResultDto doQuerySync(RequestDto req) {
        ClientDto client = this.clients.getClient(req.getSsid());
        AgensLog log = new AgensLog(client.getUserName(), client.getUserIp(), req.getSql(), StateType.PENDING.toString());
        if (!req.getOptions().equals("loggingOff")) {
            log = this.logDao.save(log);
        }

        ResultDto res = null;
        String sqlType = parseSqlType(req.getSql());
        if (sqlType.equals("SELECT")) {
            res = this.queryDao.doQuery(req);
            res.getGraph().updateMeta(this.metaStorage);
            int nodesSize = res.getGraph().getNodes().size();
            int edgesSize = res.getGraph().getEdges().size();
            if (nodesSize == 0) {
                res.setMessage(res.getMessage() + ", no graph");
            } else {
                res.setMessage(res.getMessage() + String.format(", graph(nodes=%d, edges=%d)", nodesSize, edgesSize));
            }
        } else if (sqlType.equals("FORBIDDEN")) {
            res = new ResultDto(req);
            res.setState(StateType.FAIL);
            res.setMessage("'SET graph_path' is forbidden. This product cannot change graph_path");
        } else {
            res = this.queryDao.doUpdate(req, sqlType);
        }

        if (!req.getOptions().equals("loggingOff") && log != null && log.getId() != null) {
            log.setState(res.getState().toString());
            log.setMessage(res.getMessage());
            this.logDao.saveAndFlush(log);
        }

        return res;
    }

    public static final String parseSqlType(String sql) {
        String query = (new String(sql)).toLowerCase();
        if (query.startsWith("match ")) {
            if (query.indexOf(" return ") > 0) {
                return "SELECT";
            }

            if (query.indexOf(" delete ") > 0 || query.indexOf(" delete(") > 0) {
                return "DELETE";
            }

            if (query.indexOf(" set ") > 0) {
                return "UPDATE";
            }

            if (query.indexOf(" merge ") > 0) {
                return "MERGE";
            }
        } else {
            if (query.startsWith("select ")) {
                return "SELECT";
            }

            if (query.startsWith("create ")) {
                return "CREATE";
            }

            if (query.startsWith("drop ")) {
                return "DROP";
            }

            if (query.startsWith("delete ")) {
                return "DELETE";
            }

            if (query.startsWith("update ")) {
                return "UPDATE";
            }

            if (query.startsWith("alter ")) {
                return "ALTER";
            }

            if (query.startsWith("comment ")) {
                return "COMMENT";
            }

            if (query.startsWith("set graph_path") || query.startsWith("set search_path")) {
                return "FORBIDDEN";
            }
        }

        return sql.substring(0, 12).toUpperCase() + "..";
    }

    public LabelDto doCommandSync(RequestDto req) {
        LabelDto prevDto = this.metaStorage.getLabelDtoCopy(req);
        ClientDto client = this.clients.getClient(req.getSsid());
        AgensLog log = new AgensLog(client.getUserName(), client.getUserIp(), req.getSql(), StateType.PENDING.toString());
        log = this.logDao.save(log);
        String sqlType = parseSqlType(req.getSql());
        ResultDto resQuery = this.queryDao.doUpdate(req, sqlType);
        String sql = req.getSql();
        String message = resQuery.getMessage();
        if (resQuery.getState().equals(StateType.FAIL)) {
            LabelDto result = new LabelDto(req);
            result.setState(resQuery.getState());
            result.setMessage(message + "\n updateLabel is abort");
            if (log != null && log.getId() != null) {
                log.setState(resQuery.getState().toString());
                log.setMessage(resQuery.getMessage());
                this.logDao.saveAndFlush(log);
            }

            return result;
        } else {
            if (RequestDto.toRequestType(req.getType()).equals(RequestType.CREATE) && req.getOptions().length() > 0) {
                String commentSql = "comment on " + req.getCommand() + " " + req.getTarget() + " is '" + req.getOptions() + "';";
                req.setSql(commentSql);
                sql = sql + " " + commentSql;
                ResultDto resComment = this.queryDao.doUpdate(req, "COMMENT label");
                message = message + "\n " + resComment.getMessage();
            }

            LabelType currLabel = this.metaService.updateLabelCopy(req);
            LabelDto result = new LabelDto(req);
            result.setState(resQuery.getState());
            result.setMessage(message);
            if (currLabel == null) {
                if (prevDto.getLabel() != null) {
                    result.setLabel(prevDto.getLabel());
                    this.metaStorage.removeLabelTypeCopy(prevDto.getLabel());
                    result.setMessage(message + "\n Also meta storage is updated(REMOVE)");
                }
            } else {
                result.setLabel(currLabel);
                result.setMessage(message + "\n Also meta storage is updated(ADD/REPLACE)");
            }

            if (log != null && log.getId() != null) {
                log.setQuery(sql);
                log.setState(result.getState().toString());
                log.setMessage(result.getMessage());
                this.logDao.saveAndFlush(log);
            }

            return result;
        }
    }
}
