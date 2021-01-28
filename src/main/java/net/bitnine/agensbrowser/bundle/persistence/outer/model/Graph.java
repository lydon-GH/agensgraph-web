//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

package net.bitnine.agensbrowser.bundle.persistence.outer.model;

import java.io.Serializable;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import net.bitnine.agensbrowser.bundle.persistence.outer.model.type.LabelType;
import net.bitnine.agensbrowser.bundle.persistence.outer.model.type.LabelType.ElemType;
import net.bitnine.agensbrowser.bundle.storage.MetaStorage;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.postgresql.util.PSQLException;
import org.postgresql.util.PSQLState;

public class Graph implements Serializable, Cloneable {
    private static final long serialVersionUID = 603092706295629454L;
    private Set<LabelType> meta;
    private Set<Node> nodes;
    private Set<Edge> edges;

    public Graph() {
        this.meta = new HashSet();
        this.nodes = new HashSet();
        this.edges = new HashSet();
    }

    public Graph(Set<Node> nodes, Set<Edge> edges) {
        this.nodes = nodes;
        this.edges = edges;
    }

    public Graph(Set<LabelType> meta, Set<Node> nodes, Set<Edge> edges) {
        this.meta = meta;
        this.nodes = nodes;
        this.edges = edges;
    }

    public final void updateMeta(MetaStorage storage) {
        Map<String, LabelType> map = new HashMap();
        Iterator iter;
        LabelType label;
        LabelType clone;
        if (this.nodes != null && this.nodes.size() > 0) {
            iter = this.nodes.iterator();

            while(iter.hasNext()) {
                Node node = (Node)iter.next();
                if (map.containsKey(node.getLabel())) {
                    label = (LabelType)map.get(node.getLabel());
                    label.incrementSize();
                    if (node.getProps().size() > 0) {
                        label.incrementSizeNotEmpty();
                    }

                    label.incrementSizeProperties(node.getProps());
                } else {
                    label = storage.findLabelTypeCopy(ElemType.NODE, node.getLabel());
                    if (label != null) {
                        try {
                            clone = (LabelType)label.clone();
                            clone.setSize(1L);
                            if (node.getProps().size() > 0) {
                                clone.setSizeNotEmpty(1L);
                            }

                            clone.resetSizeProperties(0);
                            clone.incrementSizeProperties(node.getProps());
                            map.put(node.getLabel(), clone);
                        } catch (CloneNotSupportedException var8) {
                            System.out.println("updateMeta(NODE): CloneNotSupportedException->" + var8.getMessage() + "\n");
                        }
                    } else {
                        label = node.getLabelType();
                        map.put(label.getName(), label);
                    }
                }
            }
        }

        if (this.edges != null && this.edges.size() > 0) {
            iter = this.edges.iterator();

            while(iter.hasNext()) {
                Edge edge = (Edge)iter.next();
                if (map.containsKey(edge.getLabel())) {
                    ((LabelType)map.get(edge.getLabel())).setSize(((LabelType)map.get(edge.getLabel())).getSize() + 1L);
                    if (edge.getProps().size() > 0) {
                        ((LabelType)map.get(edge.getLabel())).setSizeNotEmpty(((LabelType)map.get(edge.getLabel())).getSizeNotEmpty() + 1L);
                    }
                } else {
                    label = storage.findLabelTypeCopy(ElemType.EDGE, edge.getLabel());
                    if (label != null) {
                        try {
                            clone = (LabelType)label.clone();
                            clone.setSize(1L);
                            if (edge.getProps().size() > 0) {
                                clone.setSizeNotEmpty(1L);
                            }

                            map.put(edge.getLabel(), clone);
                        } catch (CloneNotSupportedException var7) {
                            System.out.println("updateMeta(EDGE): CloneNotSupportedException->" + var7.getMessage() + "\n");
                        }
                    } else {
                        label = edge.getLabelType();
                        map.put(label.getName(), label);
                    }
                }
            }
        }

        this.meta = new HashSet(map.values());
    }

    public Set<LabelType> getMeta() {
        return this.meta;
    }

    public Set<Node> getNodes() {
        return this.nodes;
    }

