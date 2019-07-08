package org.ramer.admin.system.service.common;

import java.util.List;
import org.ramer.admin.system.entity.domain.common.Privilege;
import org.ramer.admin.system.entity.pojo.common.PrivilegePoJo;
import org.ramer.admin.system.exception.CommonException;
import org.ramer.admin.system.service.BaseService;

/** @author ramer */
public interface PrivilegeService extends BaseService<Privilege, PrivilegePoJo> {
  /**
   * 创建给定字符串的权限.
   *
   * @param expPrefix eg: manager,用于权限表达式
   * @param name eg:管理员,用于权限配置显示
   * @return 已创建权限集合
   */
  List<Privilege> create(String expPrefix, String name) throws CommonException;

  Privilege getByExp( final String exp);

  List<Privilege> listByManagerId(long personId);

  List<Privilege> listByRoles(Long rolesId);
}
