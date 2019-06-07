package org.ramer.admin.system.service.common.impl;

import java.util.*;
import java.util.stream.Collectors;
import javax.annotation.Resource;
import javax.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;
import org.ramer.admin.system.entity.Constant.State;
import org.ramer.admin.system.entity.domain.common.Manager;
import org.ramer.admin.system.entity.domain.common.Organize;
import org.ramer.admin.system.entity.response.common.OrganizeMemberRelationResponse;
import org.ramer.admin.system.exception.CommonException;
import org.ramer.admin.system.repository.BaseRepository;
import org.ramer.admin.system.repository.common.OrganizeRepository;
import org.ramer.admin.system.service.common.OrganizeService;
import org.springframework.stereotype.Service;

/** @author ramer */
@Slf4j
@Service
public class OrganizeServiceImpl implements OrganizeService {
  @Resource private OrganizeRepository repository;

  @Transactional
  @Override
  public Organize create(final Organize organize) throws RuntimeException {
    repository.saveAndFlush(organize);
    // 更新: 自身祖先(rootId),上级是否有子节点(hasChild)
    if (Objects.isNull(organize.getPrevId())) {
      organize.setRootId(organize.getId());
      repository.saveAndFlush(organize);
    } else {
      final Organize prev = getById(organize.getPrevId());
      if (Objects.isNull(prev.getHasChild()) || !prev.getHasChild()) {
        prev.setHasChild(true);
        repository.saveAndFlush(prev);
      }
      organize.setRootId(prev.getRootId());
      repository.saveAndFlush(organize);
    }
    return organize;
  }

  @Override
  public Organize update(final Organize organize) throws RuntimeException {
    return create(organize);
  }

  @Override
  public List<OrganizeMemberRelationResponse> listRelation(final Long managersId) {
    return repository.findOrganizeMemberRelation(managersId, State.STATE_ON).stream()
        .collect(Collectors.groupingBy(OrganizeMemberRelationResponse::getMemberId))
        .get(managersId);
  }

  @Override
  public List<Manager> listMembers(final long id) {
    return getById(id).getMembers();
  }

  @Override
  public List<Manager> listLeaders(final long id) {
    return getById(id).getLeaders();
  }

  @Override
  public List<Organize> listAfterDate(final Date updateTime) {
    return repository.findByUpdateTimeGreaterThan(updateTime );
  }

  @SuppressWarnings({"unchecked"})
  @Override
  public <U extends BaseRepository<Organize, Long>> U getRepository() throws CommonException {
    return (U) repository;
  }
}
