//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

package net.bitnine.agensbrowser.bundle.persistence.inner.model;

import java.io.Serializable;
import java.sql.Timestamp;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Lob;
import javax.persistence.Table;
import net.bitnine.agensbrowser.bundle.message.ClientDto;
import net.bitnine.agensbrowser.bundle.message.ProjectDto;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

@Entity
@Table(
        name = "AGENS_USER_PROJECTS"
)
public class AgensProject implements Serializable {
    private static final long serialVersionUID = -2244201732018167424L;
    @Id
    @GeneratedValue(
            strategy = GenerationType.IDENTITY
    )
    @Column(
            name = "ID",
            nullable = false
    )
    private Integer id;
    @Column(
            name = "USER_NAME",
            nullable = false
    )
    private String userName;
    @Column(
            name = "USER_IP",
            nullable = false
    )
    private String userIp;
    @Column(
            name = "TITLE",
            nullable = false
    )
    private String title;
    @Column(
            name = "DESCRIPTION"
    )
    private String description;
    @CreationTimestamp
    @Column(
            name = "CREATE_DT",
            insertable = true,
            updatable = false,
            columnDefinition = "TIMESTAMP DEFAULT CURRENT_TIMESTAMP"
    )
    private Timestamp create_dt;
    @UpdateTimestamp
    @Column(
            name = "UPDATE_DT",
            insertable = false,
            updatable = true,
            columnDefinition = "TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP"
    )
    private Timestamp update_dt;
    @Column(
            name = "SQL"
    )
    private String sql;
    @Lob
    @Column(
            name = "GRAPH_JSON",
            nullable = true,
            columnDefinition = "longtext"
    )
    private String graph_json;

    public AgensProject() {
        this.id = null;
    }

    public AgensProject(String userName, String userIp, String title, String description, String sql) {
        this.id = null;
        this.userName = userName;
        this.userIp = userIp;
        this.title = title;
        this.description = description;
        this.sql = sql;
        this.graph_json = "{}";
    }

    public AgensProject(String userName, String userIp, String title, String description, String sql, String graph_json) {
        this.id = null;
        this.userName = userName;
        this.userIp = userIp;
        this.title = title;
        this.description = description;
        this.sql = sql;
        this.graph_json = graph_json;
    }

    public AgensProject(ClientDto client, ProjectDto project) {
        this.id = project.getId();
        this.userName = client.getUserName();
        this.userIp = client.getUserIp();
        this.title = project.getTitle();
        this.description = project.getDescription();
        this.sql = project.getSql();
        this.graph_json = project.getGraph_json();
    }

    public Integer getId() {
        return this.id;
    }

    public String getUserName() {
        return this.userName;
    }

    public String getUserIp() {
        return this.userIp;
    }

    public String getTitle() {
        return this.title;
    }

    public String getDescription() {
        return this.description;
    }

    public Timestamp getCreate_dt() {
        return this.create_dt;
    }

    public Timestamp getUpdate_dt() {
        return this.update_dt;
    }

    public String getSql() {
        return this.sql;
    }

    @Lob
    public String getGraph_json() {
        return this.graph_json;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public void setUserIp(String userIp) {
        this.userIp = userIp;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setCreate_dt(Timestamp create_dt) {
        this.create_dt = create_dt;
    }

    public void setUpdate_dt(Timestamp update_dt) {
        this.update_dt = update_dt;
    }

    public void setSql(String sql) {
        this.sql = sql;
    }

    public void setGraph_json(String graph_json) {
        this.graph_json = graph_json;
    }

    public String toString() {
        return "AgensProject{id=" + this.id + ", userName=" + this.userName + ", userIp=" + this.userIp + ", title='" + this.title + '\'' + ", description='" + this.description + '\'' + ", create_dt=" + this.create_dt + ", update_dt=" + this.update_dt + ", sql='" + this.sql + '\'' + ", graph_json='" + this.graph_json + '\'' + '}';
    }
}
