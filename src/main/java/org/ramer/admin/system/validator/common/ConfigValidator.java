package org.ramer.admin.system.validator.common;

import org.ramer.admin.system.entity.domain.common.Config;
import org.ramer.admin.system.entity.pojo.common.ConfigPoJo;
import org.ramer.admin.system.entity.request.common.ConfigRequest;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;


/** @author ramer */
@Component
public class ConfigValidator implements Validator {
  @Override
  public boolean supports(final Class<?> clazz) {
    return clazz.isAssignableFrom(Config.class) || clazz.isAssignableFrom(ConfigRequest.class) || clazz.isAssignableFrom(ConfigPoJo.class);
  }

  @Override
  public void validate(final Object target, final Errors errors) {
    // TODO-WARN: 添加系统配置校验规则
  }
}
