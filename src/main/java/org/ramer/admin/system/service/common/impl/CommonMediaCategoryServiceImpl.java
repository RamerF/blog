package org.ramer.admin.system.service.common.impl;

import java.util.Date;
import java.util.List;
import javax.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.ramer.admin.system.entity.Constant.State;
import org.ramer.admin.system.entity.domain.common.CommonMediaCategory;
import org.ramer.admin.system.exception.CommonException;
import org.ramer.admin.system.repository.BaseRepository;
import org.ramer.admin.system.repository.common.CommonMediaCategoryRepository;
import org.ramer.admin.system.service.common.CommonMediaCategoryService;
import org.springframework.stereotype.Service;

/** @author ramer */
@Slf4j
@Service
public class CommonMediaCategoryServiceImpl implements CommonMediaCategoryService {
  @Resource private CommonMediaCategoryRepository repository;

  @Override
  public List<CommonMediaCategory> getByCode(final String code) {
    return repository.findByCodeAndState(code, State.STATE_ON);
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
