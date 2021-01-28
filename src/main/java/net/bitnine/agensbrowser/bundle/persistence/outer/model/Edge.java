//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

package net.bitnine.agensbrowser.bundle.persistence.outer.model;

import java.io.Serializable;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map.Entry;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import net.bitnine.agensbrowser.bundle.persistence.outer.model.type.LabelType;
import net.bitnine.agensbrowser.bundle.persistence.outer.model.type.LabelType.ElemType;
import net.bitnine.agensbrowser.bundle.util.JsonbUtil;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.postgresql.util.PSQLException;
import org.postgresql.util.PSQLState;

public class Edge extends Node implements Serializable, Cloneable {
    private static final long serialVersionUID = -626958274497873643L;
    private static final String type;
    private static final Pattern edgePattern;
    private String source;
    private String target;

    public Edge() {
        this.source = "";
        this.target = "";
    }

    public Edge(String id) {
        super(id);
        this.source = "";
        this.target = "";
    }

    public Edge(LabelType label, String source, String target) {
        this.id = label.getOid();
        this.labels = new ArrayList();
        this.setLabel(label.getType().toString());
        this.source = source;
        this.target = target;
        this.props = new HashMap();
        this.props.put("id", label.getOid());
        this.props.put("name", label.getName());
        this.props.put("is_dirty", label.getIsDirty());
        this.props.put("owner", label.getOwner());
        this.props.put("desc", label.getDesc());
        this.props.put("size", label.getSize());
        this.props.put("size_not_empty", label.getSizeNotEmpty());
        this.size = label.getSize();
    }

    public String getType() {
        return type;
    }

    public String getSource() {
        return this.source;
    }

    public String getTarget() {
        return this.target;
    }

    public void setSource(String source) {
        this.source = source;
    }

    public void setTarget(String target) {
        this.target = target;
    }

    public String toString() {
        return "{\"edge\": " + this.toJson().toJSONString() + "}";
    }

    public JSONObject toJson() {
        JSONObject json = new JSONObject();
        json.put("id", this.id);
        json.put("name", this.getPropertyName());
        JSONArray jsonArray = new JSONArray();
        Iterator iter = this.labels.iterator();

        while(iter.hasNext()) {
            jsonArray.add(iter.next());
        }

        json.put("labels", jsonArray);
        json.put("source", this.source);
        json.put("target", this.target);
        JSONObject jsonObject = new JSONObject();
        Iterator var4 = this.props.entrySet().iterator();

        while(var4.hasNext()) {
            Entry<String, Object> elem = (Entry)var4.next();
            jsonObject.put(elem.getKey(), elem.getValue());
        }

        json.put("props", jsonObject);
        json.put("size", this.size);
        JSONObject parent = new JSONObject();
        parent.put("data", json);
        return parent;
    }

    public int hashCode() {
        int[] prime = new int[]{17, 29, 31, 37, 73};
        int result = prime[0];
        result = prime[1] * result + prime[2] * (this.id == null ? 0 : this.id.hashCode()) + prime[3] * (this.source == null ? 0 : this.source.hashCode()) + prime[4] * (this.target == null ? 0 : this.target.hashCode());
        return result;
    }

    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        } else if (obj == null) {
            return false;
        } else if (this.getClass() != obj.getClass()) {
            return false;
        } else {
            Edge other = (Edge)obj;
            return this.id != null && other.id != null && this.id.equals(other.id) && this.source != null && other.source != null && this.source.equals(other.source) && this.target != null && other.target != null && this.target.equals(other.target);
        }
    }

    public void setValue(String value) throws SQLException {
        Matcher m = edgePattern.matcher(value);
        if (m.find()) {
            this.labels.add(m.group(1).trim());
            this.id = m.group(2).trim();
            this.source = m.group(3).trim();
            this.target = m.group(4).trim();
            this.props = JsonbUtil.parseJsonToMap(m.group(5));
        } else {
            throw new PSQLException("Parsing EDGE failed", PSQLState.DATA_ERROR);
        }
    }

    public Object clone() throws CloneNotSupportedException {
        return (Edge)super.clone();
    }

    static {
        type = ElemType.EDGE.toString();
        edgePattern = Pattern.compile("(.+?)\\[(.+?)\\]\\[(.+?),(.+?)\\](.*)");
    }
}
