package org.ramer.admin.system.repository.common;

import org.ramer.admin.system.entity.domain.common.Privilege;
import org.ramer.admin.system.repository.BaseRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PrivilegeRepository extends BaseRepository<Privilege, Long> {
  Privilege findTopByExpAndState(final String exp, final int state);
}
