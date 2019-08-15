package org.ramer.admin.system.validator.common;

import javax.annotation.Nonnull;
import org.ramer.admin.system.entity.domain.common.DataDictType;
import org.ramer.admin.system.entity.pojo.common.DataDictTypePoJo;
import org.ramer.admin.system.entity.request.common.DataDictTypeRequest;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
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
  public void validate(final Object target,@Nonnull final Errors errors) {
    DataDictTypeRequest dataDictType = (DataDictTypeRequest) target;
    if (dataDictType == null) {
      errors.rejectValue(null, "dataDictType.null", "数据字典分类 不能为空");
    } else {
      // TODO-WARN: 添加数据字典分类校验规则
    }
  }
}
