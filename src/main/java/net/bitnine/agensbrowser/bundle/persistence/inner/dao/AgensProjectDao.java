//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

package net.bitnine.agensbrowser.bundle.persistence.inner.dao;

import java.util.List;
import net.bitnine.agensbrowser.bundle.persistence.inner.model.AgensProject;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface AgensProjectDao extends JpaRepository<AgensProject, Integer> {
    @Query(
            value = "select ID, USER_NAME, USER_IP, TITLE, DESCRIPTION, CREATE_DT, UPDATE_DT, case when length(SQL)>50 then substring(SQL,1,47)||' ..' else SQL end as SQL, null as IMAGE, null as GRAPH_JSON from AGENS_USER_PROJECTS where USER_NAME = ?1 order by ID desc",
            nativeQuery = true
    )
    List<AgensProject> findListByUserName(String var1);

    AgensProject findOneByIdAndUserName(Integer var1, String var2);

    @Override
    AgensProject findOne(Integer var1);

    @Override
    List<AgensProject> findAll(Sort var1);

    List<AgensProject> findByUserName(Sort var1, String var2);

    List<AgensProject> findByUserNameAndUserIp(Sort var1, String var2, String var3);

    @Override
    AgensProject save(AgensProject var1);

    @Override
    AgensProject saveAndFlush(AgensProject var1);

    @Override
    void flush();

    @Override
    void delete(AgensProject var1);
}
