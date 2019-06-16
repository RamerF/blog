package org.ramer.admin.system.service.common;

import java.util.List;
import org.ramer.admin.system.entity.domain.common.Menu;
import org.ramer.admin.system.entity.pojo.common.MenuPoJo;
import org.ramer.admin.system.service.BaseService;

/** @author ramer */
public interface MenuService extends BaseService<Menu, MenuPoJo> {
  List<Menu> listByManager(Long managerId);
}
