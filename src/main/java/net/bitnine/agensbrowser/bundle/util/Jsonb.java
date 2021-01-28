//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

package net.bitnine.agensbrowser.bundle.util;

import java.io.Serializable;
import java.sql.SQLException;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.JSONValue;
import org.postgresql.util.PGobject;
import org.postgresql.util.PSQLException;
import org.postgresql.util.PSQLState;

public class Jsonb extends PGobject implements Serializable, Cloneable {
    private Object jsonValue;

    public Jsonb() {
        this.jsonValue = null;
        this.setType("jsonb");
    }

    Jsonb(Object obj) {
        this();
        this.jsonValue = obj;
    }

    public void setValue(String value) throws SQLException {
        Object obj;
        try {
            obj = JSONValue.parseWithException(value);
        } catch (Exception var4) {
            throw new PSQLException("Parsing jsonb failed", PSQLState.DATA_ERROR, var4);
        }

        super.setValue(value);
        this.jsonValue = obj;
    }

    public String getValue() {
        if (this.value == null) {
            this.value = JSONValue.toJSONString(this.jsonValue);
        }

        return this.value;
    }

    public Object getJsonValue() {
        return this.jsonValue;
    }

    private String getString(Object obj) {
        if (obj instanceof String) {
            return (String)obj;
        } else {
            throw new UnsupportedOperationException("Not a string: " + obj);
        }
    }

    private int getInt(Object obj) {
        if (obj instanceof Long) {
            long l = (Long)obj;
            if (l >= -2147483648L && l <= 2147483647L) {
                return (int)l;
            } else {
                throw new IllegalArgumentException("Bad value for type int: " + l);
            }
        } else {
            throw new UnsupportedOperationException("Not an int: " + obj);
        }
    }

    private long getLong(Object obj) {
        if (obj instanceof Long) {
            return (Long)obj;
        } else {
            throw new UnsupportedOperationException("Not a long: " + obj);
        }
    }

    private double getDouble(Object obj) {
        if (obj instanceof Double) {
            return (Double)obj;
        } else {
            throw new UnsupportedOperationException("Not a double: " + obj);
        }
    }

    private boolean getBoolean(Object obj) {
        if (obj instanceof Boolean) {
            return (Boolean)obj;
        } else if (obj instanceof String) {
            return ((String)obj).length() > 0;
        } else if (obj instanceof Long) {
            return (Long)obj != 0L;
        } else if (obj instanceof Double) {
            return (Double)obj != 0.0D;
        } else if (obj instanceof JSONArray) {
            return ((JSONArray)obj).size() > 0;
        } else if (obj instanceof JSONObject) {
            return ((JSONObject)obj).size() > 0;
        } else {
            return false;
        }
    }

    private Jsonb getArray(Object obj) {
        if (obj instanceof JSONArray) {
            return new Jsonb(obj);
        } else {
            throw new UnsupportedOperationException("Not an array: " + obj);
        }
    }

    private Jsonb getObject(Object obj) {
        if (obj instanceof JSONObject) {
            return new Jsonb(obj);
        } else {
            throw new UnsupportedOperationException("Not an object: " + obj);
        }
    }

    public String tryGetString() {
        return this.jsonValue instanceof String ? (String)this.jsonValue : null;
    }

    public String getString() {
        return this.getString(this.jsonValue);
    }

    public int getInt() {
        return this.getInt(this.jsonValue);
    }

    public long getLong() {
        return this.getLong(this.jsonValue);
    }

    public double getDouble() {
        return this.getDouble(this.jsonValue);
    }

    public boolean getBoolean() {
        return this.getBoolean(this.jsonValue);
    }

    public boolean isNull() {
        return this.jsonValue == null;
    }

    public Jsonb getArray() {
        return this.getArray(this.jsonValue);
    }

    public Jsonb getObject() {
        return this.getObject(this.jsonValue);
    }

    public String getString(int index) {
        if (!(this.jsonValue instanceof JSONArray)) {
            throw new UnsupportedOperationException("Not an array: " + this.jsonValue);
        } else {
            JSONArray a = (JSONArray)this.jsonValue;
            return this.getString(a.get(index));
        }
    }

    public int getInt(int index) {
        if (!(this.jsonValue instanceof JSONArray)) {
            throw new UnsupportedOperationException("Not an array: " + this.jsonValue);
        } else {
            JSONArray a = (JSONArray)this.jsonValue;
            return this.getInt(a.get(index));
        }
    }

