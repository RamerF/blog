package io.github.ramerf.blog.service;

import io.github.ramerf.wind.core.function.IFunction;
import io.github.ramerf.wind.core.service.BaseService;
import java.util.List;
import java.util.Optional;
import javax.annotation.Nonnull;
import io.github.ramerf.blog.entity.pojo.ArticlePoJo;
import org.springframework.data.domain.Page;

/**
 * The interface Article service.
 *
 * @author ramer
 */
public interface ArticleService extends BaseService<ArticlePoJo> {
  /**
   * Page page.
   *
   * @param keyword the keyword
   * @param page the page
   * @param size the size
   * @return the page
   */
  Page<ArticlePoJo> page(final String keyword, final int page, final int size);

  /**
   * Create long.
   *
   * @param poJo the po jo
   * @param tagIds the tag ids
   * @param includeNullProps the include null props
   * @return the long
   * @throws RuntimeException the runtime exception
   */
  long create(
      @Nonnull ArticlePoJo poJo,
      List<Long> tagIds,
      List<IFunction<ArticlePoJo, ?>> includeNullProps)
      throws RuntimeException;

  /**
   * Update optional.
   *
   * @param poJo the po jo
   * @param tagIds the tag ids
   * @param includeNullProps the include null props
   * @return the optional
   * @throws RuntimeException the runtime exception
   */
  Optional<Integer> update(
      ArticlePoJo poJo, List<Long> tagIds, List<IFunction<ArticlePoJo, ?>> includeNullProps)
      throws RuntimeException;

  /**
   * Generate number long.
   *
   * @return the long
   */
  Long generateNumber();
}
