package org.ramer.admin.system.repository.common;

import java.util.Date;
import java.util.List;
import org.ramer.admin.system.entity.domain.common.Manager;
import org.ramer.admin.system.repository.BaseRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ManagerRepository extends BaseRepository<Manager, Long> {
  Manager findByIdAndState(Long id, int state);

  Manager findByEmpNoAndState(String empNo, int state);

  List<Manager> findByUpdateTimeGreaterThan(Date updateTime);
}
