package org.ramer.admin.system.service.common;

import java.util.List;
import org.ramer.admin.system.entity.domain.common.Role;
import org.ramer.admin.system.entity.pojo.common.RolePoJo;
import org.ramer.admin.system.service.BaseService;

/** @author ramer */
public interface RoleService extends BaseService<Role, RolePoJo> {
  Role create(Role role, List<Long> menuIds, List<Long> privilegeIds);

  Role getByName(final String name);

  List<Role> listByManager(long managerId);

  List<String> listNameByManager(long managerId);

  List<Long> listMenuIds(Role role);

  Role update(Role roles, List<Long> menuIds, List<Long> privilegeIds);
}
