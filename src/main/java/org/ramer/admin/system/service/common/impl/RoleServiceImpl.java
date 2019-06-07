package org.ramer.admin.system.service.common.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import javax.annotation.Resource;
import javax.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;
import org.ramer.admin.system.entity.Constant.State;
import org.ramer.admin.system.entity.domain.AbstractEntity;
import org.ramer.admin.system.entity.domain.common.*;
import org.ramer.admin.system.exception.CommonException;
import org.ramer.admin.system.repository.BaseRepository;
import org.ramer.admin.system.repository.common.RoleRepository;
import org.ramer.admin.system.service.common.RoleService;
import org.ramer.admin.system.util.TextUtil;
import org.springframework.stereotype.Service;

/** @author ramer */
@Slf4j
@Service
public class RoleServiceImpl implements RoleService {
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
  public List<Role> listByManager(long managerId) {
    return repository.findByManager(managerId, State.STATE_ON);
  }

  @Override
  public List<String> listNameByManager(long managerId) {
    return repository.findNameByManager(managerId, State.STATE_ON);
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
