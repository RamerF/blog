package io.github.ramerf.blog.service.impl;

import io.github.ramerf.blog.entity.pojo.TagPoJo;
import io.github.ramerf.blog.entity.request.TagRequest;
import io.github.ramerf.blog.repository.TagRepository;
import io.github.ramerf.blog.service.TagService;
import io.github.ramerf.wind.core.condition.SortColumn;
import io.github.ramerf.wind.core.condition.SortColumn.Order;
import io.github.ramerf.wind.core.util.StringUtils;
import java.util.List;
import java.util.Objects;
import javax.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import static java.util.stream.Collectors.toList;

/** @author ramer */
@Slf4j
@Service
public class TagServiceImpl implements TagService {
  @Resource private TagRepository repository;

  @Override
  @Transactional(rollbackFor = Exception.class)
  public List<Long> createBatchWithId(List<TagRequest> tagRequests) {
    return tagRequests.stream()
        .map(this::createIfAbsent)
        .filter(Objects::nonNull)
        .map(Long.class::cast)
        .collect(toList());
  }

  private Long createIfAbsent(final TagRequest tagRequest) {
    final TagPoJo exist = getOne(condition -> condition.eq(TagPoJo::setName, tagRequest.getName()));
    if (exist != null) {
      return exist.getId();
    }
    return create(tagRequest.poJo());
  }

  @Override
  public Page<TagPoJo> page(final String keyword, final int page, final int size) {
    return page(
        condition ->
            condition.like(StringUtils.nonEmpty(keyword), TagPoJo::setName, '%' + keyword + "%"),
        page,
        size,
        SortColumn.by(TagPoJo::getUsingCount, Order.DESC));
  }

  @Override
  @SuppressWarnings("unchecked")
  public <U> U getRepository() throws RuntimeException {
    return (U) repository;
  }
}
