package org.ramer.admin.system.service.common.impl;

import java.util.*;
import java.util.concurrent.atomic.AtomicBoolean;
import javax.annotation.Resource;
import javax.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;
import org.ramer.admin.system.entity.Constant;
import org.ramer.admin.system.entity.Constant.CommonMediaCode;
import org.ramer.admin.system.entity.domain.common.CommonMedia;
import org.ramer.admin.system.entity.domain.common.CommonMediaCategory;
import org.ramer.admin.system.exception.CommonException;
import org.ramer.admin.system.repository.BaseRepository;
import org.ramer.admin.system.repository.common.CommonMediaRepository;
import org.ramer.admin.system.service.common.CommonMediaCategoryService;
import org.ramer.admin.system.service.common.CommonMediaService;
import org.springframework.data.domain.*;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

/** @author ramer */
@Slf4j
@Service
public class CommonMediaServiceImpl implements CommonMediaService {
  @Resource private CommonMediaRepository repository;
  @Resource private CommonMediaCategoryService mediaCategoryService;

  @Transactional
  @Override
  public List<CommonMedia> createBatch(List<CommonMedia> medias) {
    if (CollectionUtils.isEmpty(medias)) {
      return new ArrayList<>();
    }
    AtomicBoolean taiQu = new AtomicBoolean(false);
    Map<Long, CommonMediaCategory> mediaCategoryMap = new HashMap<>();
    medias.stream()
        .filter(media -> media.getCategoryId() != null && media.getCategory() != null)
        .forEach(
            media -> {
              CommonMediaCategory mediaCategory = mediaCategoryMap.get(media.getCategory().getId());
              if (mediaCategory == null) {
                mediaCategory = mediaCategoryService.getById(media.getCategory().getId());
                mediaCategoryMap.put(media.getCategory().getId(), mediaCategory);
              }
              media.setCategory(mediaCategory);
              if (media.getCode().equals(CommonMediaCode.TAI_QU)) {
                taiQu.set(true);
              }
            });
    return repository.saveAll(medias);
  }

  @Override
  public List<CommonMedia> listByCode(final String code) {
    return pageByCode(code, -1, -1).getContent();
  }

  @Override
  public Page<CommonMedia> pageByCode(final String code, final int page, final int size) {
    final PageRequest pageable = pageRequest(page, size);
    if (pageable == null) {
      return new PageImpl<>(Collections.emptyList());
    }
    return repository.findByCodeAndState(code, Constant.STATE_ON, pageable);
  }

  @Override
  public List<CommonMedia> listByCodeAndAfterDate(final String code, final Date updateDate) {
    return repository.findByCodeAndUpdateTimeGreaterThan(code, updateDate);
  }

  @Transactional
  @Override
  public synchronized CommonMedia update(CommonMedia media) {
    return Optional.ofNullable(getById(media.getId()))
        .map(
            commonMedia -> {
              if (!StringUtils.isEmpty(media.getUrl())) {
                commonMedia.setUrl(media.getUrl());
              }
              return repository.saveAndFlush(commonMedia);
            })
        .orElse(null);
  }

  @Override
  public Specification<CommonMedia> getSpec(String criteria) {
    return StringUtils.isEmpty(criteria)
        ? (root, query, builder) -> builder.and(builder.equal(root.get("state"), Constant.STATE_ON))
        : (root, query, builder) ->
            builder.and(
                builder.equal(root.get("state"), Constant.STATE_ON),
                builder.or(builder.like(root.get("categoryName"), "%" + criteria + "%")));
  }

  @SuppressWarnings({"unchecked"})
  @Override
  public <U extends BaseRepository<CommonMedia, Long>> U getRepository() throws CommonException {
    return (U) repository;
  }
}
