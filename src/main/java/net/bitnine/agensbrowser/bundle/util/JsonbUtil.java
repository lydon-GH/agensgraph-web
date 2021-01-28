//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

package net.bitnine.agensbrowser.bundle.util;

import java.sql.SQLException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import java.util.Map.Entry;
import org.json.simple.JSONObject;
import org.postgresql.util.PSQLException;
import org.postgresql.util.PSQLState;

public class JsonbUtil {
    private JsonbUtil() {
    }

    public static final JSONObject parseJson(String jsonString) throws SQLException {
        jsonString = jsonString.trim();
        if (jsonString.startsWith("{") && jsonString.endsWith("}") && !jsonString.equals("{}")) {
            jsonString = jsonString.replace("\\\\", "");
            jsonString = jsonString.replace("\\\"", "'");
            Jsonb props = new Jsonb();
            props.setValue(jsonString);
            if (!(props.getJsonValue() instanceof JSONObject)) {
                throw new PSQLException("Parsing properties failed", PSQLState.DATA_ERROR);
            } else {
                return (JSONObject)props.getJsonValue();
            }
        } else {
            return new JSONObject();
        }
    }

    public static final Map<String, Object> jsonToMap(JSONObject props) throws SQLException {
        Map<String, Object> map = new HashMap();
        Set<Entry<String, Object>> set = props.entrySet();
        Iterator var3 = set.iterator();

        while(var3.hasNext()) {
            Entry<String, Object> entry = (Entry)var3.next();
            map.put(((String)entry.getKey()).toString(), entry.getValue());
        }

        return map;
    }

    public static final Map<String, Object> parseJsonToMap(String jsonString) throws SQLException {
        JSONObject props = parseJson(jsonString);
        return jsonToMap(props);
    }
}
