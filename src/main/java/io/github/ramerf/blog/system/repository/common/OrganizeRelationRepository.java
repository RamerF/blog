package io.github.ramerf.blog.system.repository.common;

import java.util.List;
import io.github.ramerf.blog.system.entity.domain.common.OrganizeRelation;
import io.github.ramerf.blog.system.repository.BaseRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface OrganizeRelationRepository extends BaseRepository<OrganizeRelation, Long> {
  List<OrganizeRelation> findByNextIdAndIsDelete(final long nextId, final Boolean isDelete);

  List<OrganizeRelation> findByPrevIdAndIsDelete(final long prevId, final Boolean isDelete);
}
