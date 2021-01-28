//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

package net.bitnine.agensbrowser.bundle.storage;

import com.google.common.collect.Lists;
import com.google.common.collect.Sets;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import net.bitnine.agensbrowser.bundle.message.LabelDto;
import net.bitnine.agensbrowser.bundle.message.MetaDto;
import net.bitnine.agensbrowser.bundle.message.RequestDto;
import net.bitnine.agensbrowser.bundle.message.ResponseDto.StateType;
import net.bitnine.agensbrowser.bundle.persistence.outer.model.Edge;
import net.bitnine.agensbrowser.bundle.persistence.outer.model.Graph;
import net.bitnine.agensbrowser.bundle.persistence.outer.model.Node;
import net.bitnine.agensbrowser.bundle.persistence.outer.model.type.GraphType;
import net.bitnine.agensbrowser.bundle.persistence.outer.model.type.LabelType;
import net.bitnine.agensbrowser.bundle.persistence.outer.model.type.LabelType.ElemType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

@Component
@Scope("singleton")
public class MetaStorage {
    private static final Logger logger = LoggerFactory.getLogger(MetaStorage.class);
    private Long txSeq = -1L;
    private String progressMessage = "";
    private Boolean isDirty;
    private Boolean isDirtyCopy;
    private float version = 1.3F;
    private GraphType graph;
    private Set<LabelType> labels = new HashSet();
    private Graph meta;
    private GraphType graphCopy;
    private Set<LabelType> labelsCopy;
    private Graph metaCopy;

    public MetaStorage() {
        this.clear();
        this.isDirtyCopy = true;
        this.graphCopy = new GraphType();
        this.labelsCopy = new HashSet();
        this.metaCopy = new Graph();
    }

    public void clear() {
        this.isDirty = true;
        this.graph = new GraphType();
        if (this.labels != null) {
            this.labels.clear();
        }

        this.meta = new Graph();
    }

    public float getVersion() {
        return this.version;
    }

    public void setVersion(float version) {
        this.version = version;
    }

    public boolean storageCopy(Boolean forced) {
        if (this.isDirty && !forced) {
            return false;
        } else {
            boolean copied = true;

            try {
                this.graphCopy = (GraphType)this.graph.clone();
            } catch (CloneNotSupportedException var6) {
                System.out.println("storageCopy: FAIL<GraphType> => " + var6.getCause());
                this.graphCopy = new GraphType();
                copied = false;
            }

            try {
                this.labelsCopy.clear();
                Iterator iter = this.labels.iterator();

                while(iter.hasNext()) {
                    LabelType clone = (LabelType)((LabelType)iter.next()).clone();
                    this.labelsCopy.add(clone);
                }
            } catch (CloneNotSupportedException var7) {
                System.out.println("storageCopy: FAIL<LabelTypes> => " + var7.getCause());
                this.labelsCopy = new HashSet();
                copied = false;
            }

            try {
                this.metaCopy = (Graph)this.meta.clone();
            } catch (CloneNotSupportedException var5) {
                System.out.println("storageCopy: FAIL<MetaGraph> => " + var5.getCause());
                this.metaCopy = new Graph();
                copied = false;
            }

            return copied;
        }
    }

    public Long getTxSeq() {
        return this.txSeq;
    }

    public String getProgressMessage() {
        return this.progressMessage;
    }

    public Boolean getIsDirty() {
        return this.isDirty;
    }

    public GraphType getGraphType() {
        return this.graph;
    }

    public List<LabelType> getLabelTypes() {
        return Lists.newArrayList(this.labels);
    }

    public Graph getMetaGraph() {
        return this.meta;
    }

    public Boolean getIsDirtyCopy() {
        return this.isDirtyCopy;
    }

    public GraphType getGraphTypeCopy() {
        return this.graphCopy;
    }

    public List<LabelType> getLabelTypesCopy() {
        return Lists.newArrayList(this.labelsCopy);
    }

    public Graph getMetaGraphCopy() {
        return this.metaCopy;
    }

    public int getNodesSize() {
        int size = 0;
        Iterator iter = this.labels.iterator();

        while(iter.hasNext()) {
            LabelType labelType = (LabelType)iter.next();
            if (labelType.getType().equals(ElemType.NODE)) {
                ++size;
            }
        }

        return size;
    }

    public int getEdgesSize() {
        int size = 0;
        Iterator iter = this.labels.iterator();

        while(iter.hasNext()) {
            LabelType labelType = (LabelType)iter.next();
            if (labelType.getType().equals(ElemType.EDGE)) {
                ++size;
            }
        }

        return size;
    }

    public int getNodesCopySize() {
        int size = 0;
        Iterator iter = this.labelsCopy.iterator();

        while(iter.hasNext()) {
            LabelType labelType = (LabelType)iter.next();
            if (labelType.getType().equals(ElemType.NODE)) {
                ++size;
            }
        }

        return size;
    }

    public int getEdgesCopySize() {
        int size = 0;
        Iterator iter = this.labelsCopy.iterator();

        while(iter.hasNext()) {
            LabelType labelType = (LabelType)iter.next();
            if (labelType.getType().equals(ElemType.EDGE)) {
                ++size;
            }
        }

        return size;
    }

