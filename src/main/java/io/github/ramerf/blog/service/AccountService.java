package io.github.ramerf.blog.service;

import io.github.ramerf.blog.entity.pojo.AccountPoJo;
import io.github.ramerf.wind.core.service.BaseService;

/**
 * The interface Account service.
 *
 * @author ramer
 */
public interface AccountService extends BaseService<AccountPoJo> {
  /**
   * Gets login id.
   *
   * @param nickName the nick name
   * @return the login id
   */
  Long getLoginId(final String nickName);
}
