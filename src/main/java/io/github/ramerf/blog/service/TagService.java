package io.github.ramerf.blog.service;

import io.github.ramerf.blog.entity.pojo.TagPoJo;
import io.github.ramerf.blog.entity.request.TagRequest;
import io.github.ramerf.wind.core.service.BaseService;
import java.util.List;
import org.springframework.data.domain.Page;
import org.springframework.transaction.annotation.Transactional;

/**
 * The interface Tag service.
 *
 * @author ramer
 */
public interface TagService extends BaseService<TagPoJo> {

  /**
   * 批量创建并返回id.
   *
   * @param tagRequests the tag requests
   * @return the list
   */
  @Transactional(rollbackFor = Exception.class)
  List<Long> createBatchWithId(List<TagRequest> tagRequests);

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
