package org.ramer.admin.system.repository.common;

import org.ramer.admin.system.entity.domain.common.Role;
import org.ramer.admin.system.repository.BaseRepository;
import java.util.List;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface RolesRepository extends BaseRepository<Role, Long> {
  @Query(
      "select r from org.ramer.admin.system.entity.domain.common.Manager m inner join m.roleses r where m.id= :managerId and r.state= :state")
  List<Role> findByManager(@Param("managerId") long managerId, @Param("state") int state);

  @Query(
      "select r.name from org.ramer.admin.system.entity.domain.common.Manager m inner join m.roleses r where m.id= :managerId and r.state= :state")
  List<String> findNameByManager(@Param("managerId") long managerId, @Param("state") int state);
}
