//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

package net.bitnine.agensbrowser.bundle.persistence.outer.model.type;

import com.google.common.primitives.Floats;
import org.json.simple.JSONObject;

public class PropertyType implements Cloneable {
    private String key;
    private Long size;
    private PropertyType.ValueType type;

    public PropertyType(String key) {
        this.key = key;
        this.type = PropertyType.ValueType.NULL;
        this.size = 0L;
    }

    public PropertyType(String key, String type) {
        this.key = key;
        this.type = toValueType(type);
        this.size = 0L;
    }

    public PropertyType(String key, String type, Long size) {
        this.key = key;
        this.type = toValueType(type);
        this.size = size;
    }

    public static final PropertyType.ValueType toValueType(String valType) {
        if (valType.equalsIgnoreCase(PropertyType.ValueType.NUMBER.toString())) {
            return PropertyType.ValueType.NUMBER;
        } else if (valType.equalsIgnoreCase(PropertyType.ValueType.STRING.toString())) {
            return PropertyType.ValueType.STRING;
        } else if (valType.equalsIgnoreCase(PropertyType.ValueType.ARRAY.toString())) {
            return PropertyType.ValueType.ARRAY;
        } else if (valType.equalsIgnoreCase(PropertyType.ValueType.OBJECT.toString())) {
            return PropertyType.ValueType.OBJECT;
        } else {
            return valType.equalsIgnoreCase(PropertyType.ValueType.BOOLEAN.toString()) ? PropertyType.ValueType.BOOLEAN : PropertyType.ValueType.NULL;
        }
    }

    public static final PropertyType.ValueType parseType(String value) {
        if (value != null && value.trim() != "") {
            value = value.trim();
            Float numericVal = Floats.tryParse(value);
            if (numericVal != null) {
                return PropertyType.ValueType.NUMBER;
            } else if (value.startsWith("[") && value.endsWith("]")) {
                return PropertyType.ValueType.ARRAY;
            } else if (value.startsWith("{") && value.endsWith("}")) {
                return PropertyType.ValueType.OBJECT;
            } else if (!value.equalsIgnoreCase("true") && !value.equalsIgnoreCase("false")) {
                return !value.equalsIgnoreCase("null") && !value.equalsIgnoreCase("none") ? PropertyType.ValueType.STRING : PropertyType.ValueType.NULL;
            } else {
                return PropertyType.ValueType.BOOLEAN;
            }
        } else {
            return PropertyType.ValueType.NULL;
        }
    }

    public String getKey() {
        return this.key;
    }

    public Long getSize() {
        return this.size;
    }

    public String getType() {
        return this.type.toString();
    }

    public void setKey(String key) {
        this.key = key;
    }

    public void setSize(Long size) {
        this.size = size;
    }

    public void setType(String type) {
        this.type = toValueType(type);
    }

    public void incrementSize() {
        this.size = this.size + 1L;
    }

    public String toString() {
        return "{\"property_type\": " + this.toJson().toJSONString() + "}";
    }

    public JSONObject toJson() {
        JSONObject json = new JSONObject();
        json.put("key", this.key);
        json.put("type", this.type.toString());
        json.put("size", this.size);
        return json;
    }

    public int hashCode() {
        int[] prime = new int[]{17, 29, 31, 37, 73};
        int result = prime[0];
        result = prime[1] * result + prime[2] * (this.type == null ? 0 : this.type.hashCode()) + prime[3] * (this.key == null ? 0 : this.key.hashCode());
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
            PropertyType other = (PropertyType)obj;
            return this.key != null && other.key != null && this.key.equals(other.key) && this.type != null && other.type != null && this.type.equals(other.type);
        }
    }

    public Object clone() throws CloneNotSupportedException {
        return (PropertyType)super.clone();
    }

    public static enum ValueType {
        NUMBER,
        STRING,
        ARRAY,
        OBJECT,
        BOOLEAN,
        NULL;

        private ValueType() {
        }
    }
}
