package org.ramer.admin.system.service.common.impl;

import com.querydsl.jpa.impl.JPAQueryFactory;
import java.util.*;
import javax.annotation.Resource;
import javax.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;
import org.ramer.admin.system.entity.Constant.State;
import org.ramer.admin.system.entity.domain.AbstractEntity;
import org.ramer.admin.system.entity.domain.common.*;
import org.ramer.admin.system.exception.CommonException;
import org.ramer.admin.system.repository.BaseRepository;
import org.ramer.admin.system.repository.common.MenuRepository;
import org.ramer.admin.system.service.common.MenuService;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

/** @author ramer */
@Slf4j
@Service
public class MenuServiceImpl implements MenuService {
  @Resource private JPAQueryFactory jpaQueryFactory;
  @Resource private MenuRepository repository;

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
  public Menu create(final Menu menu) {
    textFilter(menu, menu);
    // 更新父菜单属性
    if (Objects.nonNull(menu.getParentId())) {
      final Menu parent = getById(menu.getParentId());
      parent.setHasChild(true);
    }
    menu.setHasChild(false);
    return repository.saveAndFlush(menu);
  }

  @Transactional
  @Override
  public synchronized Menu update(Menu menu) {
    textFilter(menu, menu);
    // 更新了父菜单,同步父菜单属性
    final Long parentId =
        Optional.ofNullable(menu.getParent()).map(AbstractEntity::getId).orElse(null);
    if (!Objects.equals(parentId, menu.getParentId())) {
      // 更新原有父菜单
      if (Objects.nonNull(parentId)
          && repository.findByParentIdAndState(parentId, State.STATE_ON).size() < 2) {
        menu.getParent().setHasChild(false);
      }
      // 更新当前父菜单
      if (Objects.nonNull(menu.getParentId())
          && CollectionUtils.isEmpty(
              repository.findByParentIdAndState(menu.getParentId(), State.STATE_ON))) {
        getById(menu.getParentId()).setHasChild(true);
      }
    }
    return repository.saveAndFlush(menu);
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
