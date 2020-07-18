package io.github.ramerf.blog.system.service.common;

import java.util.List;
import io.github.ramerf.blog.system.entity.domain.common.Role;
import io.github.ramerf.blog.system.entity.pojo.common.RolePoJo;
import io.github.ramerf.blog.system.service.BaseService;

/** @author ramer */
public interface RoleService extends BaseService<Role, RolePoJo> {
  Role create(Role role, List<Long> menuIds, List<Long> privilegeIds);

  Role getByName(final String name);

  List<Role> listByManager(long managerId);

  List<Long> listMenuIds(Role role);

  Role update(Role roles, List<Long> menuIds, List<Long> privilegeIds);
}
