package io.github.ramerf.blog.system.service.common.impl;

import java.util.*;
import java.util.stream.Collectors;
import javax.annotation.Resource;
import javax.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;
import io.github.ramerf.blog.system.entity.domain.AbstractEntity;
import io.github.ramerf.blog.system.entity.domain.common.*;
import io.github.ramerf.blog.system.exception.CommonException;
import io.github.ramerf.blog.system.repository.BaseRepository;
import io.github.ramerf.blog.system.repository.common.RoleRepository;
import io.github.ramerf.blog.system.service.common.ManagerService;
import io.github.ramerf.blog.system.service.common.RoleService;
import io.github.ramerf.blog.system.util.TextUtil;
import org.springframework.stereotype.Service;

/** @author ramer */
@Slf4j
@Service
public class RoleServiceImpl implements RoleService {
  @Resource private ManagerService managerService;
  @Resource private RoleRepository repository;

  @Transactional
  @Override
  public synchronized Role create(Role roles, List<Long> menuIds, List<Long> privilegeIds) {
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

  @Override
  public Role getByName(final String name) {
    return repository.findByNameAndIsDelete(name, Boolean.FALSE);
  }

  @Override
  public List<Role> listByManager(long managerId) {
    final Manager manager = managerService.getById(managerId);
    if (Objects.isNull(manager)) {
      return Collections.emptyList();
    }
    return manager.getRoles();
  }

  @Override
  public List<Long> listMenuIds(Role roles) {
    return roles.getMenus().stream().map(AbstractEntity::getId).collect(Collectors.toList());
  }

  @Transactional
  @Override
  public synchronized Role update(Role roles, List<Long> menuIds, List<Long> privilegeIds) {
    return repository
        .findById(roles.getId())
        .map(r -> create(roles, menuIds, privilegeIds))
        .orElse(null);
  }

  @Override
  public void textFilter(Role trans, Role filtered) {
    filtered.setName(TextUtil.filter(trans.getName()));
    filtered.setRemark(TextUtil.filter(trans.getRemark()));
  }

  @SuppressWarnings({"unchecked"})
  @Override
  public <U extends BaseRepository<Role, Long>> U getRepository() throws CommonException {
    return (U) repository;
  }
}