    public LabelType findLabelType(String oid) {
        Iterator iter = this.labels.iterator();

        LabelType labelType;
        do {
            if (!iter.hasNext()) {
                return null;
            }

            labelType = (LabelType)iter.next();
        } while(!labelType.getOid().equals(oid));

        return labelType;
    }

    public LabelType findLabelType(ElemType type, String name) {
        Iterator iter = this.labels.iterator();

        LabelType labelType;
        do {
            if (!iter.hasNext()) {
                return null;
            }

            labelType = (LabelType)iter.next();
        } while(!labelType.getType().equals(type) || !labelType.getName().equals(name));

        return labelType;
    }

    public LabelType findLabelTypeCopy(String oid) {
        Iterator iter = this.labelsCopy.iterator();

        LabelType labelType;
        do {
            if (!iter.hasNext()) {
                return null;
            }

            labelType = (LabelType)iter.next();
        } while(!labelType.getOid().equals(oid));

        return labelType;
    }

    public LabelType findLabelTypeCopy(ElemType type, String name) {
        Iterator iter = this.labelsCopy.iterator();

        LabelType labelType;
        do {
            if (!iter.hasNext()) {
                return null;
            }

            labelType = (LabelType)iter.next();
        } while(!labelType.getType().equals(type) || !labelType.getName().equals(name));

        return labelType;
    }

    public void setTxSeq(Long txSeq) {
        this.txSeq = txSeq;
    }

    public void setProgressMessage(String message) {
        this.progressMessage = message;
    }

    public void setIsDirty(Boolean isDirty) {
        this.isDirty = isDirty;
    }

    public void setGraphType(GraphType graph) {
        this.graph = graph;
        this.graph.setVersion(this.version);
    }

    public void setLabelTypes(List<LabelType> labels) {
        this.labels = Sets.newHashSet(labels);
    }

    public void setMetaGraph(Graph meta) {
        this.meta = meta;
    }

    public void setIsDirtyCopy(Boolean isDirty) {
        this.isDirtyCopy = isDirty;
    }

    public void addLabelTypeCopy(LabelType label) {
        if (!this.labelsCopy.add(label)) {
            this.labelsCopy.remove(label);
            this.labelsCopy.add(label);
        }

        if (label.getType().equals(ElemType.NODE)) {
            this.metaCopy.getNodes().add(new Node(label));
        }

    }

    public void removeLabelTypeCopy(LabelType label) {
        this.labelsCopy.remove(label);
        Iterator iter;
        Edge edge;
        if (label.getType().equals(ElemType.NODE)) {
            iter = this.metaCopy.getNodes().iterator();

            while(iter.hasNext()) {
                Node node = (Node)iter.next();
                if (node.getId().equals(label.getOid())) {
                    iter.remove();
                }
            }

            iter = this.metaCopy.getEdges().iterator();

            while(true) {
                do {
                    if (!iter.hasNext()) {
                        return;
                    }

                    edge = (Edge)iter.next();
                } while(!edge.getSource().equals(label.getOid()) && !edge.getTarget().equals(label.getOid()));

                iter.remove();
            }
        } else {
            iter = this.metaCopy.getEdges().iterator();

            while(iter.hasNext()) {
                edge = (Edge)iter.next();
                if (edge.getId().equals(label.getOid())) {
                    iter.remove();
                }
            }

        }
    }

    public LabelDto getLabelDto(RequestDto req) {
        ElemType type = ElemType.NODE;
        if (req.getCommand().equals("elabel")) {
            type = ElemType.EDGE;
        }

        LabelType label = this.findLabelType(type, req.getTarget());
        return new LabelDto(req, label);
    }

    public LabelDto getLabelDtoCopy(RequestDto req) {
        ElemType type = ElemType.NODE;
        if (req.getCommand().equals("elabel")) {
            type = ElemType.EDGE;
        }

        LabelType label = this.findLabelTypeCopy(type, req.getTarget());
        return new LabelDto(req, label);
    }

    public MetaDto getMetaDto() {
        int nodes_size = this.getNodesSize();
        int edges_size = this.getEdgesSize();
        String message = String.format("%s, labels.size=%d (%d/%d), relations=%d, isDirty=%b", this.graph.getName(), this.labels.size(), nodes_size, edges_size, this.meta.getEdges().size(), this.isDirty);
        MetaDto dto = new MetaDto(this.isDirty, this.graph, this.getLabelTypes(), this.meta);
        dto.setMessage(message);
        if (this.txSeq < 0L) {
            dto.setState(StateType.PENDING);
            dto.setMessage(this.progressMessage);
        } else {
            dto.setState(StateType.SUCCESS);
        }

        return dto;
    }

    public MetaDto getMetaDtoCopy() {
        int nodes_size = this.getNodesCopySize();
        int edges_size = this.getEdgesCopySize();
        String message = String.format("%s, labels.size=%d (%d/%d), relations=%d, isDirty=%b", this.graphCopy.getName(), this.labelsCopy.size(), nodes_size, edges_size, this.metaCopy.getEdges().size(), this.isDirtyCopy);
        MetaDto dto = new MetaDto(this.isDirtyCopy, this.graphCopy, this.getLabelTypesCopy(), this.metaCopy);
        dto.setMessage(message);
        if (this.txSeq < 0L) {
            dto.setState(StateType.PENDING);
            dto.setMessage(this.progressMessage);
        } else {
            dto.setState(StateType.SUCCESS);
        }

        return dto;
    }
}
