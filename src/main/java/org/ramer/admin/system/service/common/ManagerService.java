package org.ramer.admin.system.service.common;

import org.ramer.admin.system.entity.domain.common.Manager;
import org.ramer.admin.system.entity.pojo.common.ManagerPoJo;
import org.ramer.admin.system.service.BaseService;
import org.ramer.admin.system.service.common.impl.ManagerServiceImpl.*;
import java.util.*;

public interface ManagerService extends BaseService<Manager, ManagerPoJo> {

  Manager save(Manager o, List<Long> roleIds);

  List<Manager> listAfterDate(Date updateTime);

  Manager update(Manager o, List<Long> roleIds);

  Manager getByEmpNo(String empNo);

  /**
   * 更新管理员密码.
   *
   * @return
   *     <pre>
   * 1: 成功<br>
   * 0: 失败<br>
   * -1: 密码不匹配<br>
   * -2: 管理员不存在<br>
   * </pre>
   */
  int updatePassword(Long id, String old, String password);

  ManagerLogin getLoginStatus(String empNo);

  void setLoginStatus(String empNo);

  void setUserLoginMap(String empNo, String uuid);

  /**
   * 用户端自动登录.
   *
   * @param empNo empNo
   * @param uuid 随机串
   */
  String userAutoLogin(String empNo, String uuid);
}
