//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

package net.bitnine.agensbrowser.bundle.message;

import java.io.Serializable;
import net.bitnine.agensbrowser.bundle.persistence.outer.model.type.LabelType;
import org.json.simple.JSONObject;

public class LabelDto extends ResponseDto implements Serializable {
    private static final long serialVersionUID = -6913045165388361075L;
    private RequestDto request;
    private LabelType label;

    public LabelDto() {
        this.request = null;
        this.label = new LabelType();
    }

    public LabelDto(RequestDto request) {
        this.request = request;
        this.label = new LabelType();
    }

    public LabelDto(RequestDto request, LabelType label) {
        this.request = request;
        this.label = label;
    }

    public RequestDto getRequest() {
        return this.request;
    }

    public LabelType getLabel() {
        return this.label;
    }

    public void setRequest(RequestDto request) {
        this.request = request;
    }

    public void setLabel(LabelType label) {
        this.label = label;
    }

    @Override
    public String toString() {
        return "{\"label\": " + this.toJson().toJSONString() + "}";
    }

    @Override
    public JSONObject toJson() {
        JSONObject json = new JSONObject();
        if (this.request != null) {
            json.put("request", this.request.toJson());
        }

        if (this.label != null) {
            json.put("label", this.label.toJson());
        } else {
            json.put("label", (new LabelType()).toJson());
        }

        json.put("state", this.state.toString());
        json.put("message", this.message);
        return json;
    }
}
