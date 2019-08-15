package org.ramer.admin.system.service.common.impl;

import org.ramer.admin.system.entity.Constant;
import org.ramer.admin.system.entity.domain.common.DataDictType;
import org.ramer.admin.system.exception.CommonException;
import org.ramer.admin.system.repository.BaseRepository;
import org.ramer.admin.system.repository.common.DataDictTypeRepository;
import org.ramer.admin.system.service.BaseService;
import org.ramer.admin.system.service.common.DataDictTypeService;
import org.ramer.admin.system.util.TextUtil;
import java.util.*;
import javax.annotation.Resource;
import javax.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.*;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

/** @author ramer */
@Slf4j
@Service
public class DataDictTypeServiceImpl implements DataDictTypeService {
  @Resource private DataDictTypeRepository repository;

  @SuppressWarnings({"unchecked"})
  @Override
  public <U extends BaseRepository<DataDictType, Long>> U getRepository() throws CommonException {
    return (U) repository;
  }
}
