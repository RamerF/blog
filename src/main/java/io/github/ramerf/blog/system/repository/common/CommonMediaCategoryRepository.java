package io.github.ramerf.blog.system.repository.common;

import java.util.Date;
import java.util.List;
import io.github.ramerf.blog.system.entity.domain.common.CommonMediaCategory;
import io.github.ramerf.blog.system.repository.BaseRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CommonMediaCategoryRepository extends BaseRepository<CommonMediaCategory, Long> {
  List<CommonMediaCategory> findByCodeAndIsDelete(final String code, final Boolean isDelete);

  List<CommonMediaCategory> findByCodeAndUpdateTimeGreaterThan(
      final String code, final Date updateTime);

  List<CommonMediaCategory> findByCodeAndUpdateTimeGreaterThanAndIsDelete(
      final String code, final Date updateTime, final Boolean isDelete);
}
