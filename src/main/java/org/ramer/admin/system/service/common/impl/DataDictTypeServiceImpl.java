package org.ramer.admin.system.service.common.impl;

import javax.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.ramer.admin.system.entity.Constant.State;
import org.ramer.admin.system.entity.domain.common.DataDictType;
import org.ramer.admin.system.exception.CommonException;
import org.ramer.admin.system.repository.BaseRepository;
import org.ramer.admin.system.repository.common.DataDictTypeRepository;
import org.ramer.admin.system.service.common.DataDictTypeService;
import org.springframework.stereotype.Service;

/** @author ramer */
@Slf4j
@Service
public class DataDictTypeServiceImpl implements DataDictTypeService {
  @Resource private DataDictTypeRepository repository;

  @Override
  public DataDictType getByCode(final String code) {
    return repository.findByCodeAndState(code, State.STATE_ON);
  }

  @SuppressWarnings({"unchecked"})
  @Override
  public <U extends BaseRepository<DataDictType, Long>> U getRepository() throws CommonException {
    return (U) repository;
  }
}
