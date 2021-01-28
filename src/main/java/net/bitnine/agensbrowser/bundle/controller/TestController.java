//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

package net.bitnine.agensbrowser.bundle.controller;

import java.sql.SQLException;
import java.util.concurrent.Callable;
import java.util.concurrent.atomic.AtomicLong;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import net.bitnine.agensbrowser.bundle.message.RequestDto;
import net.bitnine.agensbrowser.bundle.message.ResponseDto;
import net.bitnine.agensbrowser.bundle.message.ResponseDto.StateType;
import net.bitnine.agensbrowser.bundle.persistence.inner.service.AgensService;
import net.bitnine.agensbrowser.bundle.persistence.outer.model.Edge;
import net.bitnine.agensbrowser.bundle.persistence.outer.model.Graph;
import net.bitnine.agensbrowser.bundle.persistence.outer.model.Node;
import net.bitnine.agensbrowser.bundle.persistence.outer.service.QueryService;
import net.bitnine.agensbrowser.bundle.storage.MetaStorage;
import net.bitnine.agensbrowser.bundle.util.JsonbUtil;
import org.json.simple.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.ResponseEntity.BodyBuilder;
import org.springframework.mobile.device.Device;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.context.request.async.WebAsyncTask;

@RestController
@RequestMapping({"${agens.config.base_path}/test"})
public class TestController {
    protected final Logger logger = LoggerFactory.getLogger(this.getClass());
    private final AtomicLong txSeq = new AtomicLong();
    private static final String txBase = "test";
    @Value("${agens.config.base_path}")
    private String basePath;
    @Value("${agens.product.name}")
    private String productName;
    @Value("${agens.product.version}")
    private String productVersion;
    @Value("${agens.config.query_timeout}")
    private Long queryTimeout;
    @Autowired
    @Qualifier("agensExecutor")
    ThreadPoolTaskExecutor executor;
    private MetaStorage metaStorage;
    private AgensService agensService;
    private QueryService queryService;

    private final String getTxid() {
        return "test#" + String.valueOf(this.txSeq.getAndIncrement());
    }

    @Autowired
    public TestController(MetaStorage metaStorage, AgensService agensService, QueryService queryService) {
        this.metaStorage = metaStorage;
        this.agensService = agensService;
        this.queryService = queryService;
    }

    public final HttpHeaders productHeaders() {
        HttpHeaders headers = new HttpHeaders();
        headers.add("agens.product.name", this.productName);
        headers.add("agens.product.version", this.productVersion);
        return headers;
    }

    @RequestMapping(
            value = {"timeout"},
            method = {RequestMethod.GET},
            produces = {"application/json; charset=utf-8"}
    )
    @ResponseBody
    public WebAsyncTask<ResponseEntity<?>> getNowWithTimeout() {
        RequestDto req = new RequestDto("1234567890", this.getTxid(), "QUERY");
        String sql = "Timeout Test : " + this.queryTimeout + " ms is over!";
        req.setSql(sql.trim());
        Callable<ResponseEntity<?>> callableDto = new Callable<ResponseEntity<?>>() {
            public ResponseEntity<?> call() throws Exception {
                ResponseDto dto = new ResponseDto();
                dto.setState(StateType.SUCCESS);
                dto.setMessage("RESULT: " + TestController.this.queryService.getNow());
                return new ResponseEntity(dto.toJson(), TestController.this.productHeaders(), HttpStatus.OK);
            }
        };
        Callable<ResponseEntity<?>> timeoutDto = new Callable<ResponseEntity<?>>() {
            public ResponseEntity<?> call() throws Exception {
                ResponseDto res = new ResponseDto();
                res.setState(StateType.FAIL);
                res.setMessage("ERROR: Timeout " + TestController.this.queryTimeout / 1000L + " seconds is over\nIf you want more timeout, modify agens-browser.config.yml");
                return new ResponseEntity(res.toJson(), TestController.this.productHeaders(), HttpStatus.REQUEST_TIMEOUT);
            }
        };
        WebAsyncTask<ResponseEntity<?>> response = new WebAsyncTask(this.queryTimeout, this.executor, callableDto);
        response.onTimeout(timeoutDto);
        return response;
    }

