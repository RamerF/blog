package org.ramer.admin.system.validator.common;

import java.util.Objects;
import javax.annotation.Nonnull;
import javax.annotation.Resource;
import org.ramer.admin.system.entity.domain.common.Privilege;
import org.ramer.admin.system.entity.pojo.common.PrivilegePoJo;
import org.ramer.admin.system.entity.request.common.PrivilegeRequest;
import org.ramer.admin.system.service.common.PrivilegeService;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

/** @author ramer */
@Component
public class PrivilegeValidator implements Validator {
  @Resource private PrivilegeService service;

  @Override
  public boolean supports(final Class<?> clazz) {
    return clazz.isAssignableFrom(Privilege.class)
        || clazz.isAssignableFrom(PrivilegeRequest.class)
        || clazz.isAssignableFrom(PrivilegePoJo.class);
  }

  @Override
  public void validate(final Object target, @Nonnull final Errors errors) {
    PrivilegeRequest privilege = (PrivilegeRequest) target;
    if (privilege == null) {
      errors.rejectValue(null, "privilege.null", "权限 不能为空");
    } else {
      final String exp = privilege.getExp();
      if (StringUtils.isEmpty(exp) || exp.length() > 25) {
        errors.rejectValue("exp", "privilege.exp.length", "表达式 不能为空且小于25个字符");
      } else {
        final Privilege exist = service.getByExp(exp);
        if (Objects.nonNull(exist) && !Objects.equals(privilege.getId(), exist.getId())) {
          errors.rejectValue("exp", "privilege.exp.exist", "表达式 已存在");
        }
      }
      final String name = privilege.getName();
      if (StringUtils.isEmpty(name) || name.length() > 25) {
        errors.rejectValue("name", "privilege.name.length", "名称 不能为空且小于25个字符");
      }
    }
  }
}
