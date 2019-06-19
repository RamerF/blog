package org.ramer.admin.system.repository.common;

import org.ramer.admin.system.entity.domain.common.Role;
import org.ramer.admin.system.repository.BaseRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RoleRepository extends BaseRepository<Role, Long> {
  Role findByNameAndState(final String name, final int state);
}
