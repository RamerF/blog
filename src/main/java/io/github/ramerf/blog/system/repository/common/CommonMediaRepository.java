package io.github.ramerf.blog.system.repository.common;

import java.util.Date;
import java.util.List;
import io.github.ramerf.blog.system.entity.domain.common.CommonMedia;
import io.github.ramerf.blog.system.repository.BaseRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

@Repository
public interface CommonMediaRepository extends BaseRepository<CommonMedia, Long> {
  Page<CommonMedia> findByCodeAndIsDelete(String code, Boolean isDelete, Pageable pageable);

  List<CommonMedia> findByCodeAndUpdateTimeGreaterThan(final String code, final Date updateTime);
}
