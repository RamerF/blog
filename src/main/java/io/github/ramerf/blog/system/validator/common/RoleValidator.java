package io.github.ramerf.blog.system.validator.common;

import java.util.Objects;
import java.util.Optional;
import javax.annotation.Nonnull;
import javax.annotation.Resource;
import io.github.ramerf.blog.system.entity.domain.AbstractEntity;
import io.github.ramerf.blog.system.entity.domain.common.Role;
import io.github.ramerf.blog.system.entity.pojo.common.RolePoJo;
import io.github.ramerf.blog.system.entity.request.common.RoleRequest;
import io.github.ramerf.blog.system.service.common.RoleService;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

/** @author ramer */
@Component
public class RoleValidator implements Validator {
  @Resource private RoleService service;

  @Override
  public boolean supports(final Class<?> clazz) {
    return clazz.isAssignableFrom(Role.class)
        || clazz.isAssignableFrom(RoleRequest.class)
        || clazz.isAssignableFrom(RolePoJo.class);
  }

  @Override
  public void validate(final Object target, @Nonnull final Errors errors) {
    RoleRequest role = (RoleRequest) target;
    if (role == null) {
      errors.rejectValue(null, "role.null", "角色 不能为空");
    } else {
      if (StringUtils.isEmpty(role.getName()) || role.getName().length() > 20) {
        errors.rejectValue("name", "roles.name.length", "名称 不能为空且小于20个字符");
      } else if (Objects.nonNull(role.getId())
          && Objects.equals(
              role.getName(),
              Optional.ofNullable(service.getByName(role.getName()))
                  .map(AbstractEntity::getId)
                  .orElse(null))) {
        errors.rejectValue("name", "menu.remark.exist", "名称 已存在");
      }
      String remark = role.getRemark();
      if (!StringUtils.isEmpty(remark) && remark.length() > 100) {
        errors.rejectValue("remark", "menu.remark.length", "备注 不能超过100个字符");
      }
    }
  }
}
