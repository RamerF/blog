package io.github.ramerf.blog.service.impl;

import io.github.ramerf.wind.core.condition.SortColumn;
import io.github.ramerf.wind.core.condition.SortColumn.Order;
import io.github.ramerf.wind.core.function.IFunction;
import io.github.ramerf.wind.core.util.StringUtils;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.stream.Collectors;
import javax.annotation.Nonnull;
import javax.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import io.github.ramerf.blog.entity.pojo.ArticlePoJo;
import io.github.ramerf.blog.entity.pojo.TagPoJo;
import io.github.ramerf.blog.repository.ArticleRepository;
import io.github.ramerf.blog.service.ArticleService;
import io.github.ramerf.blog.service.TagService;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

/** @author ramer */
@Slf4j
@Service
public class ArticleServiceImpl implements ArticleService {
  @Resource private TagService tagService;
  @Resource private ArticleRepository repository;

  @Override
  public Page<ArticlePoJo> page(final String keyword, final int page, final int size) {
    return page(
        condition ->
            condition.and(
                StringUtils.nonEmpty(keyword),
                condition
                    .condition()
                    .like(StringUtils.nonEmpty(keyword), ArticlePoJo::setTitle, keyword + "%")
                    .or(
                        condition
                            .condition()
                            .like(
                                StringUtils.nonEmpty(keyword),
                                ArticlePoJo::setTagsStr,
                                keyword + "%"))),
        page,
        size,
        SortColumn.by(ArticlePoJo::getViewCount, Order.DESC)
            .desc(ArticlePoJo::getStarCount)
            .desc(ArticlePoJo::getStarCount));
  }

  @Override
  public long create(
      @Nonnull final ArticlePoJo poJo,
      final List<Long> tagIds,
      final IFunction<ArticlePoJo, ?>[] includeNullProps)
      throws RuntimeException {
    setTags(poJo, tagIds);
    getUpdate(true).create(poJo, includeNullProps);
    return poJo.getId();
  }

  @Override
  public Optional<Integer> update(
      final ArticlePoJo poJo,
      final List<Long> tagIds,
      final IFunction<ArticlePoJo, ?>[] includeNullProps)
      throws RuntimeException {
    setTags(poJo, tagIds);
    final int affectRow = getUpdate().update(poJo, includeNullProps);
    return affectRow == 1 ? Optional.empty() : Optional.of(affectRow);
  }

  private void setTags(final ArticlePoJo poJo, List<Long> tagIds) {
    if (CollectionUtils.isEmpty(tagIds)) {
      throw new IllegalArgumentException("标签不能为空");
    }
    // 更新标签集合
    poJo.setTagsStr(
        tagService.listByIds(tagIds).stream().map(TagPoJo::getName).collect(Collectors.joining()));
  }

  @Override
  public Long generateNumber() {
    final List<Long> numbers =
        list(
            query -> query.col(ArticlePoJo::getNumber),
            condition -> condition.eq(ArticlePoJo::setIsDelete, false),
            1,
            1,
            SortColumn.by(ArticlePoJo::getNumber, Order.DESC),
            Long.class);
    // 18位编号递增: 191122170150(时间) + 000001(递增)
    long maxNumber = CollectionUtils.isEmpty(numbers) ? 0 : numbers.get(0);
    // 达到最大后重置为0
    if (maxNumber >= 99999) {
      maxNumber = 0;
    }
    final long seq = maxNumber + 1;
    StringBuilder seqStr = new StringBuilder("" + seq);
    while (seqStr.length() < 4) {
      seqStr.insert(0, "0");
    }
    return Long.parseLong(
        new SimpleDateFormat("yyMMddHHmmss").format(new Date()).concat(seqStr.toString()));
  }

  @Override
  @SuppressWarnings("unchecked")
  public <U> U getRepository() throws RuntimeException {
    return (U) repository;
  }
}
