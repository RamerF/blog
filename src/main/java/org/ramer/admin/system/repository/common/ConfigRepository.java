package org.ramer.admin.system.repository.common;

import org.ramer.admin.system.entity.domain.common.Config;
import org.ramer.admin.system.repository.BaseRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ConfigRepository extends BaseRepository<Config, Long> {
  Config findByCodeAndState(final String code, int state);
}
