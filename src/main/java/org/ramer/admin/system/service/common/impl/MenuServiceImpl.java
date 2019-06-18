package org.ramer.admin.system.service.common.impl;

import com.querydsl.jpa.impl.JPAQueryFactory;
import java.util.*;
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
import org.ramer.admin.system.util.TextUtil;
import org.springframework.data.domain.*;
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
        .leftJoin(role)
        .on(role.menus.contains(menu))
        .leftJoin(manager)
        .on(manager.roles.contains(role).and(manager.id.eq(managerId)))
        .where(menu.state.eq(State.STATE_ON).and(role.state.eq(State.STATE_ON)))
        .orderBy(menu.sortWeight.asc())
        .fetch();
  }

  @Transactional
  @Override
  public Menu create(Menu menu) {
    textFilter(menu, menu);
    privilegeService.create(menu.getAlia(), menu.getRemark());
    return repository.saveAndFlush(menu);
  }

  @Override
  public long count() {
    return repository.count();
  }

  @Override
  public Menu getById(final long id) {
    return repository.findById(id).orElse(null);
  }

  @Override
  public List<Menu> list(final String criteria) {
    return page(criteria, -1, -1).getContent();
  }

  @Override
  public Page<Menu> page(final String criteria, final int page, final int size) {
    final PageRequest pageable = pageRequest(page, size);
    return pageable == null
        ? new PageImpl<>(Collections.emptyList())
        : repository.findAll(getSpec(criteria), pageable);
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

  @Transactional
  @Override
  public synchronized void delete(long id) {
    repository.deleteById(id);
  }

  @Override
  public void textFilter(Menu trans, Menu filtered) {
    filtered.setName(TextUtil.filter(trans.getName()));
    filtered.setRemark(TextUtil.filter(trans.getRemark()));
    if (!StringUtils.isEmpty(trans.getIcon())) {
      filtered.setIcon(TextUtil.filter(trans.getIcon()));
    }
    filtered.setUrl(TextUtil.filter(trans.getUrl()));
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
