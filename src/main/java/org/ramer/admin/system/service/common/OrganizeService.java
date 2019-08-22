package org.ramer.admin.system.service.common;

import org.ramer.admin.system.entity.domain.common.Manager;
import org.ramer.admin.system.entity.domain.common.Organize;
import org.ramer.admin.system.entity.pojo.common.OrganizePoJo;
import org.ramer.admin.system.entity.response.common.OrganizeMemberRelationResponse;
import org.ramer.admin.system.service.BaseService;
import java.util.Date;
import java.util.List;

/** @author ramer */
public interface OrganizeService extends BaseService<Organize, OrganizePoJo> {
  /** 获取成员组织关系 */
  List<OrganizeMemberRelationResponse> listRelation(Long managersId);

  List<Manager> listMembers(final long id);

  List<Organize> listAfterDate(final Date updateTime);
}
