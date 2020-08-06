package io.github.ramerf.blog.service.impl;

import io.github.ramerf.blog.entity.pojo.TagPoJo;
import io.github.ramerf.blog.repository.TagRepository;
import io.github.ramerf.blog.service.TagService;
import io.github.ramerf.wind.core.condition.SortColumn;
import io.github.ramerf.wind.core.condition.SortColumn.Order;
import io.github.ramerf.wind.core.util.StringUtils;
import javax.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;

/** @author ramer */
@Slf4j
@Service
public class TagServiceImpl implements TagService {
  @Resource private TagRepository repository;

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
