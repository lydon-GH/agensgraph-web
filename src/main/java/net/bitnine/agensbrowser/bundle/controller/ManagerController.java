//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

package net.bitnine.agensbrowser.bundle.controller;

import java.util.List;
import java.util.concurrent.atomic.AtomicLong;
import javax.servlet.http.HttpServletRequest;
import net.bitnine.agensbrowser.bundle.message.ClientDto;
import net.bitnine.agensbrowser.bundle.message.ProjectDto;
import net.bitnine.agensbrowser.bundle.message.ResponseDto;
import net.bitnine.agensbrowser.bundle.message.ResponseDto.StateType;
import net.bitnine.agensbrowser.bundle.persistence.inner.model.AgensLog;
import net.bitnine.agensbrowser.bundle.persistence.inner.model.AgensProject;
import net.bitnine.agensbrowser.bundle.persistence.inner.service.AgensService;
import net.bitnine.agensbrowser.bundle.storage.ClientStorage;
import org.json.simple.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.ResponseEntity.BodyBuilder;
import org.springframework.mobile.device.Device;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

@RestController
@RequestMapping({"${agens.config.base_path}/manager"})
public class ManagerController {
    protected final Logger logger = LoggerFactory.getLogger(this.getClass());
    private final AtomicLong txSeq = new AtomicLong(11L);
    private static final String txBase = "manager";
    @Value("${agens.config.base_path}")
    private String basePath;
    @Value("${agens.outer.datasource.graph_path}")
    private String graphPath;
    @Value("${agens.product.name}")
    private String productName;
    @Value("${agens.product.version}")
    private String productVersion;
    @Value("${agens.jwt.header}")
    private String ssidHeader;
    private AgensService agensService;
    private ClientStorage clients;

    private final String getTxid() {
        return "manager#" + String.valueOf(this.txSeq.getAndIncrement());
    }

    @Autowired
    public ManagerController(AgensService agensService, ClientStorage clients) {
        this.agensService = agensService;
        this.clients = clients;
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
            value = {"projects/all"},
            method = {RequestMethod.GET},
            produces = {"application/json; charset=utf-8"}
    )
    public ResponseEntity<?> getProjectsAll(HttpServletRequest request, Device device) throws InterruptedException {
        String ssid = request.getHeader(this.ssidHeader) == null ? "1234567890" : request.getHeader(this.ssidHeader);
        this.logger.info(String.format("/%s/%s/%s?ssid=%s", this.basePath, "manager", "projects/all", ssid));
        ClientDto client = this.clients.getClient(ssid);
        if (client == null) {
            return this.unauthorizedMessage();
        } else {
            List<AgensProject> list = this.agensService.findProjectsAll();
            return list == null ? ((BodyBuilder)ResponseEntity.badRequest().headers(this.productHeaders())).body((Object)null) : new ResponseEntity(list, this.productHeaders(), HttpStatus.OK);
        }
    }

    @RequestMapping(
            value = {"projects"},
            method = {RequestMethod.GET},
            produces = {"application/json; charset=utf-8"}
    )
    public ResponseEntity<?> getProjects(HttpServletRequest request, Device device) throws InterruptedException {
        String ssid = request.getHeader(this.ssidHeader) == null ? "1234567890" : request.getHeader(this.ssidHeader);
        this.logger.info(String.format("/%s/%s/%s?ssid=%s", this.basePath, "manager", "projects", ssid));
        ClientDto client = this.clients.getClient(ssid);
        if (client == null) {
            return this.unauthorizedMessage();
        } else {
            List<AgensProject> list = this.agensService.findProjectsByUserName();
            return list == null ? ((BodyBuilder)ResponseEntity.badRequest().headers(this.productHeaders())).body((Object)null) : new ResponseEntity(list, this.productHeaders(), HttpStatus.OK);
        }
    }

    @RequestMapping(
            value = {"projects/{id}"},
            method = {RequestMethod.GET},
            produces = {"application/json; charset=utf-8"}
    )
    public ResponseEntity<?> getProjectById(@PathVariable Integer id, HttpServletRequest request, Device device) throws InterruptedException {
        String ssid = request.getHeader(this.ssidHeader) == null ? "1234567890" : request.getHeader(this.ssidHeader);
        this.logger.info(String.format("/%s/%s/%s/%d?ssid=%s", this.basePath, "manager", "projects", id, ssid));
        ClientDto client = this.clients.getClient(ssid);
        if (client == null) {
            return this.unauthorizedMessage();
        } else {
            AgensProject project = this.agensService.findOneProjectById(id);
            if (project == null) {
                ResponseDto response = new ResponseDto();
                response.setState(StateType.FAIL);
                response.setMessage(String.format("Project select fail: id=%d, Check this on project list", id));
                response.set_link(ServletUriComponentsBuilder.fromCurrentRequest().replacePath("/" + this.basePath + "/manager/projects").replaceQuery("").toUriString());
                return new ResponseEntity(response.toJson(), this.productHeaders(), HttpStatus.BAD_REQUEST);
            } else {
                return new ResponseEntity(project, this.productHeaders(), HttpStatus.OK);
            }
        }
    }

