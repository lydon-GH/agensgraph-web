//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

package net.bitnine.agensbrowser.bundle.message;

import java.io.Serializable;
import org.json.simple.JSONObject;

public class ResponseDto implements Serializable {
    private static final long serialVersionUID = 2903279623511697162L;
    protected ResponseDto.StateType state;
    protected String message;
    protected String _link;

    public static final ResponseDto.StateType toStateType(String state) {
        if (state.equalsIgnoreCase(ResponseDto.StateType.PENDING.toString())) {
            return ResponseDto.StateType.PENDING;
        } else if (state.equalsIgnoreCase(ResponseDto.StateType.SUCCESS.toString())) {
            return ResponseDto.StateType.SUCCESS;
        } else if (state.equalsIgnoreCase(ResponseDto.StateType.FAIL.toString())) {
            return ResponseDto.StateType.FAIL;
        } else {
            return state.equalsIgnoreCase(ResponseDto.StateType.KILLED.toString()) ? ResponseDto.StateType.KILLED : ResponseDto.StateType.NONE;
        }
    }

    public ResponseDto() {
        this.state = ResponseDto.StateType.NONE;
        this.message = "";
        this._link = null;
    }

    public ResponseDto.StateType getState() {
        return this.state;
    }

    public String getMessage() {
        return this.message;
    }

    public String get_link() {
        return this._link;
    }

    public void setState(ResponseDto.StateType state) {
        this.state = state;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public void set_link(String _link) {
        this._link = _link;
    }

    @Override
    public String toString() {
        return "{\"response\": " + this.toJson().toJSONString() + "}";
    }

    public JSONObject toJson() {
        JSONObject json = new JSONObject();
        json.put("state", this.state.toString());
        json.put("message", this.message);
        if (this._link != null) {
            json.put("_link", this._link);
        }

        return json;
    }

    public static enum StateType {
        PENDING,
        SUCCESS,
        FAIL,
        KILLED,
        NONE;

        private StateType() {
        }
    }
}
