//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

package net.bitnine.agensbrowser.bundle.persistence.inner.dao;

import java.util.List;
import net.bitnine.agensbrowser.bundle.persistence.inner.model.AgensLog;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface AgensLogDao extends JpaRepository<AgensLog, Integer> {
    @Query(
            value = "select ID, USER_NAME, USER_IP, QUERY, STATE, MESSAGE, CREATE_DT, UPDATE_DT from AGENS_USER_LOGS where USER_NAME = ?1 order by ID desc limit 1000",
            nativeQuery = true
    )
    List<AgensLog> findListByUserName(String var1);

    AgensLog findOneByIdAndUserName(Integer var1, String var2);

    AgensLog findOne(Integer var1);

    List<AgensLog> findAll(Sort var1);

    List<AgensLog> findByUserName(Sort var1, String var2);

    List<AgensLog> findByUserNameAndUserIp(Sort var1, String var2, String var3);

    AgensLog save(AgensLog var1);

    AgensLog saveAndFlush(AgensLog var1);

    void flush();

    void delete(AgensLog var1);
}
