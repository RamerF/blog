package io.github.ramerf.blog.service.impl;

import io.github.ramerf.blog.entity.pojo.AccountBrowserArticlePoJo;
import io.github.ramerf.blog.service.AccountBrowserArticleService;
import java.util.List;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

/**
 * The type Account browser article service.
 *
 * @author ramer
 */
@Slf4j
@Service
public class AccountBrowserArticleServiceImpl implements AccountBrowserArticleService {
  @Override
  public List<Long> listByAccountId(final long accountId) {
    return list(
        query -> query.col(AccountBrowserArticlePoJo::getArticleId),
        condition -> condition.eq(AccountBrowserArticlePoJo::setAccountId, accountId),
        Long.class);
  }
}
