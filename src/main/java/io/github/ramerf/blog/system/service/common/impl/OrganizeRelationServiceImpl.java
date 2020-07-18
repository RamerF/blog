package io.github.ramerf.blog.system.service.common.impl;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;
import javax.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import io.github.ramerf.blog.system.entity.domain.AbstractEntity;
import io.github.ramerf.blog.system.entity.domain.common.OrganizeRelation;
import io.github.ramerf.blog.system.exception.CommonException;
import io.github.ramerf.blog.system.repository.BaseRepository;
import io.github.ramerf.blog.system.repository.common.OrganizeRelationRepository;
import io.github.ramerf.blog.system.service.common.OrganizeRelationService;
import org.springframework.stereotype.Service;

/** @author ramer */
@Slf4j
@Service
public class OrganizeRelationServiceImpl implements OrganizeRelationService {
  @Resource private OrganizeRelationRepository repository;

  @Override
  public List<OrganizeRelation> listParent(final long nextId) {
    return repository.findByNextIdAndIsDelete(nextId, Boolean.FALSE);
  }

  @Override
  public List<Long> listParentIds(final long nextId, final boolean includeSelf) {
    return includeSelf
        ? repository.findByNextIdAndIsDelete(nextId, Boolean.FALSE).stream()
            .map(AbstractEntity::getId)
            .collect(Collectors.toList())
        : repository.findByNextIdAndIsDelete(nextId, Boolean.FALSE).stream()
            .filter(o -> !Objects.equals(o.getId(), nextId))
            .map(OrganizeRelation::getPrevId)
            .collect(Collectors.toList());
  }

  @Override
  public List<Long> listChildrenIds(final long prevId, final boolean includeSelf) {
    return includeSelf
        ? repository.findByPrevIdAndIsDelete(prevId, Boolean.FALSE).stream()
            .map(OrganizeRelation::getNextId)
            .collect(Collectors.toList())
        : repository.findByPrevIdAndIsDelete(prevId, Boolean.FALSE).stream()
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
