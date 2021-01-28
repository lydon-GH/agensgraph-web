//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

package net.bitnine.agensbrowser.bundle.persistence.outer.service;

import java.sql.SQLException;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import net.bitnine.agensbrowser.bundle.message.RequestDto;
import net.bitnine.agensbrowser.bundle.persistence.outer.dao.MetaDao;
import net.bitnine.agensbrowser.bundle.persistence.outer.model.Edge;
import net.bitnine.agensbrowser.bundle.persistence.outer.model.Graph;
import net.bitnine.agensbrowser.bundle.persistence.outer.model.Node;
import net.bitnine.agensbrowser.bundle.persistence.outer.model.type.GraphType;
import net.bitnine.agensbrowser.bundle.persistence.outer.model.type.LabelType;
import net.bitnine.agensbrowser.bundle.persistence.outer.model.type.LabelType.ElemType;
import net.bitnine.agensbrowser.bundle.storage.MetaStorage;
import org.postgresql.util.PSQLException;
import org.postgresql.util.PSQLState;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class MetaService {
    private MetaDao dao;
    private MetaStorage storage;

    @Autowired
    public MetaService(MetaDao dao, MetaStorage storage) {
        this.dao = dao;
        this.storage = storage;
    }

    public MetaStorage getStorage() {
        return this.storage;
    }

    public String checkAGVersion() {
        float version = this.dao.checkAGVersion();
        this.storage.setVersion(version);
        return version >= 1.3F ? "v1.3 or over" : "v1.2 or under";
    }

    public int loadLabelCounts(List<LabelType> labels) throws SQLException {
        int countOfValue = 0;
        Iterator iter = labels.iterator();

        while(iter.hasNext()) {
            LabelType label = (LabelType)iter.next();
            Long[] var10000 = new Long[]{0L, 0L};

            Long[] counts;
            try {
                counts = this.dao.getLabelCount(label.getName());
            } catch (SQLException var7) {
                throw new PSQLException("loadLabelCounts() failed.", PSQLState.CONNECTION_FAILURE, var7);
            }

            if (counts.length > 0 && counts[0] > 0L) {
                label.setSize(counts[0]);
                ++countOfValue;
            }

            if (counts.length > 1 && counts[1] > 0L) {
                label.setSizeNotEmpty(counts[1]);
            }

            if (label.getSizeNotEmpty().equals(0L)) {
                label.setIsDirty(false);
            }
        }

        return countOfValue;
    }

    public int loadLabelProperties(List<LabelType> labels) throws SQLException {
        int countWithProperties = 0;
        Iterator iter = labels.iterator();

        while(true) {
            while(true) {
                LabelType label;
                do {
                    if (!iter.hasNext()) {
                        return countWithProperties;
                    }

                    label = (LabelType)iter.next();
                } while(label.getSizeNotEmpty().equals(0L));

                ++countWithProperties;
                LabelType prev = this.storage.findLabelType(label.getOid());
                if (prev != null && prev.getSizeNotEmpty().equals(label.getSizeNotEmpty()) && !prev.getIsDirty()) {
                    label.setProperties(prev.getProperties());
                    label.setIsDirty(false);
                } else {
                    List properties = null;

                    try {
                        properties = this.dao.getLabelProperties(label.getName());
                    } catch (SQLException var8) {
                        throw new PSQLException("loadLabelProperties() failed.", PSQLState.CONNECTION_FAILURE, var8);
                    }

                    if (properties != null) {
                        label.setProperties(properties);
                        label.setIsDirty(false);
                    } else {
                        if (prev != null) {
                            label.setProperties(prev.getProperties());
                        }

                        label.setIsDirty(true);
                    }

                    System.out.println(String.format(" + %s: label.size=%d, properties.size=%d, isDirty=%b", label.getName(), label.getSize(), label.getProperties().size(), label.getIsDirty()));
                }
            }
        }
    }

    public Boolean checkLabelsDirty(List<LabelType> labels) {
        Boolean totalDirty = false;

        LabelType label;
        for(Iterator iter = labels.iterator(); iter.hasNext(); totalDirty = totalDirty | label.getIsDirty()) {
            label = (LabelType)iter.next();
        }

        return totalDirty;
    }

    public int loadMetaGraph(Graph meta, List<LabelType> labels) throws SQLException {
        Set<LabelType> metaLabels = meta.getMeta();
        metaLabels.clear();
        Set<Node> nodes = meta.getNodes();
        nodes.clear();
        Set<Edge> edges = meta.getEdges();
        edges.clear();
        int countOfVertex = 0;
        int countOfVertexWithData = 0;
        int countOfRelation = 0;
        int countOfRelationWithData = 0;
        Iterator iter = labels.iterator();

        while(true) {
            LabelType label;
            while(iter.hasNext()) {
                label = (LabelType)iter.next();
                if (label.getType().equals(ElemType.NODE)) {
                    nodes.add(new Node(label));
                    ++countOfVertex;
                    if (!label.getSize().equals(0L)) {
                        ++countOfVertexWithData;
                    }
                } else if (label.getType().equals(ElemType.EDGE)) {
                    try {
                        List<Long[]> relations = this.dao.getRelationCount(label.getName());
                        Iterator rel = relations.iterator();

                        while(rel.hasNext()) {
                            Long[] record = (Long[])rel.next();
                            if (record.length == 4 && String.valueOf(record[0]).equals(label.getOid()) && !record[1].equals(0L) && !record[2].equals(0L)) {
                                Edge edge = new Edge(label, String.valueOf(record[1]), String.valueOf(record[2]));
                                edge.setSize(record[3]);
                                LabelType sourceLabel = this.storage.findLabelType(String.valueOf(record[1]));
                                LabelType targetLabel = this.storage.findLabelType(String.valueOf(record[2]));
                                if (sourceLabel != null && targetLabel != null) {
                                    edges.add(edge);
                                    label.getNeighbors().add(sourceLabel.getName());
                                    if (targetLabel != null) {
                                        sourceLabel.getNeighbors().add(targetLabel.getName());
                                    }

                                    label.getNeighbors().add(targetLabel.getName());
                                    if (sourceLabel != null) {
                                        targetLabel.getNeighbors().add(sourceLabel.getName());
                                    }
                                }
                            }
                        }
                    } catch (SQLException var18) {
                        throw new PSQLException("loadMetaGraph() failed.", PSQLState.CONNECTION_FAILURE, var18);
                    }

                    ++countOfRelation;
                    if (!label.getSize().equals(0L)) {
                        ++countOfRelationWithData;
                    }
                }
            }

            LabelType metaNodeType = new LabelType("NODE", "NODE");
            metaNodeType.setSize(new Long((long)countOfVertex));
            metaNodeType.setSizeNotEmpty(new Long((long)countOfVertexWithData));
            metaLabels.add(metaNodeType);
            label = new LabelType("EDGE", "EDGE");
            label.setSize(new Long((long)countOfRelation));
            label.setSizeNotEmpty(new Long((long)countOfRelationWithData));
            metaLabels.add(label);
            return countOfRelation;
        }
    }

    public void reloadMeta(Long txSeq) {
        this.storage.setProgressMessage(String.format("reload[%03d]: Just begining. read DB info... 3", txSeq) + "%");
        GraphType graph = this.dao.getGraphType();
        if (graph != null) {
            this.storage.setGraphType(graph);
        }

        Boolean totalDirty = this.storage.getGraphType().getIsDirty();
        int countOfValue = 0;
        int countWithProperties = 0;
        int countOfRelation = -1;

        try {
            this.storage.setProgressMessage(String.format("reload[%03d]: read Labels LIST.. 11", txSeq) + "%");
            List<LabelType> labels = this.dao.getLabelTypes();
            if (labels != null) {
                if (this.storage.getLabelTypes().size() == 0) {
                    this.storage.setLabelTypes(labels);
                }

                this.storage.setProgressMessage(String.format("reload[%03d]: read counts of Labels LIST(%d).. 29", txSeq, labels.size()) + "%");
                countOfValue = this.loadLabelCounts(labels);
                this.storage.setProgressMessage(String.format("reload[%03d]: HARD TIME! read properties of each Labels(%d).. 57", txSeq, countOfValue) + "%");
                countWithProperties = this.loadLabelProperties(labels);
                totalDirty = totalDirty | this.checkLabelsDirty(labels);
                this.storage.setLabelTypes(labels);
                this.storage.setProgressMessage(String.format("reload[%03d]: Almost done. read relations and make MetaGraph(%d).. 78", txSeq, countWithProperties) + "%");
                countOfRelation = this.loadMetaGraph(this.storage.getMetaGraph(), labels);
            } else {
                totalDirty = true;
            }

            this.storage.setIsDirty(totalDirty);
        } catch (SQLException var10) {
            System.out.println("ERROR: " + var10.getMessage() + "\n");
            this.storage.setIsDirty(true);
        }

        int nodes_size = this.storage.getNodesSize();
        int edges_size = this.storage.getEdgesSize();
        this.storage.setProgressMessage(String.format("reload[%03d]: Complete! Thank you anyway(%d/%d).. 99", txSeq, nodes_size, edges_size) + "%");
        boolean copied = this.storage.storageCopy(false);
        if (copied) {
            this.storage.setIsDirtyCopy(false);
            this.storage.setTxSeq(txSeq);
        }

        System.out.println(String.format("reload[%03d]: %s, labels=%d (%d/%d), relations=%d, isDirty=%b ==> copy %s", txSeq, this.storage.getGraphType().getName(), this.storage.getLabelTypes().size(), nodes_size, edges_size, countOfRelation, totalDirty, copied ? "OK!" : "pass"));
    }

    public LabelType updateLabelCopy(RequestDto req) {
        this.storage.setIsDirtyCopy(false);
        ElemType type = ElemType.NODE;
        if (req.getCommand().equals("elabel")) {
            type = ElemType.EDGE;
        }

        String name = req.getTarget();
        LabelType label = this.dao.getLabelType(type, name);
        if (label != null) {
            this.storage.addLabelTypeCopy(label);
        }

        return label;
    }
}
