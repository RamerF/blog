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
import io.github.ramerf.blog.system.repository.common.MenuRepository;
import io.github.ramerf.blog.system.service.common.ManagerService;
import io.github.ramerf.blog.system.service.common.MenuService;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

/** @author ramer */
@Slf4j
@Service
public class MenuServiceImpl implements MenuService {
  @Resource private ManagerService managerService;
  @Resource private MenuRepository repository;

  @Override
  public List<Menu> listByManager(Long managerId) {
    final Manager manager = managerService.getById(managerId);
    if (Objects.isNull(manager)) {
      return Collections.emptyList();
    }
    final List<Role> roles = manager.getRoles();
    if (CollectionUtils.isEmpty(roles)) {
      return Collections.emptyList();
    }
    return roles.stream().flatMap(role -> role.getMenus().stream()).collect(Collectors.toList());
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
          && repository.findByParentIdAndIsDelete(parentId, Boolean.FALSE).size() < 2) {
        menu.getParent().setHasChild(false);
      }
      // 更新当前父菜单
      if (Objects.nonNull(menu.getParentId())
          && CollectionUtils.isEmpty(
              repository.findByParentIdAndIsDelete(menu.getParentId(), Boolean.FALSE))) {
        getById(menu.getParentId()).setHasChild(true);
      }
    }
    return repository.saveAndFlush(menu);
  }

  @Override
  public Specification<Menu> getSpec(String criteria) {
    return StringUtils.isEmpty(criteria)
        ? (root, query, builder) -> builder.and(builder.equal(root.get("isDelete"), Boolean.FALSE))
        : (root, query, builder) ->
            builder.and(
                builder.equal(root.get("isDelete"), Boolean.FALSE),
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
