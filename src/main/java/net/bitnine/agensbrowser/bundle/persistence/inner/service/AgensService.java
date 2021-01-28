//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

package net.bitnine.agensbrowser.bundle.persistence.inner.service;

import java.util.List;
import net.bitnine.agensbrowser.bundle.persistence.inner.dao.AgensLogDao;
import net.bitnine.agensbrowser.bundle.persistence.inner.dao.AgensProjectDao;
import net.bitnine.agensbrowser.bundle.persistence.inner.model.AgensLog;
import net.bitnine.agensbrowser.bundle.persistence.inner.model.AgensProject;
import net.bitnine.agensbrowser.bundle.storage.ClientStorage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class AgensService {
    private static final Logger logger = LoggerFactory.getLogger(AgensService.class);
    private AgensProjectDao projectDao;
    private AgensLogDao logDao;
    private ClientStorage clients;

    @Autowired
    public AgensService(AgensProjectDao projectDao, AgensLogDao logDao, ClientStorage clients) {
        this.projectDao = projectDao;
        this.logDao = logDao;
        this.clients = clients;
    }

    public List<AgensProject> findProjectsAll() {
        List<AgensProject> projects = this.projectDao.findAll(new Sort(Direction.DESC, new String[]{"id"}));
        return projects;
    }

    public List<AgensProject> findProjectsByUserName() {
        List<AgensProject> projects = this.projectDao.findListByUserName(this.clients.getCurrentUserName());
        return projects;
    }

    public AgensProject findOneProjectById(Integer id) {
        AgensProject project = this.projectDao.findOneByIdAndUserName(id, this.clients.getCurrentUserName());
        return project;
    }

    public AgensProject saveProject(AgensProject project) {
        if (project == null) {
            return null;
        } else {
            if (project.getId() != null) {
                ;
            }

            return this.projectDao.saveAndFlush(project);
        }
    }

    public Boolean deleteProject(Integer id) {
        AgensProject project = this.projectDao.findOneByIdAndUserName(id, this.clients.getCurrentUserName());
        if (project != null) {
            this.projectDao.delete(project);
            return true;
        } else {
            return false;
        }
    }

    public List<AgensLog> findLogsAll() {
        List<AgensLog> logs = this.logDao.findAll(new Sort(Direction.DESC, new String[]{"id"}));
        return logs;
    }

    public List<AgensLog> findLogsByUserName() {
        List<AgensLog> logs = this.logDao.findListByUserName(this.clients.getCurrentUserName());
        return logs;
    }

    public AgensLog findOneLogById(Integer id) {
        AgensLog log = this.logDao.findOneByIdAndUserName(id, this.clients.getCurrentUserName());
        return log;
    }

    public AgensLog saveLog(String sql, String state, String message) {
        if (sql != null && state != null) {
            AgensLog log = new AgensLog(this.clients.getCurrentUserName(), "localhost", sql, state);
            log.setMessage(message);
            return this.logDao.saveAndFlush(log);
        } else {
            return null;
        }
    }

    public Boolean deleteLog(Integer id) {
        AgensLog log = this.logDao.findOne(id);
        if (log != null) {
            this.logDao.delete(log);
            return true;
        } else {
            return false;
        }
    }
}
