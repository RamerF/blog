package io.github.ramerf.blog.service;

import io.github.ramerf.blog.entity.pojo.AccountBrowserArticlePoJo;
import io.github.ramerf.wind.core.service.BaseService;
import java.util.List;

/**
 * The interface Account browser article service.
 *
 * @author ramer
 */
public interface AccountBrowserArticleService extends BaseService<AccountBrowserArticlePoJo> {
  /**
   * List by account id list.
   *
   * @param accountId the account id
   * @return the list
   */
  List<Long> listByAccountId(final long accountId);
}
