package org.ramer.admin.system.service.common;

import java.util.List;
import org.ramer.admin.system.entity.domain.common.Privilege;
import org.ramer.admin.system.entity.pojo.common.PrivilegePoJo;
import org.ramer.admin.system.service.BaseService;

/** @author ramer */
public interface PrivilegeService extends BaseService<Privilege, PrivilegePoJo> {

  Privilege getByExp(final String exp);

  List<Privilege> listByManagerId(long personId);

  List<Privilege> listByRoles(Long rolesId);
}
