//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

package net.bitnine.agensbrowser.bundle.controller;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.util.concurrent.Callable;
import java.util.concurrent.atomic.AtomicLong;
import javax.servlet.http.HttpServletRequest;
import net.bitnine.agensbrowser.bundle.message.ClientDto;
import net.bitnine.agensbrowser.bundle.message.LabelDto;
import net.bitnine.agensbrowser.bundle.message.MetaDto;
import net.bitnine.agensbrowser.bundle.message.RequestDto;
import net.bitnine.agensbrowser.bundle.message.ResponseDto;
import net.bitnine.agensbrowser.bundle.message.ResultDto;
import net.bitnine.agensbrowser.bundle.message.RequestDto.RequestType;
import net.bitnine.agensbrowser.bundle.message.ResponseDto.StateType;
import net.bitnine.agensbrowser.bundle.persistence.inner.service.AgensService;
import net.bitnine.agensbrowser.bundle.persistence.outer.service.QueryService;
import net.bitnine.agensbrowser.bundle.storage.ClientStorage;
import net.bitnine.agensbrowser.bundle.storage.MetaStorage;
import org.json.simple.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.mobile.device.Device;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.context.request.async.WebAsyncTask;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

@RestController
@RequestMapping({"${agens.config.base_path}/core"})
public class CoreController {
    protected final Logger logger = LoggerFactory.getLogger(this.getClass());
    private final AtomicLong txSeq = new AtomicLong(100001L);
    private static final String txBase = "core";
    @Value("${agens.config.base_path}")
    private String basePath;
    @Value("${agens.product.name}")
    private String productName;
    @Value("${agens.product.version}")
    private String productVersion;
    @Value("${agens.config.query_timeout}")
    private Long queryTimeout;
    @Value("${agens.jwt.header}")
    private String ssidHeader;
    @Autowired
    @Qualifier("agensExecutor")
    ThreadPoolTaskExecutor executor;
    private MetaStorage metaStorage;
    private ClientStorage clientStorage;
    private AgensService agensService;
    private QueryService queryService;

    private final String getTxid() {
        return "core#" + String.valueOf(this.txSeq.getAndIncrement());
    }

    @Autowired
    public CoreController(MetaStorage metaStorage, ClientStorage clientStorage, AgensService agensService, QueryService queryService) {
        this.metaStorage = metaStorage;
        this.clientStorage = clientStorage;
        this.agensService = agensService;
        this.queryService = queryService;
    }

    private final HttpHeaders productHeaders() {
        HttpHeaders headers = new HttpHeaders();
        headers.add("agens.product.name", this.productName);
        headers.add("agens.product.version", this.productVersion);
        return headers;
    }

    private final ResponseEntity<JSONObject> unauthorizedMessage() {
        ResponseDto response = new ResponseDto();
        response.setState(StateType.FAIL);
        response.setMessage("You do not have right SESSION_ID. Do connect again");
        response.set_link(ServletUriComponentsBuilder.fromCurrentRequest().replacePath("/" + this.basePath + "/core/connect").replaceQuery("").toUriString());
        return new ResponseEntity(response.toJson(), this.productHeaders(), HttpStatus.UNAUTHORIZED);
    }

    @RequestMapping(
            value = {"meta"},
            method = {RequestMethod.GET},
            produces = {"application/json; charset=utf-8"}
    )
    public ResponseEntity<?> getMeta(@RequestParam(value = "from",required = false,defaultValue = "copy") String from, HttpServletRequest request, Device device) throws InterruptedException {
        String ssid = request.getHeader(this.ssidHeader) == null ? "1234567890" : request.getHeader(this.ssidHeader);
        this.logger.info(String.format("/%s/%s/%s", this.basePath, "core", "meta"));
        ClientDto client = this.clientStorage.getClient(ssid);
        if (client == null) {
            return this.unauthorizedMessage();
        } else {
            MetaDto dto = this.metaStorage.getMetaDtoCopy();
            if (from.equals("source")) {
                dto = this.metaStorage.getMetaDto();
            }

            return new ResponseEntity(dto.toJson(), this.productHeaders(), HttpStatus.OK);
        }
    }

