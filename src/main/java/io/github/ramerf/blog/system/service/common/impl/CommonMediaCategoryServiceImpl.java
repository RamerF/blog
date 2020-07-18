package io.github.ramerf.blog.system.service.common.impl;

import java.util.Date;
import java.util.List;
import javax.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import io.github.ramerf.blog.system.entity.domain.common.CommonMediaCategory;
import io.github.ramerf.blog.system.exception.CommonException;
import io.github.ramerf.blog.system.repository.BaseRepository;
import io.github.ramerf.blog.system.repository.common.CommonMediaCategoryRepository;
import io.github.ramerf.blog.system.service.common.CommonMediaCategoryService;
import org.springframework.stereotype.Service;

/** @author ramer */
@Slf4j
@Service
public class CommonMediaCategoryServiceImpl implements CommonMediaCategoryService {
  @Resource private CommonMediaCategoryRepository repository;

  @Override
  public List<CommonMediaCategory> getByCode(final String code) {
    return repository.findByCodeAndIsDelete(code, Boolean.FALSE);
  }

  @Override
  public List<CommonMediaCategory> listAfterDate(final String code, final Date updateTime) {
    return repository.findByCodeAndUpdateTimeGreaterThan(code, updateTime);
  }

  @SuppressWarnings({"unchecked"})
  @Override
  public <U extends BaseRepository<CommonMediaCategory, Long>> U getRepository()
      throws CommonException {
    return (U) repository;
  }
}
