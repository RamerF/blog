package org.ramer.admin.system.service.common.impl;

import java.util.List;
import javax.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.ramer.admin.system.entity.Constant.State;
import org.ramer.admin.system.entity.domain.common.OrganizeRelation;
import org.ramer.admin.system.exception.CommonException;
import org.ramer.admin.system.repository.BaseRepository;
import org.ramer.admin.system.repository.common.OrganizeRelationRepository;
import org.ramer.admin.system.service.common.OrganizeRelationService;
import org.springframework.stereotype.Service;

/** @author ramer */
@Slf4j
@Service
public class OrganizeRelationServiceImpl implements OrganizeRelationService {
  @Resource private OrganizeRelationRepository repository;

  @Override
  public List<OrganizeRelation> listByNextId(final long nextId) {
    return repository.findByNextIdAndState(nextId, State.STATE_ON);
  }

  @SuppressWarnings({"unchecked"})
  @Override
  public <U extends BaseRepository<OrganizeRelation, Long>> U getRepository()
      throws CommonException {
    return (U) repository;
  }
}
