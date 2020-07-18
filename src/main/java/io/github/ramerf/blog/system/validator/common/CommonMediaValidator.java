package io.github.ramerf.blog.system.validator.common;

import io.github.ramerf.blog.system.entity.domain.common.CommonMedia;
import io.github.ramerf.blog.system.entity.pojo.common.CommonMediaPoJo;
import io.github.ramerf.blog.system.entity.request.common.CommonMediaRequest;
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
