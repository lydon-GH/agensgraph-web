//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

package net.bitnine.agensbrowser.bundle.persistence.outer.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import net.bitnine.agensbrowser.bundle.persistence.outer.model.type.ColumnType;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

public class Record implements Serializable {
    private static final long serialVersionUID = -5575955989067231552L;
    private List<ColumnType> meta;
    private List<List<Object>> rows;

    public Record() {
        this.meta = new ArrayList();
        this.rows = new ArrayList();
    }

    public Record(List<ColumnType> meta, List<List<Object>> rows) {
        this.meta = meta;
        this.rows = rows;
    }

    public List<ColumnType> getMeta() {
        return this.meta;
    }

    public List<List<Object>> getRows() {
        return this.rows;
    }

    public void setMeta(List<ColumnType> meta) {
        this.meta = meta;
    }

    public void setRows(List<List<Object>> rows) {
        this.rows = rows;
    }

    public String toString() {
        return "{\"record\": " + this.toJson().toJSONString() + "}";
    }

    public JSONObject toJson() {
        JSONObject json = new JSONObject();
        JSONArray arrayMeta = new JSONArray();
        Iterator iter = this.meta.iterator();

        while(iter.hasNext()) {
            arrayMeta.add(((ColumnType)iter.next()).toJson());
        }

        json.put("meta", arrayMeta);
        JSONArray arrayRow = new JSONArray();
        Iterator row = this.rows.iterator();

        while(row.hasNext()) {
            JSONArray arrayCol = new JSONArray();
            Iterator col = ((List)row.next()).iterator();

            while(col.hasNext()) {
                arrayCol.add(col.next());
            }

            arrayRow.add(arrayCol);
        }

        json.put("rows", arrayRow);
        return json;
    }
}
