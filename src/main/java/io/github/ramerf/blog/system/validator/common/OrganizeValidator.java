package io.github.ramerf.blog.system.validator.common;

import java.util.Objects;
import javax.annotation.Nonnull;
import javax.annotation.Resource;
import io.github.ramerf.blog.system.entity.domain.common.Organize;
import io.github.ramerf.blog.system.entity.pojo.common.OrganizePoJo;
import io.github.ramerf.blog.system.entity.request.common.OrganizeRequest;
import io.github.ramerf.blog.system.service.common.OrganizeService;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

/** @author ramer */
@Component
public class OrganizeValidator implements Validator {
  @Resource private OrganizeService service;

  @Override
  public boolean supports(final Class<?> clazz) {
    return clazz.isAssignableFrom(Organize.class)
        || clazz.isAssignableFrom(OrganizeRequest.class)
        || clazz.isAssignableFrom(OrganizePoJo.class);
  }

  @Override
  public void validate(final Object target, @Nonnull final Errors errors) {
    final OrganizeRequest organize = (OrganizeRequest) target;
    if (organize == null) {
      errors.rejectValue(null, "organize.null", "参数不能为空");
    } else {
      if (StringUtils.isEmpty(organize.getName())) {
        errors.rejectValue("name", "organize.name.empty", "名称 不能为空");
      }
      if (!Objects.isNull(organize.getPrevId())
          && Objects.isNull(service.getById(organize.getPrevId()))) {
        errors.rejectValue("name", "organize.prevId.error", "上级组织不存在 !");
      }
    }
  }
}
