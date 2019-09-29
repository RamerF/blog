package org.ramer.admin.system.service.common.impl;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;
import javax.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.ramer.admin.system.entity.Constant.State;
import org.ramer.admin.system.entity.domain.AbstractEntity;
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
  public List<OrganizeRelation> listParent(final long nextId) {
    return repository.findByNextIdAndState(nextId, State.STATE_ON);
  }

  @Override
  public List<Long> listParentIds(final long nextId, final boolean includeSelf) {
    return includeSelf
        ? repository.findByNextIdAndState(nextId, State.STATE_ON).stream()
            .map(AbstractEntity::getId)
            .collect(Collectors.toList())
        : repository.findByNextIdAndState(nextId, State.STATE_ON).stream()
            .filter(o -> !Objects.equals(o.getId(), nextId))
            .map(OrganizeRelation::getPrevId)
            .collect(Collectors.toList());
  }

  @Override
  public List<Long> listChildrenIds(final long prevId, final boolean includeSelf) {
    return includeSelf
        ? repository.findByPrevIdAndState(prevId, State.STATE_ON).stream()
            .map(OrganizeRelation::getNextId)
            .collect(Collectors.toList())
        : repository.findByPrevIdAndState(prevId, State.STATE_ON).stream()
            .filter(o -> !Objects.equals(o.getId(), prevId))
            .map(OrganizeRelation::getNextId)
            .collect(Collectors.toList());
  }

  @SuppressWarnings({"unchecked"})
  @Override
  public <U extends BaseRepository<OrganizeRelation, Long>> U getRepository()
      throws CommonException {
    return (U) repository;
  }
}
