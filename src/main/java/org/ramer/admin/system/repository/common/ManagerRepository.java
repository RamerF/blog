package org.ramer.admin.system.repository.common;

import java.time.LocalDateTime;
import java.util.List;
import org.ramer.admin.system.entity.domain.common.Manager;
import org.ramer.admin.system.repository.BaseRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ManagerRepository extends BaseRepository<Manager, Long> {

  Manager findByEmpNoAndState(final String empNo, final int state);

  List<Manager> findByUpdateTimeGreaterThanAndState(
      final LocalDateTime updateTime, final int state);
}
