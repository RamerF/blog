package io.github.ramerf.blog.service;

import io.github.ramerf.blog.entity.pojo.ArticlePoJo;
import io.github.ramerf.blog.entity.request.ArticleRequest;
import io.github.ramerf.wind.core.service.BaseService;
import javax.annotation.Nonnull;
import org.springframework.data.domain.Page;

/**
 * The interface Article service.
 *
 * @author ramer
 */
public interface ArticleService extends BaseService<ArticlePoJo> {

  /**
   * Create long.
   *
   * @param request the request
   * @return the long
   * @throws RuntimeException the runtime exception
   */
  long create(@Nonnull ArticleRequest request) throws RuntimeException;

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
   * Update optional.
   *
   * @param request the request
   * @return id
   * @throws RuntimeException the runtime exception
   */
  long update(ArticleRequest request) throws RuntimeException;

  /**
   * Generate number long.
   *
   * @return the long
   */
  Long generateNumber();
}
