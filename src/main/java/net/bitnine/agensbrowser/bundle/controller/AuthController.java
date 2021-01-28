//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

package net.bitnine.agensbrowser.bundle.controller;

import java.util.List;
import javax.servlet.http.HttpServletRequest;
import net.bitnine.agensbrowser.bundle.message.ClientDto;
import net.bitnine.agensbrowser.bundle.message.ResponseDto;
import net.bitnine.agensbrowser.bundle.message.ResponseDto.StateType;
import net.bitnine.agensbrowser.bundle.storage.ClientStorage;
import net.bitnine.agensbrowser.bundle.util.TokenUtil;
import org.json.simple.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.mobile.device.Device;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

@RestController
@RequestMapping({"${agens.config.base_path}/auth"})
public class AuthController {
    protected final Logger logger = LoggerFactory.getLogger(this.getClass());
    private static final String txBase = "auth";
    @Value("${agens.config.base_path}")
    private String basePath;
    @Value("${agens.product.name}")
    private String productName;
    @Value("${agens.product.version}")
    private String productVersion;
    @Value("${agens.product.hello_msg}")
    private String hello_msg;
    @Value("${agens.jwt.header}")
    private String ssidHeader;
    @Autowired
    private TokenUtil jwtTokenUtil;
    @Autowired
    ClientStorage clients;

    public AuthController() {
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
            value = {"connect"},
            method = {RequestMethod.GET},
            produces = {"application/json; charset=utf-8"}
    )
    public ResponseEntity<?> connectRequest(HttpServletRequest request, Device device) throws InterruptedException {
        String userIp = request.getRemoteAddr();
        this.logger.info(String.format("/%s/%s/%s?addr=%s", this.basePath, "auth", "connect", userIp));
        ClientDto client = new ClientDto(this.clients.getCurrentUserName(), userIp, device);
        String token = this.jwtTokenUtil.generateToken(client);
        HttpStatus status = HttpStatus.SERVICE_UNAVAILABLE;
        if (token != null) {
            status = HttpStatus.OK;
            client.setToken(token);
            this.clients.addClient(client.getSsid(), client);
            client.setValid(this.jwtTokenUtil.validateToken(token, client));
            client.setState(StateType.SUCCESS);
            client.setMessage(this.hello_msg);
        } else {
            client.setState(StateType.FAIL);
            client.setMessage("ERROR: To generate token is failed! Try again or ask to administrator.");
            client.set_link(ServletUriComponentsBuilder.fromCurrentRequest().replacePath("/" + this.basePath + "/core/connect").replaceQuery("").toUriString());
        }

        return new ResponseEntity(client.toJson(), this.productHeaders(), status);
    }

    @RequestMapping(
            value = {"refresh"},
            method = {RequestMethod.GET},
            produces = {"application/json; charset=utf-8"}
    )
    public ResponseEntity<?> connectRefresh(HttpServletRequest request, Device device) throws InterruptedException {
        String ssid = request.getHeader(this.ssidHeader) == null ? "1234567890" : request.getHeader(this.ssidHeader);
        this.logger.info(String.format("/%s/%s/%s?ssid=%s", this.basePath, "auth", "refresh", ssid));
        ClientDto client = this.clients.getClient(ssid);
        if (client == null) {
            return this.unauthorizedMessage();
        } else {
            String token = this.jwtTokenUtil.refreshToken(client.getToken());
            HttpStatus status = HttpStatus.PARTIAL_CONTENT;
            if (token != null) {
                status = HttpStatus.OK;
                client.setToken(token);
                client.setValid(this.jwtTokenUtil.validateToken(token, client));
                client.setState(StateType.SUCCESS);
                client.setMessage(this.hello_msg);
            } else {
                client.setValid(this.jwtTokenUtil.validateToken(client.getToken(), client));
                client.setState(StateType.FAIL);
                client.setMessage("ERROR: To refresh token is failed! Try again or start from login.");
                client.set_link(ServletUriComponentsBuilder.fromCurrentRequest().replacePath("/" + this.basePath + "/core/connect").replaceQuery("").toUriString());
            }

            return new ResponseEntity(client.toJson(), this.productHeaders(), status);
        }
    }

    @RequestMapping(
            value = {"valid"},
            method = {RequestMethod.GET},
            produces = {"application/json; charset=utf-8"}
    )
    public ResponseEntity<?> connectValid(HttpServletRequest request, Device device) throws InterruptedException {
        String ssid = request.getHeader(this.ssidHeader) == null ? "1234567890" : request.getHeader(this.ssidHeader);
        this.logger.info(String.format("/%s/%s/%s?ssid=%s", this.basePath, "auth", "valid", ssid));
        ClientDto client = this.clients.getClient(ssid);
        if (client == null) {
            return this.unauthorizedMessage();
        } else {
            String token = client.getToken();
            client.setValid(this.jwtTokenUtil.validateToken(token, client));
            client.setState(StateType.SUCCESS);
            client.setMessage(this.hello_msg);
            return new ResponseEntity(client.toJson(), this.productHeaders(), HttpStatus.OK);
        }
    }

    @RequestMapping(
            value = {"disconnect"},
            method = {RequestMethod.GET},
            produces = {"application/json; charset=utf-8"}
    )
    public ResponseEntity<?> disconnect(HttpServletRequest request, Device device) throws InterruptedException {
        String ssid = request.getHeader(this.ssidHeader) == null ? "1234567890" : request.getHeader(this.ssidHeader);
        this.logger.info(String.format("/%s/%s/%s?ssid=%s", this.basePath, "auth", "disconnect", ssid));
        String userIp = request.getRemoteAddr();
        ClientDto client = this.clients.getClient(ssid);
        if (client != null && client.getUserIp().equals(userIp)) {
            Boolean done = this.clients.removeClient(ssid);
            ResponseDto response = new ResponseDto();
            response.setState(StateType.SUCCESS);
            response.setMessage("disconnect client: ssid='" + ssid + "', done=" + done);
            response.set_link(ServletUriComponentsBuilder.fromCurrentRequest().replacePath("/" + this.basePath + "/core/connect").replaceQuery("").toUriString());
            return new ResponseEntity(response.toJson(), this.productHeaders(), HttpStatus.OK);
        } else {
            return this.unauthorizedMessage();
        }
    }

    @RequestMapping(
            value = {"clients"},
            method = {RequestMethod.GET},
            produces = {"application/json; charset=utf-8"}
    )
    public ResponseEntity<?> listClients(HttpServletRequest request, Device device) throws InterruptedException {
        String ssid = request.getHeader(this.ssidHeader) == null ? "1234567890" : request.getHeader(this.ssidHeader);
        this.logger.info(String.format("/%s/%s/%s?ssid=%s", this.basePath, "auth", "clients", ssid));
        ClientDto client = this.clients.getClient(ssid);
        if (client == null) {
            return this.unauthorizedMessage();
        } else {
            List<JSONObject> list = this.clients.getAllClients();
            return new ResponseEntity(list, this.productHeaders(), HttpStatus.OK);
        }
    }
}
