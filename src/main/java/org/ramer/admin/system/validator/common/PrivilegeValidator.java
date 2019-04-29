package org.ramer.admin.system.validator.common;

import org.ramer.admin.system.entity.domain.common.Privilege;
import org.ramer.admin.system.entity.pojo.common.PrivilegePoJo;
import org.ramer.admin.system.entity.request.common.PrivilegeRequest;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;


/** @author ramer */
@Component
public class PrivilegeValidator implements Validator {
  @Override
  public boolean supports(final Class<?> clazz) {
    return clazz.isAssignableFrom(Privilege.class) || clazz.isAssignableFrom(PrivilegeRequest.class) || clazz.isAssignableFrom(PrivilegePoJo.class);
  }

  @Override
  public void validate(final Object target, final Errors errors) {
    // TODO-WARN: 添加权限校验规则
  }
}
