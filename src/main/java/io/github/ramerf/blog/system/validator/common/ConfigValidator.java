package io.github.ramerf.blog.system.validator.common;

import java.util.Objects;
import javax.annotation.Nonnull;
import javax.annotation.Resource;
import io.github.ramerf.blog.system.entity.domain.common.Config;
import io.github.ramerf.blog.system.entity.pojo.common.ConfigPoJo;
import io.github.ramerf.blog.system.entity.request.common.ConfigRequest;
import io.github.ramerf.blog.system.service.common.ConfigService;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

/** @author ramer */
@Component
public class ConfigValidator implements Validator {
  @Resource private ConfigService service;

  @Override
  public boolean supports(@Nonnull final Class<?> clazz) {
    return clazz.isAssignableFrom(Config.class)
        || clazz.isAssignableFrom(ConfigRequest.class)
        || clazz.isAssignableFrom(ConfigPoJo.class);
  }

  @Override
  public void validate(final Object target, @Nonnull final Errors errors) {
    ConfigRequest config = (ConfigRequest) target;
    if (config == null) {
      errors.rejectValue(null, "config.null", "系统配置 不能为空");
    } else {
      final String code = config.getCode();
      if (StringUtils.isEmpty(code) || code.length() > 50) {
        errors.rejectValue("code", "config.code.length", "CODE 不能为空且小于50个字符");
      } else {
        final Config exist = service.getByCode(code);
        if (Objects.nonNull(exist) && !Objects.equals(config.getId(), exist.getId())) {
          errors.rejectValue("code", "config.code.exist", "CODE 已存在");
        }
      }
      final String name = config.getName();
      if (StringUtils.isEmpty(name) || name.length() > 50) {
        errors.rejectValue("name", "config.name.length", "名称 不能为空且小于50个字符");
      }
      final String value = config.getValue();
      if (StringUtils.isEmpty(value) || name.length() > 100) {
        errors.rejectValue("value", "config.value.length", "值 不能为空且小于100个字符");
      }
      final String remark = config.getRemark();
      if (!StringUtils.isEmpty(remark) && remark.length() > 65535) {
        errors.rejectValue("remark", "config.remark.length", "备注 长度不能大于65535个字符");
      }
    }
  }
}
