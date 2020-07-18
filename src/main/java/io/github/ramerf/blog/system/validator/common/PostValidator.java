package io.github.ramerf.blog.system.validator.common;

import javax.annotation.Nonnull;
import io.github.ramerf.blog.system.entity.domain.common.Post;
import io.github.ramerf.blog.system.entity.pojo.common.PostPoJo;
import io.github.ramerf.blog.system.entity.request.common.PostRequest;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;


/** @author ramer */
@Component
public class PostValidator implements Validator {
  @Override
  public boolean supports(final Class<?> clazz) {
    return clazz.isAssignableFrom(Post.class)
        || clazz.isAssignableFrom(PostRequest.class)
        || clazz.isAssignableFrom(PostPoJo.class);
  }

  @Override
  public void validate(final Object target,@Nonnull final Errors errors) {
    PostRequest post = (PostRequest) target;
    if (post == null) {
      errors.rejectValue(null, "post.null", "岗位 不能为空");
    } else {
      // TODO-WARN: 添加岗位校验规则
    }
  }
}
