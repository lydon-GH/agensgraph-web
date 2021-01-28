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
import javax.persistence.Table;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

@Entity
@Table(
        name = "AGENS_USER_LOGS"
)
public class AgensLog implements Serializable {
    private static final long serialVersionUID = 2822601521889829886L;
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
            name = "QUERY",
            nullable = false
    )
    private String query;
    @Column(
            name = "STATE",
            nullable = false
    )
    private String state;
    @Column(
            name = "MESSAGE"
    )
    private String message;
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

    public AgensLog() {
    }

    public AgensLog(String userName, String userIp, String query, String state) {
        this.userName = userName;
        this.userIp = userIp;
        this.query = query;
        this.state = state;
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

    public Timestamp getCreate_dt() {
        return this.create_dt;
    }

    public Timestamp getUpdate_dt() {
        return this.update_dt;
    }

    public String getQuery() {
        return this.query;
    }

    public String getState() {
        return this.state;
    }

    public String getMessage() {
        return this.message;
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

    public void setCreate_dt(Timestamp create_dt) {
        this.create_dt = create_dt;
    }

    public void setUpdate_dt(Timestamp update_dt) {
        this.create_dt = update_dt;
    }

    public void setQuery(String query) {
        this.query = query;
    }

    public void setState(String state) {
        this.state = state;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String toString() {
        return "AgensLog{id=" + this.id + ", userName=" + this.userName + ", userIp=" + this.userIp + ", query='" + this.query + '\'' + ", state='" + this.state + '\'' + ", create_dt=" + this.create_dt + ", update_dt=" + this.update_dt + ", message='" + this.message + '\'' + '}';
    }
}
