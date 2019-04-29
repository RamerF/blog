package org.ramer.admin.system.validator.common;

import org.ramer.admin.system.entity.domain.common.DataDict;
import org.ramer.admin.system.entity.pojo.common.DataDictPoJo;
import org.ramer.admin.system.entity.request.common.DataDictRequest;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;


/** @author ramer */
@Component
public class DataDictValidator implements Validator {
  @Override
  public boolean supports(final Class<?> clazz) {
    return clazz.isAssignableFrom(DataDict.class) || clazz.isAssignableFrom(DataDictRequest.class) || clazz.isAssignableFrom(DataDictPoJo.class);
  }

  @Override
  public void validate(final Object target, final Errors errors) {
    // TODO-WARN: 添加数据字典校验规则
  }
}
