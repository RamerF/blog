package io.github.ramerf.blog.system.repository.common;

import java.time.LocalDateTime;
import java.util.List;
import io.github.ramerf.blog.system.entity.domain.common.Manager;
import io.github.ramerf.blog.system.entity.domain.common.Organize;
import io.github.ramerf.blog.system.repository.BaseRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

@Repository
public interface ManagerRepository extends BaseRepository<Manager, Long> {

  Manager findByEmpNoAndIsDelete(final String empNo, final Boolean isDelete);

  List<Manager> findByUpdateTimeGreaterThanAndIsDelete(
      final LocalDateTime updateTime, final Boolean isDelete);

  Page<Manager> findByOrganizeIdInAndIsDelete(
      final List<Long> organizeIds, final Boolean isDelete, final Pageable pageable);

  Page<Manager> findByOrganizeInAndIsDelete(
      final List<Organize> organizes, final Boolean isDelete, final Pageable pageable);

  List<Manager> findByPostIdAndIsDelete(final long postId, final Boolean isDelete);
}
