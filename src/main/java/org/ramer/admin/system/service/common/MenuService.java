package org.ramer.admin.system.service.common;

import org.ramer.admin.system.entity.domain.common.Menu;
import org.ramer.admin.system.entity.pojo.common.MenuPoJo;
import org.ramer.admin.system.service.BaseService;
import java.util.List;

/** @author ramer */
public interface MenuService extends BaseService<Menu, MenuPoJo> {

  List<Menu> listByManager(Long managerId);

  List<MenuPoJo> listNameByManager(Long managerId);
}
