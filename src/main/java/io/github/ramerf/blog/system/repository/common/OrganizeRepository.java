package io.github.ramerf.blog.system.repository.common;

import java.time.LocalDateTime;
import java.util.List;
import io.github.ramerf.blog.system.entity.domain.common.Organize;
import io.github.ramerf.blog.system.entity.response.common.OrganizeMemberRelationResponse;
import io.github.ramerf.blog.system.repository.BaseRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface OrganizeRepository extends BaseRepository<Organize, Long> {
  @Query(
      "select new io.github.ramerf.blog.system.entity.response.common.OrganizeMemberRelationResponse(m.id,m.name,o.id,o.name) from io.github.ramerf.blog.system.entity.domain.common.Organize o join o.members m where m.id = :membersId and o.isDelete = :isDelete")
  List<OrganizeMemberRelationResponse> findOrganizeMemberRelation(
      @Param("membersId") Long membersId, @Param("isDelete") final Boolean isDelete);

  List<Organize> findByUpdateTimeGreaterThan(final LocalDateTime updateTime);
}