    @RequestMapping(
            value = {"projects/delete/{id}"},
            method = {RequestMethod.GET},
            produces = {"application/json; charset=utf-8"}
    )
    public ResponseEntity<?> deleteProjectById(@PathVariable Integer id, HttpServletRequest request, Device device) throws InterruptedException {
        String ssid = request.getHeader(this.ssidHeader) == null ? "1234567890" : request.getHeader(this.ssidHeader);
        this.logger.info(String.format("/%s/%s/%s/%d?ssid=%s", this.basePath, "manager", "projects/delete", id, ssid));
        ClientDto client = this.clients.getClient(ssid);
        if (client == null) {
            return this.unauthorizedMessage();
        } else {
            Boolean result = this.agensService.deleteProject(id);
            ResponseDto response = new ResponseDto();
            if (!result) {
                response.setState(StateType.FAIL);
                response.setMessage(String.format("Project delete fail: id=%d, Check this project", id));
                response.set_link(ServletUriComponentsBuilder.fromCurrentRequest().replacePath("/" + this.basePath + "/manager/projects/" + id).replaceQuery("").toUriString());
                return new ResponseEntity(response.toJson(), this.productHeaders(), HttpStatus.BAD_REQUEST);
            } else {
                response.setState(StateType.SUCCESS);
                response.setMessage(String.format("Project delete success: id=%s, Reload projects", id));
                response.set_link(ServletUriComponentsBuilder.fromCurrentRequest().replacePath("/" + this.basePath + "/manager/projects").replaceQuery("").toUriString());
                return new ResponseEntity(response.toJson(), this.productHeaders(), HttpStatus.OK);
            }
        }
    }

    @RequestMapping(
            value = {"projects/save"},
            method = {RequestMethod.POST},
            consumes = {"application/json; charset=utf-8"},
            produces = {"application/json; charset=utf-8"}
    )
    public ResponseEntity<?> saveProject(@RequestBody ProjectDto dto, HttpServletRequest request, Device device) throws InterruptedException {
        String ssid = request.getHeader(this.ssidHeader) == null ? "1234567890" : request.getHeader(this.ssidHeader);
        this.logger.info(String.format("/%s/%s/%s?ssid=%s&title=%s", this.basePath, "manager", "projects/save", ssid, dto.getTitle()));
        ClientDto client = this.clients.getClient(ssid);
        if (client == null) {
            return this.unauthorizedMessage();
        } else {
            AgensProject project = new AgensProject(client, dto);
            project = this.agensService.saveProject(project);
            if (project == null) {
                ResponseDto response = new ResponseDto();
                response.setState(StateType.FAIL);
                String projId = String.valueOf(dto.getId());
                response.setMessage(String.format("Project save fail: id=%s, title=\"%s\". Check this project", projId, dto.getTitle()));
                response.set_link(ServletUriComponentsBuilder.fromCurrentRequest().replacePath("/" + this.basePath + "/manager/projects/" + projId).replaceQuery("").toUriString());
                return new ResponseEntity(response.toJson(), this.productHeaders(), HttpStatus.BAD_REQUEST);
            } else {
                return new ResponseEntity(project, this.productHeaders(), HttpStatus.OK);
            }
        }
    }

    @RequestMapping(
            value = {"logs/all"},
            method = {RequestMethod.GET},
            produces = {"application/json; charset=utf-8"}
    )
    public ResponseEntity<?> getLogsAll(HttpServletRequest request, Device device) throws InterruptedException {
        String ssid = request.getHeader(this.ssidHeader) == null ? "1234567890" : request.getHeader(this.ssidHeader);
        this.logger.info(String.format("/%s/%s/%s?ssid=%s", this.basePath, "manager", "logs/all", ssid));
        ClientDto client = this.clients.getClient(ssid);
        if (client == null) {
            return this.unauthorizedMessage();
        } else {
            List<AgensLog> list = this.agensService.findLogsAll();
            return list == null ? ((BodyBuilder)ResponseEntity.badRequest().headers(this.productHeaders())).body((Object)null) : new ResponseEntity(list, this.productHeaders(), HttpStatus.OK);
        }
    }

    @RequestMapping(
            value = {"logs"},
            method = {RequestMethod.GET},
            produces = {"application/json; charset=utf-8"}
    )
    public ResponseEntity<?> getLogs(HttpServletRequest request, Device device) throws InterruptedException {
        String ssid = request.getHeader(this.ssidHeader) == null ? "1234567890" : request.getHeader(this.ssidHeader);
        this.logger.info(String.format("/%s/%s/%s?ssid=%s", this.basePath, "manager", "logs", ssid));
        ClientDto client = this.clients.getClient(ssid);
        if (client == null) {
            return this.unauthorizedMessage();
        } else {
            List<AgensLog> list = this.agensService.findLogsByUserName();
            return list == null ? ((BodyBuilder)ResponseEntity.badRequest().headers(this.productHeaders())).body((Object)null) : new ResponseEntity(list, this.productHeaders(), HttpStatus.OK);
        }
    }

    @RequestMapping(
            value = {"logs/{id}"},
            method = {RequestMethod.GET},
            produces = {"application/json; charset=utf-8"}
    )
    public ResponseEntity<?> getLogById(@PathVariable Integer id, HttpServletRequest request, Device device) throws InterruptedException {
        String ssid = request.getHeader(this.ssidHeader) == null ? "1234567890" : request.getHeader(this.ssidHeader);
        this.logger.info(String.format("/%s/%s/%s/%d?ssid=%s", this.basePath, "manager", "logs", id, ssid));
        ClientDto client = this.clients.getClient(ssid);
        if (client == null) {
            return this.unauthorizedMessage();
        } else {
            AgensLog log = this.agensService.findOneLogById(id);
            return log == null ? ((BodyBuilder)ResponseEntity.badRequest().headers(this.productHeaders())).body((Object)null) : new ResponseEntity(log, this.productHeaders(), HttpStatus.OK);
        }
    }
}