    public Set<Edge> getEdges() {
        return this.edges;
    }

    public void setMeta(Set<LabelType> meta) {
        this.meta = meta;
    }

    public void setNodes(Set<Node> nodes) {
        this.nodes = nodes;
    }

    public void setEdges(Set<Edge> edges) {
        this.edges = edges;
    }

    public String toString() {
        return "{\"graph\": " + this.toJson().toString() + "}";
    }

    public JSONObject toJson() {
        JSONObject json = new JSONObject();
        JSONArray jsonArray = new JSONArray();
        Iterator iter = this.meta.iterator();

        while(iter.hasNext()) {
            jsonArray.add(((LabelType)iter.next()).toJson());
        }

        json.put("meta", jsonArray);
        jsonArray = new JSONArray();
        iter = this.nodes.iterator();

        while(iter.hasNext()) {
            jsonArray.add(((Node)iter.next()).toJson());
        }

        json.put("nodes", jsonArray);
        jsonArray = new JSONArray();
        iter = this.edges.iterator();

        while(iter.hasNext()) {
            jsonArray.add(((Edge)iter.next()).toJson());
        }

        json.put("edges", jsonArray);
        return json;
    }

    public void setValue(String value) throws SQLException {
        ArrayList<String> tokens = this.tokenize(value);

        for(int i = 0; i < tokens.size(); ++i) {
            if (i % 2 == 0) {
                Node node = new Node();
                node.setValue((String)tokens.get(i));
                this.nodes.add(node);
            } else {
                Edge edge = new Edge();
                edge.setValue((String)tokens.get(i));
                this.edges.add(edge);
            }
        }

    }

    private ArrayList<String> tokenize(String value) throws SQLException {
        ArrayList<String> tokens = new ArrayList();
        int pos = 1;
        int len = value.length() - 1;
        int start = pos;
        int depth = 0;

        label59:
        for(boolean gid = false; pos < len; ++pos) {
            char c = value.charAt(pos);
            switch(c) {
                case '"':
                    if (depth <= 0) {
                        break;
                    }

                    boolean escape = false;
                    int i = pos + 1;

                    while(true) {
                        if (i >= len) {
                            continue label59;
                        }

                        c = value.charAt(i);
                        if (c == '\\') {
                            escape = !escape;
                        } else if (c == '"') {
                            if (!escape) {
                                pos = i;
                                continue label59;
                            }

                            escape = false;
                        } else {
                            escape = false;
                        }

                        ++i;
                    }
                case ',':
                    if (depth == 0 && !gid) {
                        tokens.add(value.substring(start, pos));
                        start = pos + 1;
                    }
                    break;
                case '[':
                    if (depth == 0) {
                        gid = true;
                    }
                    break;
                case ']':
                    if (depth == 0) {
                        gid = false;
                    }
                    break;
                case '{':
                    ++depth;
                    break;
                case '}':
                    --depth;
                    if (depth < 0) {
                        throw new PSQLException("Parsing GRAPH failed", PSQLState.DATA_ERROR);
                    }
            }
        }

        tokens.add(value.substring(start, pos));
        return tokens;
    }

    public Object clone() throws CloneNotSupportedException {
        Graph graphClone = (Graph)super.clone();
        Set<LabelType> metaClone = new HashSet();
        Iterator iter = this.meta.iterator();

        while(iter.hasNext()) {
            LabelType clone = (LabelType)((LabelType)iter.next()).clone();
            metaClone.add(clone);
        }

        graphClone.setMeta(metaClone);
        Set<Node> nodesClone = new HashSet();
        Iterator iter1 = this.nodes.iterator();

        while(iter1.hasNext()) {
            Node clone = (Node)((Node)iter1.next()).clone();
            nodesClone.add(clone);
        }

        graphClone.setNodes(nodesClone);
        Set<Edge> edgesClone = new HashSet();
        Iterator iter2 = this.edges.iterator();

        while(iter2.hasNext()) {
            Edge clone = (Edge)((Edge)iter2.next()).clone();
            edgesClone.add(clone);
        }

        graphClone.setEdges(edgesClone);
        return graphClone;
    }
}
