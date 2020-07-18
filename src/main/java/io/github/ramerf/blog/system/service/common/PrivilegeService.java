package io.github.ramerf.blog.system.service.common;

import java.util.List;
import io.github.ramerf.blog.system.entity.domain.common.Privilege;
import io.github.ramerf.blog.system.entity.pojo.common.PrivilegePoJo;
import io.github.ramerf.blog.system.service.BaseService;

/** @author ramer */
public interface PrivilegeService extends BaseService<Privilege, PrivilegePoJo> {

  Privilege getByExp(final String exp);

  List<Privilege> listByManagerId(long personId);

  List<Privilege> listByRoles(Long rolesId);
}
