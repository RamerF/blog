package org.ramer.admin.system.validator.common;

import org.ramer.admin.system.entity.domain.common.Roles;
import org.ramer.admin.system.entity.pojo.common.RolesPoJo;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

/** @author ramer */
@Component
public class RolesValidator implements Validator {
  @Override
  public boolean supports(final Class<?> clazz) {
    return clazz.isAssignableFrom(Roles.class) || clazz.isAssignableFrom(RolesPoJo.class);
  }

  @Override
  public void validate(final Object target, final Errors errors) {
    final Roles roles = (Roles) target;
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
