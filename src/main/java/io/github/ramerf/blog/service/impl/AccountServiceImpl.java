package io.github.ramerf.blog.service.impl;

import io.github.ramerf.blog.entity.pojo.AccountPoJo;
import io.github.ramerf.blog.service.AccountService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

/**
 * The type Account service.
 *
 * @author ramer
 */
@Slf4j
@Service
public class AccountServiceImpl implements AccountService {
  @Override
  public Long getLoginId(final String nickName) {
    return getOne(
        query -> query.col(AccountPoJo::getId),
        condition -> condition.eq(AccountPoJo::setNickName, nickName),
        Long.class);
  }
}
