package org.ramer.admin.system.repository.common;

import java.util.Date;
import java.util.List;
import org.ramer.admin.system.entity.domain.common.CommonMedia;
import org.ramer.admin.system.repository.BaseRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

@Repository
public interface CommonMediaRepository extends BaseRepository<CommonMedia, Long> {
  Page<CommonMedia> findByCodeAndState(String code, int state, Pageable pageable);

  List<CommonMedia> findByCodeAndUpdateTimeGreaterThan(final String code, final Date updateTime);
}
