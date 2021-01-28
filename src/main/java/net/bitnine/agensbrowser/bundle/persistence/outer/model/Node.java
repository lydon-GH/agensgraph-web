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
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import net.bitnine.agensbrowser.bundle.persistence.outer.model.type.LabelType;
import net.bitnine.agensbrowser.bundle.persistence.outer.model.type.PropertyType;
import net.bitnine.agensbrowser.bundle.persistence.outer.model.type.LabelType.ElemType;
import net.bitnine.agensbrowser.bundle.util.JsonbUtil;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.postgresql.util.PSQLException;
import org.postgresql.util.PSQLState;

public class Node implements Serializable, Cloneable {
    private static final long serialVersionUID = -9063462726499884789L;
    private static final String type;
    private static final Pattern nodePattern;
    protected String id;
    protected List<String> labels;
    protected Map<String, Object> props;
    protected Long size;

    public Node() {
        this.id = "";
        this.labels = new ArrayList();
        this.props = new HashMap();
        this.size = 1L;
    }

    public Node(String id) {
        this.id = id;
        this.labels = new ArrayList();
        this.props = new HashMap();
        this.size = 1L;
    }

    public Node(LabelType label) {
        this.id = label.getOid();
        this.labels = new ArrayList();
        this.setLabel(label.getType().toString());
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

    public String getPropertyId() {
        return this.props.containsKey("id") ? this.props.get("id").toString() : "";
    }

    public String getPropertyName() {
        String name = this.id;
        if (this.props.containsKey("name")) {
            name = this.props.get("name").toString();
        } else if (this.props.containsKey("title")) {
            name = this.props.get("title").toString();
        }

        return name;
    }

    public String getId() {
        return this.id;
    }

    public List<String> getLabels() {
        return this.labels;
    }

    public String getLabel() {
        return this.labels.size() > 0 ? (String)this.labels.get(0) : "";
    }

    public Map<String, Object> getProps() {
        return this.props;
    }

    public Object getProperty(String key) {
        return this.props.get(key);
    }

    public Long getSize() {
        return this.size;
    }

    public void setId(String id) {
        this.id = id;
    }

    public void setLabels(List<String> labels) {
        this.labels = labels;
    }

    public void setLabel(String label) {
        this.labels.add(label);
    }

    public void setProps(Map<String, Object> props) {
        this.props = props;
    }

    public void setProperty(String key, Object value) {
        this.props.put(key, value);
    }

    public void setSize(Long size) {
        this.size = size;
    }

    public String toString() {
        return "{\"node\": " + this.toJson().toJSONString() + "}";
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
        result = prime[1] * result + prime[2] * (this.id == null ? 0 : this.id.hashCode());
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
            Node other = (Node)obj;
            return this.id != null && other.id != null && this.id.equals(other.id);
        }
    }

    public void setValue(String value) throws SQLException {
        Matcher m = nodePattern.matcher(value);
        if (m.find()) {
            this.labels.add(m.group(1).trim());
            this.id = m.group(2).trim();
            this.props = JsonbUtil.parseJsonToMap(m.group(3));
        } else {
            throw new PSQLException("Parsing NODE failed", PSQLState.DATA_ERROR);
        }
    }

    public LabelType getLabelType() {
        LabelType label = new LabelType(this.getLabel(), this.getType());
        Iterator iter = this.props.keySet().iterator();

        while(iter.hasNext()) {
            label.getProperties().add(new PropertyType((String)iter.next()));
        }

        return label;
    }

    public Object clone() throws CloneNotSupportedException {
        return (Node)super.clone();
    }

    static {
        type = ElemType.NODE.toString();
        nodePattern = Pattern.compile("(.+?)\\[(.+?)\\](.*)");
    }
}
