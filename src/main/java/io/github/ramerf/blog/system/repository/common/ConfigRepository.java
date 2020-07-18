package io.github.ramerf.blog.system.repository.common;

import io.github.ramerf.blog.system.entity.domain.common.Config;
import io.github.ramerf.blog.system.repository.BaseRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ConfigRepository extends BaseRepository<Config, Long> {
  Config findByCodeAndIsDelete(final String code, Boolean isDelete);
}
