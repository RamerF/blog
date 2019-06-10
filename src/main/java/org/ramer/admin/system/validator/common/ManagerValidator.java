package org.ramer.admin.system.validator.common;

import org.ramer.admin.system.entity.domain.common.Manager;
import org.ramer.admin.system.entity.pojo.common.ManagerPoJo;
import org.ramer.admin.system.entity.request.common.ManagerRequest;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;


/** @author ramer */
@Component
public class ManagerValidator implements Validator {
  @Override
  public boolean supports(final Class<?> clazz) {
    return clazz.isAssignableFrom(Manager.class)
        || clazz.isAssignableFrom(ManagerRequest.class)
        || clazz.isAssignableFrom(ManagerPoJo.class);
  }

  @Override
  public void validate(final Object target, final Errors errors) {
    ManagerRequest manager = (ManagerRequest) target;
    if (manager == null) {
      errors.rejectValue(null, "manager.null", "管理员 不能为空");
    } else {
      // TODO-WARN: 添加管理员校验规则
    }
  }
}
