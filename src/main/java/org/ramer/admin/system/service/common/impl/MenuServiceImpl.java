package org.ramer.admin.system.service.common.impl;

import com.querydsl.jpa.impl.JPAQueryFactory;
import java.util.List;
import java.util.Optional;
import javax.annotation.Resource;
import javax.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;
import org.ramer.admin.system.entity.Constant.State;
import org.ramer.admin.system.entity.domain.common.*;
import org.ramer.admin.system.exception.CommonException;
import org.ramer.admin.system.repository.BaseRepository;
import org.ramer.admin.system.repository.common.MenuRepository;
import org.ramer.admin.system.service.common.MenuService;
import org.ramer.admin.system.service.common.PrivilegeService;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

/** @author ramer */
@Slf4j
@Service
public class MenuServiceImpl implements MenuService {
  @Resource private JPAQueryFactory jpaQueryFactory;
  @Resource private MenuRepository repository;
  @Resource private PrivilegeService privilegeService;

  @Override
  public List<Menu> listByManager(Long managerId) {
    final QMenu menu = QMenu.menu;
    final QRole role = QRole.role;
    final QManager manager = QManager.manager;
    return jpaQueryFactory
        .selectFrom(menu)
        .innerJoin(role)
        .on(role.menus.contains(menu))
        .innerJoin(manager)
        .on(manager.roles.contains(role).and(manager.id.eq(managerId)))
        .where(menu.state.eq(State.STATE_ON).and(role.state.eq(State.STATE_ON)))
        .orderBy(menu.sortWeight.asc())
        .distinct()
        .fetch();
  }

  @Transactional
  @Override
  public synchronized Menu update(Menu m) {
    return Optional.ofNullable(getById(m.getId()))
        .map(
            menu -> {
              textFilter(m, menu);
              menu.setHasChild(m.getHasChild());
              menu.setSortWeight(m.getSortWeight());
              menu.setParent(m.getParent());
              return repository.saveAndFlush(menu);
            })
        .orElse(null);
  }

  @Override
  public Specification<Menu> getSpec(String criteria) {
    return StringUtils.isEmpty(criteria)
        ? (root, query, builder) -> builder.and(builder.equal(root.get("state"), State.STATE_ON))
        : (root, query, builder) ->
            builder.and(
                builder.equal(root.get("state"), State.STATE_ON),
                builder.and(
                    builder.like(root.get("name"), "%" + criteria + "%"),
                    builder.like(root.get("remark"), "%" + criteria + "%")));
  }

  @SuppressWarnings({"unchecked"})
  @Override
  public <U extends BaseRepository<Menu, Long>> U getRepository() throws CommonException {
    return (U) repository;
  }
}
