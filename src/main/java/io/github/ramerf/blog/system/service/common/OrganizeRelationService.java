package io.github.ramerf.blog.system.service.common;

import java.util.List;
import io.github.ramerf.blog.system.entity.domain.common.OrganizeRelation;
import io.github.ramerf.blog.system.entity.pojo.common.OrganizeRelationPoJo;
import io.github.ramerf.blog.system.service.BaseService;

/** @author ramer */
public interface OrganizeRelationService
    extends BaseService<OrganizeRelation, OrganizeRelationPoJo> {
  List<OrganizeRelation> listParent(final long nextId);

  List<Long> listParentIds(final long nextId, final boolean includeSelf);

  List<Long> listChildrenIds(final long id, final boolean includeSelf);
}
