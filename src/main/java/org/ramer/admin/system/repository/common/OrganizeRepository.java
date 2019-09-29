package org.ramer.admin.system.repository.common;

import java.time.LocalDateTime;
import java.util.List;
import org.ramer.admin.system.entity.domain.common.Organize;
import org.ramer.admin.system.entity.response.common.OrganizeMemberRelationResponse;
import org.ramer.admin.system.repository.BaseRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface OrganizeRepository extends BaseRepository<Organize, Long> {
  @Query(
      "select new org.ramer.admin.system.entity.response.common.OrganizeMemberRelationResponse(m.id,m.name,o.id,o.name) from org.ramer.admin.system.entity.domain.common.Organize o join o.members m where m.id = :membersId and o.state = :state")
  List<OrganizeMemberRelationResponse> findOrganizeMemberRelation(
      @Param("membersId") Long membersId, @Param("state") final int state);

  List<Organize> findByUpdateTimeGreaterThan(final LocalDateTime updateTime);
}
