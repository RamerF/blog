package org.ramer.admin.system.validator.common;

import org.ramer.admin.system.entity.domain.common.DataDictType;
import org.ramer.admin.system.entity.pojo.common.DataDictTypePoJo;
import org.ramer.admin.system.entity.request.common.DataDictTypeRequest;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

/** @author ramer */
@Component
public class DataDictTypeValidator implements Validator {
  @Override
  public boolean supports(final Class<?> clazz) {
    return clazz.isAssignableFrom(DataDictType.class)
        || clazz.isAssignableFrom(DataDictTypeRequest.class)
        || clazz.isAssignableFrom(DataDictTypePoJo.class);
  }

  @Override
  public void validate(final Object target, final Errors errors) {
    // TODO-WARN: 添加数据字典类型校验规则
  }
}
