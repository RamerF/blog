package org.ramer.admin.system.service.common;

import org.ramer.admin.system.entity.domain.common.Role;
import org.ramer.admin.system.entity.pojo.common.RolesPoJo;
import org.ramer.admin.system.service.BaseService;
import java.util.List;

public interface RoleService extends BaseService<Role, RolesPoJo> {

  Role create(Role role, List<Long> menuIds, List<Long> privilegeIds);

  Role update(Role roles, List<Long> menuIds, List<Long> privilegeIds);

  List<Role> listByManager(long managerId);

  List<String> listNameByManager(long managerId);

  List<Long> listMenuIds(Role role);
}
