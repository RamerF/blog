package org.ramer.admin.system.service.common.impl;

import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;
import javax.annotation.Resource;
import javax.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;
import org.ramer.admin.system.entity.Constant.State;
import org.ramer.admin.system.entity.domain.common.*;
import org.ramer.admin.system.entity.response.common.OrganizeMemberRelationResponse;
import org.ramer.admin.system.exception.CommonException;
import org.ramer.admin.system.repository.BaseRepository;
import org.ramer.admin.system.repository.common.OrganizeRepository;
import org.ramer.admin.system.service.common.OrganizeRelationService;
import org.ramer.admin.system.service.common.OrganizeService;
import org.springframework.stereotype.Service;

/** @author ramer */
@Slf4j
@Service
public class OrganizeServiceImpl implements OrganizeService {
  @Resource private OrganizeRepository repository;
  @Resource private OrganizeRelationService relationService;

  @Transactional
  @Override
  public Organize create(final Organize organize) throws RuntimeException {
    repository.saveAndFlush(organize);
    // 更新: 自身祖先(rootId),上级是否有子节点(hasChild)
    final Long prevId = organize.getPrevId();
    if (Objects.isNull(prevId)) {
      organize.setRootId(organize.getId());
      repository.saveAndFlush(organize);
    } else {
      final Organize prev = getById(prevId);
      if (Objects.isNull(prev.getHasChild()) || !prev.getHasChild()) {
        prev.setHasChild(true);
        repository.saveAndFlush(prev);
      }
      organize.setRootId(prev.getRootId());
      repository.saveAndFlush(organize);
    }

    // TODO-WARN: 维护关系表

    /*
     * 保存以下结构(id),按顺序保存:
     * 步骤: 1. 保存自身 3.获取并遍历next_id为prev_id的数据集,将next_id替换为当前保存元素id并保存.
     * -1
     * --11
     * --12
     * ---121
     * ----1211
     * prev_id    next_id   depth
     * 1          1         0
     *
     * 11         11        0
     * 1          11        1
     *
     * 12         12        0
     * 1          12        1
     *
     * 121        121       0
     * 12         121       1
     * 1          121       2
     *
     * 1211       1211      0
     * 121        1211      1
     * 12         1211      2
     * 1          1211      3
     *
     */
    OrganizeRelation organizeRelation = new OrganizeRelation();
    organizeRelation.setPrevId(organize.getId());
    organizeRelation.setNextId(organize.getId());
    organizeRelation.setDepth(0);
    relationService.create(organizeRelation);

    if (Objects.nonNull(prevId)) {
      relationService.createBatch(
          relationService.listParent(prevId).stream()
              .map(
                  o ->
                      OrganizeRelation.of(
                          o.getPrevId(),
                          organize.getId(),
                          Objects.equals(o.getPrevId(), prevId) ? 1 : o.getDepth() + 1))
              .collect(Collectors.toList()));
    }
    return organize;
  }

  @Override
  public Organize update(final Organize organize) throws RuntimeException {
    // TODO-WARN: 组织更新逻辑
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
    return Optional.ofNullable(getById(id)).map(Organize::getMembers).orElse(new ArrayList<>());
  }

  @Override
  public List<Organize> listAfterDate(final LocalDateTime updateTime) {
    return repository.findByUpdateTimeGreaterThan(updateTime);
  }

  @Override
  public List<Long> listChildrenIds(final long id, final boolean includeSelf) {
    return relationService.listChildrenIds(id, includeSelf);
  }

  @Override
  public List<Organize> listChildren(final long id, final boolean includeSelf) {
    return listByIds(listChildrenIds(id, includeSelf));
  }

  @Override
  public List<Organize> listChildren(final long id, final int depth) {
    return null;
  }

  @SuppressWarnings({"unchecked"})
  @Override
  public <U extends BaseRepository<Organize, Long>> U getRepository() throws CommonException {
    return (U) repository;
  }
}
