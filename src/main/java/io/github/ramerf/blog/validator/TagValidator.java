package io.github.ramerf.blog.validator;

import java.util.Objects;
import javax.annotation.Nonnull;
import javax.annotation.Resource;
import io.github.ramerf.blog.entity.domain.Tag;
import io.github.ramerf.blog.entity.pojo.TagPoJo;
import io.github.ramerf.blog.entity.request.TagRequest;
import io.github.ramerf.blog.service.TagService;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

/** @author ramer */
@Component
public class TagValidator implements Validator {
  @Resource private TagService service;

  @Override
  public boolean supports(final Class<?> clazz) {
    return clazz.isAssignableFrom(Tag.class)
        || clazz.isAssignableFrom(TagPoJo.class)
        || clazz.isAssignableFrom(TagRequest.class);
  }

  @Override
  public void validate(final Object target, @Nonnull final Errors errors) {
    TagRequest tag = (TagRequest) target;
    if (tag == null) {
      errors.rejectValue(null, "tag.null", "标签 不能为空");
    } else {
      if (Objects.isNull(tag.getId())) {
        if (StringUtils.isEmpty(tag.getName())) {
          errors.rejectValue("title", "tag.name.length", "标题 不能为空");
        }
        if (!tag.getName().matches("^[a-zA-Z]")) {
          errors.rejectValue("title", "tag.name.length", "标题 应为2-10个字母");
        }

      }
    }
  }
}
