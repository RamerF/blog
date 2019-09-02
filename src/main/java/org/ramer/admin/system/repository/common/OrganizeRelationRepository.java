package org.ramer.admin.system.repository.common;

import java.util.List;
import org.ramer.admin.system.entity.domain.common.OrganizeRelation;
import org.ramer.admin.system.repository.BaseRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface OrganizeRelationRepository extends BaseRepository<OrganizeRelation, Long> {
  List<OrganizeRelation> findByNextIdAndState(final long nextId, final int state);

  List<OrganizeRelation> findByPrevIdAndState(final long prevId, final int state);
}
