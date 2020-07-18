package io.github.ramerf.blog.system.repository.common;

import io.github.ramerf.blog.system.entity.domain.common.Privilege;
import io.github.ramerf.blog.system.repository.BaseRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PrivilegeRepository extends BaseRepository<Privilege, Long> {
  Privilege findTopByExpAndIsDelete(final String exp, final Boolean isDelete);
}
