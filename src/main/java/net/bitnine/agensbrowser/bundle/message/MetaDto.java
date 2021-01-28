//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

package net.bitnine.agensbrowser.bundle.message;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import net.bitnine.agensbrowser.bundle.persistence.outer.model.Graph;
import net.bitnine.agensbrowser.bundle.persistence.outer.model.type.GraphType;
import net.bitnine.agensbrowser.bundle.persistence.outer.model.type.LabelType;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

public class MetaDto extends ResponseDto implements Serializable {
    private static final long serialVersionUID = -6913045165388361075L;
    private Boolean isDirty;
    private GraphType graph;
    private List<LabelType> labels;
    private Graph meta;

    public MetaDto() {
        this.isDirty = true;
        this.graph = new GraphType();
        this.labels = new ArrayList();
        this.meta = new Graph();
    }

    public MetaDto(Boolean isDirty, GraphType graph, List<LabelType> labels, Graph meta) {
        this.isDirty = isDirty;
        this.graph = graph;
        this.labels = labels;
        this.meta = meta;
    }

    public Boolean getDirty() {
        return this.isDirty;
    }

    public GraphType getGraph() {
        return this.graph;
    }

    public List<LabelType> getLabels() {
        return this.labels;
    }

    public Graph getMeta() {
        return this.meta;
    }

    public void setDirty(Boolean dirty) {
        this.isDirty = dirty;
    }

    public void setGraph(GraphType graph) {
        this.graph = graph;
    }

    public void setLabels(List<LabelType> labels) {
        this.labels = labels;
    }

    public void setMeta(Graph meta) {
        this.meta = meta;
    }

    @Override
    public String toString() {
        return "{\"meta\": " + this.toJson().toJSONString() + "}";
    }

    @Override
    public JSONObject toJson() {
        JSONObject json = new JSONObject();
        json.put("is_dirty", this.isDirty);
        json.put("graph", this.graph == null ? null : this.graph.toJson());
        JSONArray jsonArray = new JSONArray();
        Iterator iter = this.labels.iterator();

        while(iter.hasNext()) {
            jsonArray.add(((LabelType)iter.next()).toJson());
        }

        json.put("labels", jsonArray);
        json.put("meta", this.meta.toJson());
        json.put("state", this.state.toString());
        json.put("message", this.message);
        return json;
    }
}
