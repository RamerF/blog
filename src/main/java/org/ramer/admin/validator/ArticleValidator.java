package org.ramer.admin.validator;

import javax.annotation.Nonnull;
import org.ramer.admin.entity.domain.Article;
import org.ramer.admin.entity.pojo.ArticlePoJo;
import org.ramer.admin.entity.request.ArticleRequest;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;


/** @author ramer */
@Component
public class ArticleValidator implements Validator {
  @Override
  public boolean supports(final Class<?> clazz) {
    return clazz.isAssignableFrom(Article.class)
        || clazz.isAssignableFrom(ArticleRequest.class)
        || clazz.isAssignableFrom(ArticlePoJo.class);
  }

  @Override
  public void validate(final Object target,@Nonnull final Errors errors) {
    ArticleRequest article = (ArticleRequest) target;
    if (article == null) {
      errors.rejectValue(null, "article.null", "文章 不能为空");
    } else {
      // TODO-WARN: 添加文章校验规则
    }
  }
}
