package org.ramer.admin.system.validator.common;

import org.ramer.admin.system.entity.domain.common.Role;
import org.ramer.admin.system.entity.pojo.common.RolePoJo;
import org.ramer.admin.system.entity.request.common.RoleRequest;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

/** @author ramer */
@Component
public class RoleValidator implements Validator {
  @Override
  public boolean supports(final Class<?> clazz) {
    return clazz.isAssignableFrom(Role.class)
        || clazz.isAssignableFrom(RoleRequest.class)
        || clazz.isAssignableFrom(RolePoJo.class);
  }

  @Override
  public void validate(final Object target, final Errors errors) {
    final Role roles = (Role) target;
    if (roles == null) {
      errors.rejectValue("roles", "roles.null", "参数不能为空");
    } else {
      if (StringUtils.isEmpty(roles.getName()) || roles.getName().length() > 20) {
        errors.rejectValue("name", "roles.name.null", "名称 不能为空且小于20个字符");
      }
      String remark = roles.getRemark();
      if (!StringUtils.isEmpty(remark) && remark.length() > 100) {
        errors.rejectValue("remark", "menu.remark.length", "备注 不能超过100个字符");
      }
    }
  }
}
