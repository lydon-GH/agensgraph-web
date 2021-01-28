//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

package net.bitnine.agensbrowser.bundle.persistence.outer.model.type;

import org.json.simple.JSONObject;

public class ColumnType {
    private String name;
    private Integer index;
    private ColumnType.ValueType type;

    public ColumnType(String name) {
        this.name = name;
        this.type = ColumnType.ValueType.NULL;
        this.index = 0;
    }

    public ColumnType(String name, String type) {
        this.name = name;
        this.type = toValueType(type);
        this.index = 0;
    }

    public ColumnType(String name, ColumnType.ValueType type, Integer index) {
        this.name = name;
        this.type = type;
        this.index = index;
    }

    public static final ColumnType.ValueType toValueType(String valType) {
        if (valType.equalsIgnoreCase(ColumnType.ValueType.NUMBER.toString())) {
            return ColumnType.ValueType.NUMBER;
        } else if (valType.equalsIgnoreCase(ColumnType.ValueType.STRING.toString())) {
            return ColumnType.ValueType.STRING;
        } else if (valType.equalsIgnoreCase(ColumnType.ValueType.ID.toString())) {
            return ColumnType.ValueType.ID;
        } else if (valType.equalsIgnoreCase(ColumnType.ValueType.ARRAY.toString())) {
            return ColumnType.ValueType.ARRAY;
        } else if (valType.equalsIgnoreCase(ColumnType.ValueType.OBJECT.toString())) {
            return ColumnType.ValueType.OBJECT;
        } else if (valType.equalsIgnoreCase(ColumnType.ValueType.NODE.toString())) {
            return ColumnType.ValueType.NODE;
        } else if (valType.equalsIgnoreCase(ColumnType.ValueType.EDGE.toString())) {
            return ColumnType.ValueType.EDGE;
        } else {
            return valType.equalsIgnoreCase(ColumnType.ValueType.GRAPH.toString()) ? ColumnType.ValueType.GRAPH : ColumnType.ValueType.NULL;
        }
    }

    public String getName() {
        return this.name;
    }

    public Integer getIndex() {
        return this.index;
    }

    public ColumnType.ValueType getType() {
        return this.type;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setIndex(Integer size) {
        this.index = this.index;
    }

    public void setType(String type) {
        this.type = toValueType(type);
    }

    public void setType(ColumnType.ValueType type) {
        this.type = type;
    }

    public String toString() {
        return "{\"column_type\": " + this.toJson().toJSONString() + "}";
    }

    public JSONObject toJson() {
        JSONObject json = new JSONObject();
        json.put("name", this.name);
        json.put("index", this.index);
        json.put("type", this.type.toString());
        return json;
    }

    public static enum ValueType {
        NODE,
        EDGE,
        GRAPH,
        NUMBER,
        ID,
        STRING,
        ARRAY,
        OBJECT,
        BOOLEAN,
        NULL;

        private ValueType() {
        }
    }
}
