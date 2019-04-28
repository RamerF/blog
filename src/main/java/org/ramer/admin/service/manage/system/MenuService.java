package org.ramer.admin.service.manage.system;

import java.util.List;
import org.ramer.admin.service.common.BaseService;
import org.ramer.admin.entity.domain.manage.Menu;
import org.ramer.admin.entity.pojo.manage.MenuPoJo;

/** @author ramer */
public interface MenuService extends BaseService<Menu, MenuPoJo> {

  List<Menu> listByManager(final long managerId);

  List<MenuPoJo> listNameByManager(final long managerId);
}
