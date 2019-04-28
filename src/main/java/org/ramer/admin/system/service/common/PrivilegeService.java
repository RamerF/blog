package org.ramer.admin.system.service.common;

import org.ramer.admin.system.entity.domain.common.Privilege;
import org.ramer.admin.system.entity.pojo.common.PrivilegePoJo;
import org.ramer.admin.system.exception.CommonException;
import org.ramer.admin.system.service.BaseService;
import java.util.List;

/** @author ramer */
public interface PrivilegeService extends BaseService<Privilege, PrivilegePoJo> {
  /**
   * 创建给定字符串的权限.
   *
   * @param expPrefix eg: config,用于编码权限表达式
   * @param remark eg:系统配置,用于权限配置显示
   * @return 已创建权限集合
   */
  List<Privilege> create(String expPrefix, String remark) throws CommonException;

  List<Privilege> listByManagerId(long personId);

  void saveBatch(List<Privilege> privileges);

  List<Privilege> listByRoles(Long rolesId);
}
