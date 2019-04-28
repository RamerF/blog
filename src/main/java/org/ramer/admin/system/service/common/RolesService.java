package org.ramer.admin.system.service.common;

import org.ramer.admin.system.entity.domain.common.Roles;
import org.ramer.admin.system.entity.pojo.common.RolesPoJo;
import org.ramer.admin.system.service.BaseService;
import java.util.List;

public interface RolesService extends BaseService<Roles, RolesPoJo> {

  Roles create(Roles role, List<Long> menuIds, List<Long> privilegeIds);

  Roles update(Roles roles, List<Long> menuIds, List<Long> privilegeIds);

  List<Roles> listByManager(long managerId);

  List<String> listNameByManager(long managerId);

  List<Long> listMenuIds(Roles role);
}