    @RequestMapping(
            value = {"query"},
            method = {RequestMethod.GET},
            produces = {"application/json; charset=utf-8"}
    )
    public WebAsyncTask<ResponseEntity<?>> doQuery(@RequestParam(value = "sql",required = true) String sql, @RequestParam(value = "options",required = false,defaultValue = "") String options, HttpServletRequest request, Device device) throws InterruptedException {
        try {
            sql = URLDecoder.decode(sql, StandardCharsets.UTF_8.toString());
        } catch (UnsupportedEncodingException var11) {
            System.out.println("doQuery: UnsupportedEncodingException => " + var11.getCause());
        }

        String ssid = request.getHeader(this.ssidHeader) == null ? "1234567890" : request.getHeader(this.ssidHeader);
        this.logger.info(String.format("%s/%s/%s?sql=%s&options=%s", this.basePath, "core", "query", sql, options));
        ClientDto client = this.clientStorage.getClient(ssid);
        if (client == null) {
            Callable<ResponseEntity<?>> callableDto = new Callable<ResponseEntity<?>>() {
                public ResponseEntity<?> call() throws Exception {
                    return CoreController.this.unauthorizedMessage();
                }
            };
            return new WebAsyncTask(callableDto);
        } else {
            final RequestDto req = new RequestDto(ssid, this.getTxid(), "QUERY");
            sql = sql.trim();
            if (!sql.endsWith(";")) {
                sql = sql + ";";
            }

            sql = sql.replaceAll("\\s{2,}", " ");
            req.setSql(sql);
            req.setOptions(options);
            Callable<ResponseEntity<?>> callableDto = new Callable<ResponseEntity<?>>() {
                public ResponseEntity<?> call() throws Exception {
                    ResultDto dto = CoreController.this.queryService.doQuerySync(req);
                    return new ResponseEntity(dto.toJson(), CoreController.this.productHeaders(), HttpStatus.OK);
                }
            };
            Callable<ResponseEntity<?>> timeoutDto = new Callable<ResponseEntity<?>>() {
                public ResponseEntity<?> call() throws Exception {
                    ResultDto res = new ResultDto(req);
                    res.setState(StateType.FAIL);
                    res.setMessage("ERROR: Timeout " + CoreController.this.queryTimeout / 1000L + " seconds is over. If you want more timeout, modify agens-browser.config.yml");
                    return new ResponseEntity(res.toJson(), CoreController.this.productHeaders(), HttpStatus.REQUEST_TIMEOUT);
                }
            };
            WebAsyncTask<ResponseEntity<?>> response = new WebAsyncTask(this.queryTimeout, this.executor, callableDto);
            response.onTimeout(timeoutDto);
            return response;
        }
    }

    private String makeSql(String type, String command, String target) {
        RequestType reqType = RequestDto.toRequestType(type);
        if (reqType.equals(RequestType.NONE)) {
            return null;
        } else {
            return (command.equalsIgnoreCase("vlabel") || command.equalsIgnoreCase("elabel")) && !target.equals("") ? new String(type + " " + command + " " + target + ";") : null;
        }
    }

    @RequestMapping(
            value = {"command"},
            method = {RequestMethod.GET},
            produces = {"application/json; charset=utf-8"}
    )
    public WebAsyncTask<ResponseEntity<?>> doCommand(@RequestParam(value = "type",required = true) String type, @RequestParam(value = "command",required = true) String command, @RequestParam(value = "target",required = true) String target, @RequestParam(value = "options",required = false,defaultValue = "") String options, HttpServletRequest request, Device device) throws InterruptedException {
        String ssid = request.getHeader(this.ssidHeader) == null ? "1234567890" : request.getHeader(this.ssidHeader);
        this.logger.info(String.format("/%s/%s/%s?command=%s&options=%s", this.basePath, "core", "command", command, options));
        ClientDto client = this.clientStorage.getClient(ssid);
        if (client == null) {
            Callable<ResponseEntity<?>> callableDto = new Callable<ResponseEntity<?>>() {
                public ResponseEntity<?> call() throws Exception {
                    return CoreController.this.unauthorizedMessage();
                }
            };
            return new WebAsyncTask(callableDto);
        } else {
            final RequestDto req = new RequestDto(ssid, this.getTxid(), type);
            command = command.trim().toLowerCase();
            req.setCommand(command);
            target = target.trim().toLowerCase();
            req.setTarget(target);
            options = options.trim();
            req.setOptions(options);
            type = type.trim().toUpperCase();
            String sql = this.makeSql(type, command, target);
            Callable callableDto;
            if (sql == null) {
                callableDto = new Callable<ResponseEntity<?>>() {
                    public ResponseEntity<?> call() throws Exception {
                        LabelDto res = new LabelDto(req);
                        res.setState(StateType.FAIL);
                        res.setMessage("ERROR: wrong command => '" + req.getType() + " " + req.getCommand() + " " + req.getTarget() + "'");
                        return new ResponseEntity(res.toJson(), CoreController.this.productHeaders(), HttpStatus.PARTIAL_CONTENT);
                    }
                };
                return new WebAsyncTask(callableDto);
            } else {
                req.setSql(sql);
                callableDto = new Callable<ResponseEntity<?>>() {
                    public ResponseEntity<?> call() throws Exception {
                        LabelDto dto = CoreController.this.queryService.doCommandSync(req);
                        return new ResponseEntity(dto.toJson(), CoreController.this.productHeaders(), HttpStatus.OK);
                    }
                };
                Callable<ResponseEntity<?>> timeoutDto = new Callable<ResponseEntity<?>>() {
                    public ResponseEntity<?> call() throws Exception {
                        LabelDto res = new LabelDto(req);
                        res.setState(StateType.FAIL);
                        res.setMessage("ERROR: Timeout " + CoreController.this.queryTimeout / 1000L + " seconds is over. If you want more timeout, modify agens-browser.config.yml");
                        return new ResponseEntity(res.toJson(), CoreController.this.productHeaders(), HttpStatus.PARTIAL_CONTENT);
                    }
                };
                WebAsyncTask<ResponseEntity<?>> response = new WebAsyncTask(this.queryTimeout, this.executor, callableDto);
                response.onTimeout(timeoutDto);
                return response;
            }
        }
    }
}
