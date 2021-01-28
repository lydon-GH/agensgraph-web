//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

package net.bitnine.agensbrowser.bundle.message;

import java.io.Serializable;
import org.json.simple.JSONObject;

public class ProjectDto implements Serializable {
    private static final long serialVersionUID = 3714942898323624874L;
    private Integer id;
    private String title;
    private String description;
    private String sql;
    private String graph_json;

    public ProjectDto() {
        this.id = null;
        this.title = "";
        this.description = "";
        this.sql = "";
        this.graph_json = "";
    }

    public ProjectDto(Integer id, String title, String description, String sql, String graph_json) {
        this.id = id;
        this.title = title;
        this.description = description;
        this.sql = sql;
        this.graph_json = graph_json;
    }

    public Integer getId() {
        return this.id;
    }

    public String getTitle() {
        return this.title;
    }

    public String getDescription() {
        return this.description;
    }

    public String getSql() {
        return this.sql;
    }

    public String getGraph_json() {
        return this.graph_json;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setSql(String sql) {
        this.sql = sql;
    }

    public void setGraph_json(String graph_json) {
        this.graph_json = graph_json;
    }

    public String toString() {
        return "{\"project\": " + this.toJson().toJSONString() + "}";
    }

    public JSONObject toJson() {
        JSONObject json = new JSONObject();
        json.put("id", this.id);
        json.put("title", this.title);
        json.put("description", this.description);
        json.put("sql", this.sql);
        json.put("graph_json", this.graph_json);
        return json;
    }
}
