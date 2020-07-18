package io.github.ramerf.blog.system.service.common.impl;

import java.util.*;
import java.util.stream.Collectors;
import javax.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import io.github.ramerf.blog.system.entity.domain.common.*;
import io.github.ramerf.blog.system.exception.CommonException;
import io.github.ramerf.blog.system.repository.BaseRepository;
import io.github.ramerf.blog.system.repository.common.PrivilegeRepository;
import io.github.ramerf.blog.system.service.common.*;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

/** @author ramer */
@Slf4j
@Service
public class PrivilegeServiceImpl implements PrivilegeService {
  @Resource private RoleService roleService;
  @Resource private ManagerService managerService;
  @Resource private PrivilegeRepository repository;

  @Override
  public Privilege getByExp(final String exp) {
    return repository.findTopByExpAndIsDelete(exp, Boolean.FALSE);
  }

  @Override
  public List<Privilege> listByManagerId(final long managerId) {
    final Manager manager = managerService.getById(managerId);
    if (Objects.isNull(manager)) {
      return Collections.emptyList();
    }
    final List<Role> roles = manager.getRoles();
    if (CollectionUtils.isEmpty(roles)) {
      return Collections.emptyList();
    }
    return roles.stream()
        .flatMap(role -> role.getPrivileges().stream())
        .collect(Collectors.toList());
  }

  @Override
  public List<Privilege> listByRoles(Long rolesId) {
    final Role role = roleService.getById(rolesId);
    if (Objects.isNull(role)) {
      return Collections.emptyList();
    }
    return role.getPrivileges();
  }

  @Override
  public Specification<Privilege> getSpec(String criteria) {
    return StringUtils.isEmpty(criteria)
        ? (root, query, builder) -> builder.and(builder.equal(root.get("isDelete"), Boolean.FALSE))
        : (root, query, builder) ->
            builder.and(
                builder.equal(root.get("isDelete"), Boolean.FALSE),
                builder.or(
                    builder.like(root.get("exp"), "%" + criteria + "%"),
                    builder.like(root.get("name"), "%" + criteria + "%")));
  }

  @SuppressWarnings({"unchecked"})
  @Override
  public <U extends BaseRepository<Privilege, Long>> U getRepository() throws CommonException {
    return (U) repository;
  }
}
