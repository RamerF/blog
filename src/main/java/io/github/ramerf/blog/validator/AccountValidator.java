package io.github.ramerf.blog.validator;

import io.github.ramerf.blog.entity.domain.Account;
import io.github.ramerf.blog.entity.pojo.AccountPoJo;
import io.github.ramerf.blog.entity.request.AccountRequest;
import javax.annotation.Nonnull;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

/** @author ramer */
@Component
public class AccountValidator implements Validator {
  @Override
  public boolean supports(final Class<?> clazz) {
    return clazz.isAssignableFrom(Account.class)
        || clazz.isAssignableFrom(AccountRequest.class)
        || clazz.isAssignableFrom(AccountPoJo.class);
  }

  @Override
  public void validate(final Object target, @Nonnull final Errors errors) {
    AccountRequest account = (AccountRequest) target;
    if (account == null) {
      errors.rejectValue(null, "account.null", "账户 不能为空");
    } else {
      // TODO-WARN: 添加账户校验规则
    }
  }
}
