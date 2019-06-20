package org.ramer.admin.system.validator.common;

import java.util.Objects;
import javax.annotation.Nonnull;
import javax.annotation.Resource;
import org.ramer.admin.system.entity.domain.common.DataDict;
import org.ramer.admin.system.entity.pojo.common.DataDictPoJo;
import org.ramer.admin.system.entity.request.common.ConfigRequest;
import org.ramer.admin.system.entity.request.common.DataDictRequest;
import org.ramer.admin.system.service.common.DataDictService;
import org.ramer.admin.system.service.common.DataDictTypeService;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

/** @author ramer */
@Component
public class DataDictValidator implements Validator {
  @Resource private DataDictService service;
  @Resource private DataDictTypeService typeService;

  @Override
  public boolean supports(final Class<?> clazz) {
    return clazz.isAssignableFrom(DataDict.class)
        || clazz.isAssignableFrom(DataDictRequest.class)
        || clazz.isAssignableFrom(DataDictPoJo.class);
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
        final DataDict exist = service.getByCode(code);
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
