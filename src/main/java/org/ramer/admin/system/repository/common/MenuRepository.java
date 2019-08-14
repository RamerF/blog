package org.ramer.admin.system.repository.common;

import java.util.List;
import org.ramer.admin.system.entity.domain.common.Menu;
import org.ramer.admin.system.repository.BaseRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MenuRepository extends BaseRepository<Menu, Long> {
  List<Menu> findByParentIdAndState(final long parentId, final int state);
}
