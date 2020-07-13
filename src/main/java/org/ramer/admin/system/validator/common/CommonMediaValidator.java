package org.ramer.admin.system.validator.common;

import org.ramer.admin.system.entity.domain.common.CommonMedia;
import org.ramer.admin.system.entity.pojo.common.CommonMediaPoJo;
import org.ramer.admin.system.entity.request.common.CommonMediaRequest;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;


/** @author ramer */
@Component
public class CommonMediaValidator implements Validator {
  @Override
  public boolean supports(final Class<?> clazz) {
    return clazz.isAssignableFrom(CommonMedia.class) || clazz.isAssignableFrom(CommonMediaRequest.class) || clazz.isAssignableFrom(CommonMediaPoJo.class);
  }

  @Override
  public void validate(final Object target, final Errors errors) {
    // TODO-WARN: 添加通用多媒体校验规则
  }
}
