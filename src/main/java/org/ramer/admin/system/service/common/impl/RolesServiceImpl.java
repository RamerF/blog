package org.ramer.admin.system.service.common.impl;

import java.util.*;
import java.util.stream.Collectors;
import javax.annotation.Resource;
import javax.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;
import org.ramer.admin.system.entity.Constant;
import org.ramer.admin.system.entity.domain.AbstractEntity;
import org.ramer.admin.system.entity.domain.common.*;
import org.ramer.admin.system.repository.common.RolesRepository;
import org.ramer.admin.system.service.common.RolesService;
import org.ramer.admin.system.util.TextUtil;
import org.springframework.data.domain.*;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

@Slf4j
@Service
public class RolesServiceImpl implements RolesService {
  @Resource private RolesRepository repository;

  @Transactional
  @Override
  public synchronized Roles create(Roles roles, List<Long> menuIds, List<Long> privilegeIds) {
    roles.setName(TextUtil.filter(roles.getName()));
    roles.setRemark(TextUtil.filter(roles.getRemark()));
    if (menuIds.size() > 0) {
      List<Menu> menus = new ArrayList<>();
      menuIds.forEach(menuId -> menus.add(Menu.of(menuId)));
      roles.setMenus(menus);
    }
    if (privilegeIds.size() > 0) {
      List<Privilege> privileges = new ArrayList<>();
      privilegeIds.forEach(privilegeId -> privileges.add(Privilege.of(privilegeId)));
      roles.setPrivileges(privileges);
    }
    return repository.saveAndFlush(roles);
  }

  @Transactional
  @Override
  public synchronized Roles update(Roles roles, List<Long> menuIds, List<Long> privilegeIds) {
    return repository
        .findById(roles.getId())
        .map(r -> create(roles, menuIds, privilegeIds))
        .orElse(null);
  }

  @Override
  public List<Roles> listByManager(long managerId) {
    return repository.findByManager(managerId, Constant.STATE_ON);
  }

  @Override
  public List<String> listNameByManager(long managerId) {
    return repository.findNameByManager(managerId, Constant.STATE_ON);
  }

  @Override
  public List<Long> listMenuIds(Roles roles) {
    return roles.getMenus().stream()
        .mapToLong(AbstractEntity::getId)
        .boxed()
        .collect(Collectors.toList());
  }

  @Transactional
  @Override
  public Roles create(Roles roles) {
    textFilter(roles, roles);
    return repository.saveAndFlush(roles);
  }

  @Override
  public long count() {
    return repository.count();
  }

  @Override
  public Roles getById(long id) {
    return repository.findById(id).orElse(null);
  }

  @Override
  public List<Roles> list(final String criteria) {
    return page(criteria, -1, -1).getContent();
  }

  @Override
  public Page<Roles> page(final String criteria, final int page, final int size) {
    final PageRequest pageable = pageRequest(page, size);
    return pageable == null
        ? new PageImpl<>(Collections.emptyList())
        : repository.findAll(getSpec(criteria), pageable);
  }

  @Transactional
  @Override
  public synchronized Roles update(Roles r) {
    return Optional.ofNullable(getById(r.getId()))
        .map(
            roles -> {
              textFilter(r, roles);
              return repository.saveAndFlush(roles);
            })
        .orElse(null);
  }

  @Transactional
  @Override
  public synchronized void delete(long id) {
    repository.deleteById(id);
  }

  @Override
  public void textFilter(Roles trans, Roles filtered) {
    filtered.setName(TextUtil.filter(trans.getName()));
    filtered.setRemark(TextUtil.filter(trans.getRemark()));
  }

  @Override
  public Specification<Roles> getSpec(String criteria) {
    return StringUtils.isEmpty(criteria)
        ? (root, query, builder) -> builder.and(builder.equal(root.get("state"), Constant.STATE_ON))
        : (root, query, builder) ->
            builder.and(
                builder.equal(root.get("state"), Constant.STATE_ON),
                builder.or(builder.like(root.get("name"), "%" + criteria + "%")));
  }
}