    @RequestMapping(
            value = {"hello"},
            method = {RequestMethod.GET},
            produces = {"application/json"}
    )
    public ResponseEntity<?> sayHello(@RequestParam(value = "name",defaultValue = "props") String name, HttpServletRequest request, HttpServletResponse response, Device device) {
        if (name == null) {
            return ((BodyBuilder)ResponseEntity.badRequest().headers(this.productHeaders())).body((Object)null);
        } else {
            this.logger.info(String.format("/%s/%s <-- %s", "test", "projects", name));
            String jsonStr;
            if (name.equals("node")) {
                jsonStr = "production[4.3682715]{\\\"id\\\": 3673628, \\\"kind\\\": \\\"video movie\\\", \\\"title\\\": \\\"Once Upon a Secretary\\\", \\\"md5sum\\\": \\\"3aba7079347b80e2fd1f7b1430174f9b\\\", \\\"full_info\\\": [{\\\"plot\\\": \\\"Nina is a porn star. Sharon is a secretary who wants more than just sex from her married boss Mr. Murphy. Meanwhile, tired Nina falls asleep before sex with her boyfriend Brad. He therefore calls in their neighbor Elaine as substitute. Nina wakes up but just joins in until falling asleep again. Pressured for a less tiresome job, Nina applies for a strip bar, but gets too comfortable with the clients. Meanwhile, Mr. Murphy rewards Sharon after asking her to pleasure two businessmen. Realizing she's getting paid for sex, Sharon accepts her coworker Steve's offer to become a porn star. Steve introduces Sharon to Nina who shows Sharon the ropes. They decide trading jobs. Brad comes working for Mr. Murphy too, to protect Nina from the boss who has to overhear the couple's own office sex. Sharon convinces Steve to get on camera with her.\\\"}, {\\\"certificates\\\": \\\"USA:X\\\"}, {\\\"color info\\\": \\\"Color\\\"}, {\\\"countries\\\": \\\"USA\\\"}, {\\\"genres\\\": \\\"Adult\\\"}, {\\\"languages\\\": \\\"English\\\"}, {\\\"locations\\\": \\\"New York City, New York, USA\\\"}, {\\\"runtimes\\\": \\\"77\\\"}, {\\\"tech info\\\": \\\"OFM:Video\\\"}, {\\\"tech info\\\": \\\"RAT:1.33 : 1\\\"}], \\\"phonetic_code\\\": \\\"O5215\\\", \\\"production_year\\\": 1983}";
                Node node = new Node();

                try {
                    node.setValue(jsonStr);
                } catch (SQLException var8) {
                    System.out.println("node fail: " + var8.getCause());
                }

                return new ResponseEntity(node.toString(), this.productHeaders(), HttpStatus.OK);
            } else if (name.equals("props")) {
                jsonStr = "{\\\"id\\\": 3673628, \\\"kind\\\": \\\"video movie\\\", \\\"title\\\": \\\"Once Upon a Secretary\\\", \\\"md5sum\\\": \\\"3aba7079347b80e2fd1f7b1430174f9b\\\", \\\"full_info\\\": [{\\\"plot\\\": \\\"Nina is a porn star. Sharon is a secretary who wants more than just sex from her married boss Mr. Murphy. Meanwhile, tired Nina falls asleep before sex with her boyfriend Brad. He therefore calls in their neighbor Elaine as substitute. Nina wakes up but just joins in until falling asleep again. Pressured for a less tiresome job, Nina applies for a strip bar, but gets too comfortable with the clients. Meanwhile, Mr. Murphy rewards Sharon after asking her to pleasure two businessmen. Realizing she's getting paid for sex, Sharon accepts her coworker Steve's offer to become a porn star. Steve introduces Sharon to Nina who shows Sharon the ropes. They decide trading jobs. Brad comes working for Mr. Murphy too, to protect Nina from the boss who has to overhear the couple's own office sex. Sharon convinces Steve to get on camera with her.\\\"}, {\\\"certificates\\\": \\\"USA:X\\\"}, {\\\"color info\\\": \\\"Color\\\"}, {\\\"countries\\\": \\\"USA\\\"}, {\\\"genres\\\": \\\"Adult\\\"}, {\\\"languages\\\": \\\"English\\\"}, {\\\"locations\\\": \\\"New York City, New York, USA\\\"}, {\\\"runtimes\\\": \\\"77\\\"}, {\\\"tech info\\\": \\\"OFM:Video\\\"}, {\\\"tech info\\\": \\\"RAT:1.33 : 1\\\"}], \\\"phonetic_code\\\": \\\"O5215\\\", \\\"production_year\\\": 1983}";
                JSONObject props = null;

                try {
                    props = JsonbUtil.parseJson(jsonStr);
                } catch (SQLException var9) {
                    System.out.println("props fail: " + var9.getCause());
                }

                return new ResponseEntity(props.toJSONString(), this.productHeaders(), HttpStatus.OK);
            } else if (name.equals("edge")) {
                jsonStr = "actor_in[30.2744][3.1,4.3682715]{\"md5sum\": \"072e281b4317ad0a8b0bdb459d2a2ed4\", \"role_name\": \"Bartender\", \"name_pcode_nf\": \"B6353\"}";
                Edge edge = new Edge();

                try {
                    edge.setValue(jsonStr);
                } catch (SQLException var10) {
                    System.out.println("edge fail: " + var10.getCause());
                }

                return new ResponseEntity(edge.toString(), this.productHeaders(), HttpStatus.OK);
            } else if (name.equals("graph")) {
                jsonStr = "[person[3.1]{\"id\": 718, \"name\": \"A., David\", \"gender\": \"m\", \"md5sum\": \"cf45e7b42fbc800c61462988ad1156d2\", \"name_pcode_cf\": \"A313\", \"name_pcode_nf\": \"D13\", \"surname_pcode\": \"A\"}, actor_in[30.2744][3.1,4.3682715]{\"md5sum\": \"072e281b4317ad0a8b0bdb459d2a2ed4\", \"role_name\": \"Bartender\", \"name_pcode_nf\": \"B6353\"}, production[4.3682715]{\"id\": 3673628, \"kind\": \"video movie\", \"title\": \"Once Upon a Secretary\", \"md5sum\": \"3aba7079347b80e2fd1f7b1430174f9b\", \"full_info\": [{\"plot\": \"Nina is a porn star. Sharon is a secretary who wants more than just sex from her married boss Mr. Murphy. Meanwhile, tired Nina falls asleep before sex with her boyfriend Brad. He therefore calls in their neighbor Elaine as substitute. Nina wakes up but just joins in until falling asleep again. Pressured for a less tiresome job, Nina applies for a strip bar, but gets too comfortable with the clients. Meanwhile, Mr. Murphy rewards Sharon after asking her to pleasure two businessmen. Realizing she's getting paid for sex, Sharon accepts her coworker Steve's offer to become a porn star. Steve introduces Sharon to Nina who shows Sharon the ropes. They decide trading jobs. Brad comes working for Mr. Murphy too, to protect Nina from the boss who has to overhear the couple's own office sex. Sharon convinces Steve to get on camera with her.\"}, {\"certificates\": \"USA:X\"}, {\"color info\": \"Color\"}, {\"countries\": \"USA\"}, {\"genres\": \"Adult\"}, {\"languages\": \"English\"}, {\"locations\": \"New York City, New York, USA\"}, {\"runtimes\": \"77\"}, {\"tech info\": \"OFM:Video\"}, {\"tech info\": \"RAT:1.33 : 1\"}], \"phonetic_code\": \"O5215\", \"production_year\": 1983}]";
                Graph graph = new Graph();

                try {
                    graph.setValue(jsonStr);
                } catch (SQLException var11) {
                    System.out.println("graph fail: " + var11.getCause());
                }

                return new ResponseEntity(graph.toString(), this.productHeaders(), HttpStatus.OK);
            } else {
                return new ResponseEntity(name, this.productHeaders(), HttpStatus.OK);
            }
        }
    }

    @RequestMapping(
            value = {"meta"},
            method = {RequestMethod.GET},
            produces = {"application/json"}
    )
    public ResponseEntity<?> getMeta(HttpServletRequest request, HttpServletResponse response, Device device) {
        this.logger.info(String.format("/%s/%s", "test", "meta"));
        return ((BodyBuilder)ResponseEntity.badRequest().headers(this.productHeaders())).body((Object)null);
    }

    @RequestMapping(
            value = {"query"},
            method = {RequestMethod.GET},
            produces = {"application/json"}
    )
    public ResponseEntity<?> doQuery(@RequestParam(value = "sql",defaultValue = "match path=(a)-[b:actor_in]->(c) return path, a, b, c limit 10;") String query, HttpServletRequest request, HttpServletResponse response, Device device) {
        if (query == null) {
            return ((BodyBuilder)ResponseEntity.badRequest().headers(this.productHeaders())).body((Object)null);
        } else {
            this.logger.info(String.format("/%s/%s?sql=%s", "test", "query", query));
            return new ResponseEntity("OK", this.productHeaders(), HttpStatus.OK);
        }
    }
}
