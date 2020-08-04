package io.github.ramerf.blog.system.service.common;

import io.github.ramerf.blog.entity.response.IdNameResponse;
import io.github.ramerf.blog.system.entity.domain.common.Manager;
import io.github.ramerf.blog.system.entity.pojo.common.ManagerPoJo;
import io.github.ramerf.blog.system.service.BaseService;
import io.github.ramerf.blog.system.service.common.impl.ManagerServiceImpl.ManagerLogin;
import java.util.List;
import org.springframework.data.domain.Page;

/**
 * The interface Manager service.
 *
 * @author ramer
 */
public interface ManagerService extends BaseService<Manager, ManagerPoJo> {

  /**
   * Gets id and name.
   *
   * @param ids the ids
   * @return the id and name
   */
  List<IdNameResponse> getIdAndName(List<Long> ids);

  /**
   * Gets by emp no.
   *
   * @param empNo the emp no
   * @return the by emp no
   */
  Manager getByEmpNo(final String empNo);

  /**
   * 获取未分配岗位的管理员列表 @param criteria the criteria
   *
   * @param criteria the criteria
   * @param page the page
   * @param size the size
   * @return the page
   */
  Page<Manager> pageDetach(final String criteria, final int page, final int size);

  /**
   * Page by organize page.
   *
   * @param organizeId the organize id
   * @param criteria the criteria
   * @param page the page
   * @param size the size
   * @return the page
   */
  Page<Manager> pageByOrganize(
      final long organizeId, final String criteria, final int page, final int size);

  /**
   * List by post list.
   *
   * @param postId the post id
   * @return the list
   */
  List<Manager> listByPost(final long postId);

  /**
   * 更新管理员密码.
   *
   * @param id the id
   * @param old the old
   * @param password the password
   * @return
   *     <pre> 1: 成功<br> 0: 失败<br> -1: 密码不匹配<br> -2: 管理员不存在<br> </pre>
   */
  int updatePassword(Long id, String old, String password);

  /**
   * Update post boolean.
   *
   * @param ids the ids
   * @param organizeId the organize id
   * @param postId the post id
   * @return the boolean
   */
  boolean updatePost(final List<Long> ids, final long organizeId, final long postId);

  /**
   * Gets login status.
   *
   * @param empNo the emp no
   * @return the login status
   */
  ManagerLogin getLoginStatus(String empNo);

  /**
   * Sets login status.
   *
   * @param empNo the emp no
   */
  void setLoginStatus(String empNo);

  /**
   * Sets user login map.
   *
   * @param empNo the emp no
   * @param uuid the uuid
   */
  void setUserLoginMap(String empNo, String uuid);

  /**
   * 用户端自动登录.
   *
   * @param empNo empNo
   * @param uuid 随机串
   * @return the string
   */
  String userAutoLogin(String empNo, String uuid);
}
