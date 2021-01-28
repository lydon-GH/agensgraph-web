//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

package net.bitnine.agensbrowser.bundle.message;

import java.io.Serializable;
import net.bitnine.agensbrowser.bundle.persistence.outer.model.Graph;
import net.bitnine.agensbrowser.bundle.persistence.outer.model.Record;
import org.json.simple.JSONObject;

public class ResultDto extends ResponseDto implements Serializable {
    private static final long serialVersionUID = 7998913526704164587L;
    private RequestDto request;
    private Record record;
    private Graph graph;

    public ResultDto() {
        this.request = null;
        this.record = new Record();
        this.graph = new Graph();
    }

    public ResultDto(RequestDto request) {
        this.request = request;
        this.record = new Record();
        this.graph = new Graph();
    }

    public RequestDto getRequest() {
        return this.request;
    }

    public Record getRecord() {
        return this.record;
    }

    public Graph getGraph() {
        return this.graph;
    }

    public void setRequest(RequestDto request) {
        this.request = request;
    }

    public void setRecord(Record record) {
        this.record = record;
    }

    public void setGraph(Graph graph) {
        this.graph = graph;
    }

    @Override
    public String toString() {
        return "{\"result\": " + this.toJson().toJSONString() + "}";
    }

    @Override
    public JSONObject toJson() {
        JSONObject json = new JSONObject();
        json.put("request", this.request.toJson());
        json.put("graph", this.graph.toJson());
        json.put("record", this.record.toJson());
        json.put("state", this.state.toString());
        json.put("message", this.message);
        return json;
    }
}
