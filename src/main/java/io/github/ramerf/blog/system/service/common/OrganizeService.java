package io.github.ramerf.blog.system.service.common;

import java.time.LocalDateTime;
import java.util.List;
import io.github.ramerf.blog.system.entity.domain.common.Manager;
import io.github.ramerf.blog.system.entity.domain.common.Organize;
import io.github.ramerf.blog.system.entity.pojo.common.OrganizePoJo;
import io.github.ramerf.blog.system.entity.response.common.OrganizeMemberRelationResponse;
import io.github.ramerf.blog.system.service.BaseService;

/** @author ramer */
public interface OrganizeService extends BaseService<Organize, OrganizePoJo> {
  /** 获取成员组织关系 */
  List<OrganizeMemberRelationResponse> listRelation(Long managersId);

  List<Manager> listMembers(final long id);

  List<Organize> listAfterDate(final LocalDateTime updateTime);

  List<Long> listChildrenIds(final long id, final boolean includeSelf);

  List<Organize> listChildren(final long id, final boolean includeSelf);

  List<Organize> listChildren(final long id, final int depth);
}
