package io.github.ramerf.blog.system.repository.common;

import java.util.List;
import io.github.ramerf.blog.system.entity.domain.common.Menu;
import io.github.ramerf.blog.system.repository.BaseRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MenuRepository extends BaseRepository<Menu, Long> {
  List<Menu> findByParentIdAndIsDelete(final long parentId, final Boolean isDelete);
}
