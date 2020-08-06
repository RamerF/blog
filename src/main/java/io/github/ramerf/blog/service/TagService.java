package io.github.ramerf.blog.service;

import io.github.ramerf.blog.entity.pojo.TagPoJo;
import io.github.ramerf.wind.core.service.BaseService;
import org.springframework.data.domain.Page;

/** @author ramer */
public interface TagService extends BaseService<TagPoJo> {

  /**
   * Page page.
   *
   * @param keyword the keyword
   * @param page the page
   * @param size the size
   * @return the page
   */
  Page<TagPoJo> page(final String keyword, final int page, final int size);
}
