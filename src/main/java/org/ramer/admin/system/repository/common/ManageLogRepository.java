package org.ramer.admin.system.repository.common;

import org.ramer.admin.system.entity.domain.common.ManageLog;
import org.ramer.admin.system.repository.BaseRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ManageLogRepository extends BaseRepository<ManageLog, Long> {}
