//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

package net.bitnine.agensbrowser.bundle.message;

import java.io.Serializable;
import org.json.simple.JSONObject;

public class RequestDto implements Serializable {
    private static final long serialVersionUID = -8735609790292961085L;
    private String ssid;
    private String txid;
    private RequestDto.RequestType type;
    private String sql;
    private String command;
    private String target;
    private String options;

    public RequestDto() {
        this.ssid = "";
        this.txid = "";
        this.type = RequestDto.RequestType.NONE;
        this.sql = "";
        this.command = "";
        this.target = "";
        this.options = "{}";
    }

    public RequestDto(String ssid, String txid, String type) {
        this.ssid = ssid;
        this.txid = txid;
        this.type = toRequestType(type);
        this.sql = "";
        this.command = "";
        this.target = "";
        this.options = "{}";
    }

    public static RequestDto.RequestType toRequestType(String reqType) {
        if (reqType.equalsIgnoreCase(RequestDto.RequestType.CREATE.toString())) {
            return RequestDto.RequestType.CREATE;
        } else if (reqType.equalsIgnoreCase(RequestDto.RequestType.DROP.toString())) {
            return RequestDto.RequestType.DROP;
        } else if (reqType.equalsIgnoreCase(RequestDto.RequestType.QUERY.toString())) {
            return RequestDto.RequestType.QUERY;
        } else {
            return reqType.equalsIgnoreCase(RequestDto.RequestType.KILL.toString()) ? RequestDto.RequestType.KILL : RequestDto.RequestType.NONE;
        }
    }

    public String getSsid() {
        return this.ssid;
    }

    public String getTxid() {
        return this.txid;
    }

    public String getType() {
        return this.type.toString();
    }

    public String getSql() {
        return this.sql;
    }

    public String getCommand() {
        return this.command;
    }

    public String getTarget() {
        return this.target;
    }

    public String getOptions() {
        return this.options;
    }

    public void setSsid(String ssid) {
        this.ssid = ssid;
    }

    public void setTxid(String txid) {
        this.txid = txid;
    }

    public void setType(RequestDto.RequestType type) {
        this.type = type;
    }

    public void setType(String type) {
        this.type = toRequestType(type);
    }

    public void setSql(String sql) {
        this.sql = sql;
    }

    public void setCommand(String command) {
        this.command = command;
    }

    public void setTarget(String target) {
        this.target = target;
    }

    public void setOptions(String options) {
        this.options = options;
    }

    @Override
    public String toString() {
        return "{\"request\": " + this.toJson().toJSONString() + "}";
    }

    public JSONObject toJson() {
        JSONObject json = new JSONObject();
        json.put("ssid", this.ssid);
        json.put("txid", this.txid);
        json.put("type", this.type.toString());
        json.put("sql", this.sql);
        json.put("command", this.command);
        json.put("target", this.target);
        json.put("options", this.options);
        return json;
    }

    public static enum RequestType {
        CREATE,
        DROP,
        QUERY,
        KILL,
        NONE;

        private RequestType() {
        }
    }
}
