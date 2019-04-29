package org.ramer.admin.system.repository.common;

import java.util.Date;
import java.util.List;
import org.ramer.admin.system.entity.domain.common.CommonMediaCategory;
import org.ramer.admin.system.repository.BaseRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CommonMediaCategoryRepository extends BaseRepository<CommonMediaCategory, Long> {
  List<CommonMediaCategory> findByCodeAndState(final String code, final int state);

  List<CommonMediaCategory> findByCodeAndUpdateTimeGreaterThan(
      final String code, final Date updateTime);

  List<CommonMediaCategory> findByCodeAndUpdateTimeGreaterThanAndState(
      final String code, final Date updateTime, final int state);
}
