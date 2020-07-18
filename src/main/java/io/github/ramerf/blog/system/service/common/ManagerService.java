package io.github.ramerf.blog.system.service.common;

import java.util.List;
import io.github.ramerf.blog.system.entity.domain.common.Manager;
import io.github.ramerf.blog.system.entity.pojo.common.ManagerPoJo;
import io.github.ramerf.blog.system.service.BaseService;
import io.github.ramerf.blog.system.service.common.impl.ManagerServiceImpl.ManagerLogin;
import org.springframework.data.domain.Page;

/** @author ramer */
public interface ManagerService extends BaseService<Manager, ManagerPoJo> {

  Manager getByEmpNo(final String empNo);

  /** 获取未分配岗位的管理员列表 */
  Page<Manager> pageDetach(final String criteria, final int page, final int size);

  Page<Manager> pageByOrganize(
      final long organizeId, final String criteria, final int page, final int size);

  List<Manager> listByPost(final long postId);

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

  boolean updatePost(final List<Long> ids, final long organizeId, final long postId);

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
