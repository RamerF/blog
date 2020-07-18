package io.github.ramerf.blog.system.service.common;

import java.util.List;
import io.github.ramerf.blog.system.entity.domain.common.Menu;
import io.github.ramerf.blog.system.entity.pojo.common.MenuPoJo;
import io.github.ramerf.blog.system.service.BaseService;

/** @author ramer */
public interface MenuService extends BaseService<Menu, MenuPoJo> {
  List<Menu> listByManager(Long managerId);
}
