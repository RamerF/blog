package io.github.ramerf.blog.system.repository.common;

import io.github.ramerf.blog.system.entity.domain.common.ManageLog;
import io.github.ramerf.blog.system.repository.BaseRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ManageLogRepository extends BaseRepository<ManageLog, Long> {}
