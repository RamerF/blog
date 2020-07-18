package io.github.ramerf.blog.system.repository.common;

import io.github.ramerf.blog.system.entity.domain.common.Role;
import io.github.ramerf.blog.system.repository.BaseRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RoleRepository extends BaseRepository<Role, Long> {
  Role findByNameAndIsDelete(final String name, final Boolean isDelete);
}
