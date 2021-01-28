//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

package net.bitnine.agensbrowser.bundle.message;

import java.io.Serializable;
import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.UUID;
import org.json.simple.JSONObject;
import org.springframework.mobile.device.Device;

public class ClientDto extends ResponseDto implements Serializable {
    private String ssid = UUID.randomUUID().toString();
    private String token = null;
    private Boolean valid = false;
    private String userName;
    private String userIp;
    private Device device;
    private Timestamp timestamp;
    private static final DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

    public final String getConnectDate() {
        return dateFormat.format(this.timestamp);
    }

    public ClientDto(String userName, String userIp, Device device) {
        this.userName = userName;
        this.userIp = userIp;
        this.device = device;
        this.timestamp = new Timestamp(System.currentTimeMillis());
    }

    public String getSsid() {
        return this.ssid;
    }

    public String getToken() {
        return this.token;
    }

    public Boolean getValid() {
        return this.valid;
    }

    public String getUserName() {
        return this.userName;
    }

    public String getUserIp() {
        return this.userIp;
    }

    public Device getDevice() {
        return this.device;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public void setValid(Boolean valid) {
        this.valid = valid;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public void setUserIp(String userIp) {
        this.userIp = userIp;
    }

    public void setDevice(Device device) {
        this.device = device;
    }

    @Override
    public String toString() {
        return "{\"client\": " + this.toJson().toJSONString() + "}";
    }

    @Override
    public JSONObject toJson() {
        JSONObject json = new JSONObject();
        json.put("ssid", this.ssid);
        json.put("valid", this.valid);
        json.put("user_name", this.userName);
        json.put("user_ip", this.userIp);
        json.put("timestamp", this.getConnectDate());
        json.put("state", this.state.toString());
        json.put("message", this.message);
        if (this._link != null) {
            json.put("_link", this._link);
        }

        return json;
    }

    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        } else if (obj == null) {
            return false;
        } else if (this.getClass() != obj.getClass()) {
            return false;
        } else {
            ClientDto other = (ClientDto)obj;
            if (this.token != null && other.token != null && this.token.equals(other.token)) {
                return true;
            } else {
                return this.ssid != null && other.ssid != null && this.ssid.equals(other.ssid);
            }
        }
    }
}
