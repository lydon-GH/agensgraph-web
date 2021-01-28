//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

package net.bitnine.agensbrowser.bundle.persistence.outer.model.type;

import java.io.Serializable;
import javax.persistence.Entity;
import org.json.simple.JSONObject;

@Entity
public class GraphType implements Serializable, Cloneable {
    private static final long serialVersionUID = -2901884864870956373L;
    private String oid;
    private String name;
    private String owner;
    private String desc;
    private String jdbcUrl;
    private float version;
    private Boolean isDirty;

    public GraphType() {
        this.oid = "-1";
        this.name = "";
        this.owner = "";
        this.desc = "";
        this.jdbcUrl = "";
        this.version = 1.3F;
        this.isDirty = true;
    }

    public GraphType(String oid, String name, String owner, String desc) {
        this.oid = oid;
        this.name = name;
        this.owner = owner;
        this.desc = desc;
        this.jdbcUrl = "";
        this.version = 1.3F;
        this.isDirty = true;
    }

    public String getOid() {
        return this.oid;
    }

    public String getName() {
        return this.name;
    }

    public String getOwner() {
        return this.owner;
    }

    public String getDesc() {
        return this.desc;
    }

    public String getJdbcUrl() {
        return this.jdbcUrl;
    }

    public float getVersion() {
        return this.version;
    }

    public Boolean getIsDirty() {
        return this.isDirty;
    }

    public void setOid(String oid) {
        this.oid = oid;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setOwner(String owner) {
        this.owner = owner;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public void setIsDirty(Boolean isDirty) {
        this.isDirty = isDirty;
    }

    public void setVersion(float version) {
        this.version = version;
    }

    public void setJdbcUrl(String jdbcUrl) {
        this.jdbcUrl = jdbcUrl;
    }

    @Override
    public String toString() {
        return "{\"graph_type\": " + this.toJson().toJSONString() + "}";
    }

    public JSONObject toJson() {
        JSONObject json = new JSONObject();
        json.put("oid", this.oid);
        json.put("name", this.name);
        json.put("owner", this.owner);
        json.put("desc", this.desc);
        json.put("jdbc_url", this.jdbcUrl);
        json.put("version", this.version);
        json.put("is_dirty", this.isDirty);
        return json;
    }

    @Override
    public int hashCode() {
        int[] prime = new int[]{17, 29, 31, 37, 73};
        int result = prime[0];
        result = prime[1] * result + prime[2] * (this.oid == null ? 0 : this.oid.hashCode());
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        } else if (obj == null) {
            return false;
        } else if (this.getClass() != obj.getClass()) {
            return false;
        } else {
            GraphType other = (GraphType)obj;
            return this.oid != null && other.oid != null && this.oid.equals(other.oid);
        }
    }

    public Object clone() throws CloneNotSupportedException {
        return (GraphType)super.clone();
    }
}
