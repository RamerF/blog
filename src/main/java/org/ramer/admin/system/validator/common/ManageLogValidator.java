package org.ramer.admin.system.validator.common;

import org.ramer.admin.system.entity.domain.common.ManageLog;
import org.ramer.admin.system.entity.pojo.common.ManageLogPoJo;
import org.ramer.admin.system.entity.request.common.ManageLogRequest;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;


/** @author ramer */
@Component
public class ManageLogValidator implements Validator {
  @Override
  public boolean supports(final Class<?> clazz) {
    return clazz.isAssignableFrom(ManageLog.class) || clazz.isAssignableFrom(ManageLogRequest.class) || clazz.isAssignableFrom(ManageLogPoJo.class);
  }

  @Override
  public void validate(final Object target, final Errors errors) {
    // TODO-WARN: 添加管理端日志校验规则
  }
}
