package org.ramer.admin.system.service.common.impl;

import com.querydsl.jpa.impl.JPAQueryFactory;
import java.util.ArrayList;
import java.util.List;
import javax.annotation.Resource;
import javax.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;
import org.ramer.admin.system.entity.Constant.PrivilegeEnum;
import org.ramer.admin.system.entity.Constant.State;
import org.ramer.admin.system.entity.domain.common.*;
import org.ramer.admin.system.exception.CommonException;
import org.ramer.admin.system.repository.BaseRepository;
import org.ramer.admin.system.repository.common.PrivilegeRepository;
import org.ramer.admin.system.service.common.PrivilegeService;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

/** @author ramer */
@Slf4j
@Service
public class PrivilegeServiceImpl implements PrivilegeService {
  @Resource private JPAQueryFactory jpaQueryFactory;
  @Resource private PrivilegeRepository repository;

  @Transactional
  @Override
  public synchronized List<Privilege> create(final String expPrefix, final String expName) {
    List<Privilege> privileges = new ArrayList<>();
    PrivilegeEnum.map()
        .forEach(
            (name, remark) -> {
              final Privilege p = new Privilege();
              p.setExp(expPrefix + ":" + name);
              p.setName(expName + ":" + remark);
              privileges.add(create(p));
            });
    return repository.saveAll(privileges);
  }

  @Override
  public Privilege getByExp(final String exp) {
    return repository.findTopByExpAndState(exp, State.STATE_ON);
  }

  @Override
  public List<Privilege> listByManagerId(final long managerId) {
    final QPrivilege privilege = QPrivilege.privilege;
    final QRole role = QRole.role;
    final QManager manager = QManager.manager;
    return jpaQueryFactory
        .selectFrom(privilege)
        .leftJoin(role)
        .on(role.privileges.contains(privilege))
        .leftJoin(manager)
        .on(manager.roles.contains(role).and(manager.id.eq(managerId)))
        .where(privilege.state.eq(State.STATE_ON).and(role.state.eq(State.STATE_ON)))
        .fetch();
  }

  @Override
  public List<Privilege> listByRoles(Long rolesId) {
    final QPrivilege privilege = QPrivilege.privilege;
    final QRole role = QRole.role;
    return jpaQueryFactory
        .selectFrom(privilege)
        .leftJoin(role)
        .on(role.privileges.contains(privilege).and(role.id.eq(rolesId)))
        .where(privilege.state.eq(State.STATE_ON))
        .fetch();
  }

  @Override
  public Specification<Privilege> getSpec(String criteria) {
    return StringUtils.isEmpty(criteria)
        ? (root, query, builder) -> builder.and(builder.equal(root.get("state"), State.STATE_ON))
        : (root, query, builder) ->
            builder.and(
                builder.equal(root.get("state"), State.STATE_ON),
                builder.or(
                    builder.like(root.get("exp"), "%" + criteria + "%"),
                    builder.like(root.get("remark"), "%" + criteria + "%")));
  }

  @SuppressWarnings({"unchecked"})
  @Override
  public <U extends BaseRepository<Privilege, Long>> U getRepository() throws CommonException {
    return (U) repository;
  }
}
