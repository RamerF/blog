package org.ramer.admin.system.repository.common;

import org.ramer.admin.system.entity.domain.common.Config;
import org.ramer.admin.system.repository.BaseRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ConfigRepository extends BaseRepository<Config, Long> {
  Config findByCode(String code);

  Config findByCodeAndState(String code, int state);
}
