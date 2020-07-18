package io.github.ramerf.blog.service;

import io.github.ramerf.wind.core.function.IFunction;
import io.github.ramerf.wind.core.service.BaseService;
import java.util.List;
import java.util.Optional;
import javax.annotation.Nonnull;
import io.github.ramerf.blog.entity.pojo.ArticlePoJo;
import org.springframework.data.domain.Page;

/** @author ramer */
public interface ArticleService extends BaseService<ArticlePoJo> {
  Page<ArticlePoJo> page(final String keyword, final int page, final int size);

  long create(@Nonnull ArticlePoJo poJo, List<Long> tagIds, IFunction<ArticlePoJo, ?>[] includeNullProps)
      throws RuntimeException;

  Optional<Integer> update(ArticlePoJo poJo, List<Long> tagIds, IFunction<ArticlePoJo, ?>[] includeNullProps)
      throws RuntimeException;

  Long generateNumber();
}