    public long getLong(int index) {
        if (!(this.jsonValue instanceof JSONArray)) {
            throw new UnsupportedOperationException("Not an array: " + this.jsonValue);
        } else {
            JSONArray a = (JSONArray)this.jsonValue;
            return this.getLong(a.get(index));
        }
    }

    public double getDouble(int index) {
        if (!(this.jsonValue instanceof JSONArray)) {
            throw new UnsupportedOperationException("Not an array: " + this.jsonValue);
        } else {
            JSONArray a = (JSONArray)this.jsonValue;
            return this.getDouble(a.get(index));
        }
    }

    public boolean getBoolean(int index) {
        if (!(this.jsonValue instanceof JSONArray)) {
            throw new UnsupportedOperationException("Not an array: " + this.jsonValue);
        } else {
            JSONArray a = (JSONArray)this.jsonValue;
            return this.getBoolean(a.get(index));
        }
    }

    public boolean isNull(int index) {
        if (!(this.jsonValue instanceof JSONArray)) {
            throw new UnsupportedOperationException("Not an array: " + this.jsonValue);
        } else {
            JSONArray a = (JSONArray)this.jsonValue;
            return a.get(index) == null;
        }
    }

    public Jsonb getArray(int index) {
        if (!(this.jsonValue instanceof JSONArray)) {
            throw new UnsupportedOperationException("Not an array: " + this.jsonValue);
        } else {
            JSONArray a = (JSONArray)this.jsonValue;
            return this.getArray(a.get(index));
        }
    }

    public Jsonb getObject(int index) {
        if (!(this.jsonValue instanceof JSONArray)) {
            throw new UnsupportedOperationException("Not an array: " + this.jsonValue);
        } else {
            JSONArray a = (JSONArray)this.jsonValue;
            return this.getObject(a.get(index));
        }
    }

    public String getString(String key) {
        if (!(this.jsonValue instanceof JSONObject)) {
            throw new UnsupportedOperationException("Not an object: " + this.jsonValue);
        } else {
            JSONObject o = (JSONObject)this.jsonValue;
            return this.getString(o.get(key));
        }
    }

    public int getInt(String key) {
        if (!(this.jsonValue instanceof JSONObject)) {
            throw new UnsupportedOperationException("Not an object: " + this.jsonValue);
        } else {
            JSONObject o = (JSONObject)this.jsonValue;
            return this.getInt(o.get(key));
        }
    }

    public long getLong(String key) {
        if (!(this.jsonValue instanceof JSONObject)) {
            throw new UnsupportedOperationException("Not an object: " + this.jsonValue);
        } else {
            JSONObject o = (JSONObject)this.jsonValue;
            return this.getLong(o.get(key));
        }
    }

    public double getDouble(String key) {
        if (!(this.jsonValue instanceof JSONObject)) {
            throw new UnsupportedOperationException("Not an object: " + this.jsonValue);
        } else {
            JSONObject o = (JSONObject)this.jsonValue;
            return this.getDouble(o.get(key));
        }
    }

    public boolean getBoolean(String key) {
        if (!(this.jsonValue instanceof JSONObject)) {
            throw new UnsupportedOperationException("Not an object: " + this.jsonValue);
        } else {
            JSONObject o = (JSONObject)this.jsonValue;
            return this.getBoolean(o.get(key));
        }
    }

    public boolean isNull(String key) {
        if (!(this.jsonValue instanceof JSONObject)) {
            throw new UnsupportedOperationException("Not an object: " + this.jsonValue);
        } else {
            JSONObject o = (JSONObject)this.jsonValue;
            return o.get(key) == null;
        }
    }

    public Jsonb getArray(String key) {
        if (!(this.jsonValue instanceof JSONObject)) {
            throw new UnsupportedOperationException("Not an object: " + this.jsonValue);
        } else {
            JSONObject o = (JSONObject)this.jsonValue;
            return this.getArray(o.get(key));
        }
    }

    public Jsonb getObject(String key) {
        if (!(this.jsonValue instanceof JSONObject)) {
            throw new UnsupportedOperationException("Not an object: " + this.jsonValue);
        } else {
            JSONObject o = (JSONObject)this.jsonValue;
            return this.getObject(o.get(key));
        }
    }

    public int size() {
        if (this.jsonValue instanceof JSONArray) {
            return ((JSONArray)this.jsonValue).size();
        } else if (this.jsonValue instanceof JSONObject) {
            return ((JSONObject)this.jsonValue).size();
        } else {
            throw new UnsupportedOperationException("Not an array or an object: " + this.jsonValue);
        }
    }

    public String toString() {
        return this.getValue();
    }
}
