//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

package net.bitnine.agensbrowser.bundle.persistence.outer.model.type;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.UUID;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

public class LabelType implements Cloneable {
    private String oid;
    private String name;
    private String owner;
    private String desc;
    private Long size;
    private Long sizeNotEmpty;
    private Boolean isDirty;
    private List<PropertyType> properties;
    private Set<String> neighbors;
    private LabelType.ElemType type;

    public LabelType() {
        this.oid = UUID.randomUUID().toString();
        this.name = "";
        this.type = LabelType.ElemType.NONE;
        this.owner = "";
        this.desc = "";
        this.size = 0L;
        this.sizeNotEmpty = 0L;
        this.isDirty = true;
        this.properties = new ArrayList();
        this.neighbors = new HashSet();
    }

    public LabelType(String name, String type) {
        this.oid = UUID.randomUUID().toString();
        this.name = name;
        this.type = toElemType(type);
        this.owner = "";
        this.desc = "";
        this.size = 0L;
        this.sizeNotEmpty = 0L;
        this.isDirty = true;
        this.properties = new ArrayList();
        this.neighbors = new HashSet();
    }

    public LabelType(String oid, String name, String type, String owner, String desc) {
        this.oid = oid;
        this.name = name;
        this.type = toElemType(type);
        this.owner = owner;
        this.desc = desc;
        this.size = 0L;
        this.sizeNotEmpty = 0L;
        this.isDirty = true;
        this.properties = new ArrayList();
        this.neighbors = new HashSet();
    }

    public static final LabelType.ElemType toElemType(String elemType) {
        if (elemType.equalsIgnoreCase(LabelType.ElemType.EDGE.toString())) {
            return LabelType.ElemType.EDGE;
        } else {
            return elemType.equalsIgnoreCase(LabelType.ElemType.NODE.toString()) ? LabelType.ElemType.NODE : LabelType.ElemType.NONE;
        }
    }

    public String getOid() {
        return this.oid;
    }

    public String getName() {
        return this.name;
    }

    public LabelType.ElemType getType() {
        return this.type;
    }

    public String getOwner() {
        return this.owner;
    }

    public String getDesc() {
        return this.desc;
    }

    public Long getSize() {
        return this.size;
    }

    public Long getSizeNotEmpty() {
        return this.sizeNotEmpty;
    }

    public Boolean getIsDirty() {
        return this.isDirty;
    }

    public List<PropertyType> getProperties() {
        return this.properties;
    }

    public Set<String> getNeighbors() {
        return this.neighbors;
    }

    public void setOid(String oid) {
        this.oid = oid;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setType(LabelType.ElemType type) {
        this.type = type;
    }

    public void setOwner(String owner) {
        this.owner = owner;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public void setSize(Long size) {
        this.size = size;
    }

    public void setSizeNotEmpty(Long sizeNotEmpty) {
        this.sizeNotEmpty = sizeNotEmpty;
    }

    public void setIsDirty(Boolean isDirty) {
        this.isDirty = isDirty;
    }

    public void setProperties(List<PropertyType> properties) {
        this.properties = properties;
    }

    public void setNeighbors(Set<String> neighbors) {
        this.neighbors = neighbors;
    }

    public void incrementSize() {
        this.size = this.size + 1L;
    }

    public void incrementSizeNotEmpty() {
        this.sizeNotEmpty = this.sizeNotEmpty + 1L;
    }

    public void resetSizeProperties(int val) {
        Iterator iter = this.properties.iterator();

        while(iter.hasNext()) {
            ((PropertyType)iter.next()).setSize((long)val);
        }

    }

    public void incrementSizeProperties(Map<String, Object> props) {
        Iterator iter = this.properties.iterator();

        while(iter.hasNext()) {
            PropertyType property = (PropertyType)iter.next();
            if (props.containsKey(property.getKey())) {
                property.incrementSize();
            }
        }

    }

    @Override
    public String toString() {
        return "{\"label_type\": " + this.toJson().toJSONString() + "}";
    }

    public JSONObject toJson() {
        JSONObject json = new JSONObject();
        json.put("oid", this.oid);
        json.put("name", this.name);
        json.put("type", this.type.toString());
        json.put("owner", this.owner);
        json.put("desc", this.desc);
        json.put("size", this.size);
        json.put("size_not_empty", this.sizeNotEmpty);
        json.put("is_dirty", this.isDirty);
        JSONArray propertyArray = new JSONArray();
        Iterator iter = this.properties.iterator();

        while(iter.hasNext()) {
            propertyArray.add(((PropertyType)iter.next()).toJson());
        }

        json.put("properties", propertyArray);
        JSONArray neighborArray = new JSONArray();
        Iterator iter2 = this.neighbors.iterator();

        while(iter2.hasNext()) {
            neighborArray.add(iter2.next());
        }

        json.put("neighbors", neighborArray);
        return json;
    }

    @Override
    public int hashCode() {
        int[] prime = new int[]{17, 29, 31, 37, 73};
        int result = prime[0];
        result = prime[1] * result + prime[2] * (this.oid == null ? 0 : this.oid.hashCode());
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
            LabelType other = (LabelType)obj;
            if (this.oid != null && other.oid != null && this.oid.equals(other.oid)) {
                return true;
            } else {
                return this.name != null && other.name != null && this.name.equals(other.name) && this.type != null && other.type != null && this.type.equals(other.type);
            }
        }
    }

    public Object clone() throws CloneNotSupportedException {
        LabelType label = (LabelType)super.clone();
        List<PropertyType> cloneProperties = new ArrayList();
        Iterator iter = label.properties.iterator();

        while(iter.hasNext()) {
            PropertyType cloneProp = (PropertyType)((PropertyType)iter.next()).clone();
            cloneProperties.add(cloneProp);
        }

        label.setProperties(cloneProperties);
        return label;
    }

    public static enum ElemType {
        EDGE,
        NODE,
        NONE;

        private ElemType() {
        }
    }
}
