package org.ramer.admin.system.service.common;

import java.util.List;
import org.ramer.admin.system.entity.domain.common.OrganizeRelation;
import org.ramer.admin.system.entity.pojo.common.OrganizeRelationPoJo;
import org.ramer.admin.system.service.BaseService;

/** @author ramer */
public interface OrganizeRelationService
    extends BaseService<OrganizeRelation, OrganizeRelationPoJo> {
  List<OrganizeRelation> listByNextId(final long nextId);
}
