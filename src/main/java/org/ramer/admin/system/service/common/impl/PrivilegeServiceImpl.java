package org.ramer.admin.system.service.common.impl;

import org.ramer.admin.system.entity.Constant;
import org.ramer.admin.system.entity.Constant.PrivilegeEnum;
import org.ramer.admin.system.entity.domain.common.Privilege;
import org.ramer.admin.system.exception.CommonException;
import org.ramer.admin.system.repository.common.PrivilegeRepository;
import org.ramer.admin.system.service.common.PrivilegeService;
import java.util.*;
import javax.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.*;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

/** @author ramer */
@Slf4j
@Service
public class PrivilegeServiceImpl implements PrivilegeService {
  @Resource private PrivilegeRepository repository;

  @Transactional
  @Override
  public List<Privilege> create(final String expPrefix, final String remark) {
    List<Privilege> privileges = new ArrayList<>();
    PrivilegeEnum.map()
        .forEach(
            (name, remark2) -> {
              final Privilege p = new Privilege();
              p.setExp(expPrefix + ":" + name);
              p.setRemark(remark + ":" + remark2);
              privileges.add(create(p));
            });
    return privileges;
  }

  @Override
  public List<Privilege> listByManagerId(long menuId) {
    return repository.findByManager(menuId, Constant.STATE_ON);
  }

  @Transactional
  @Override
  public synchronized void saveBatch(List<Privilege> privileges) {
    repository.saveAll(privileges);
  }

  @Override
  public List<Privilege> listByRoles(Long rolesId) {
    return repository.findByRoles(rolesId, Constant.STATE_ON);
  }

  @Transactional
  @Override
  public Privilege create(Privilege privilege) {
    return repository.save(privilege);
  }

  @Override
  public long count() {
    return repository.count();
  }

  @Override
  public Privilege getById(final long id) {
    return repository.findById(id).orElse(null);
  }

  @Override
  public List<Privilege> list(final String criteria) {
    return page(criteria, -1, -1).getContent();
  }

  @Override
  public Page<Privilege> page(final String criteria, final int page, final int size) {
    final PageRequest pageable = pageRequest(page, size);
    return pageable == null
        ? new PageImpl<>(Collections.emptyList())
        : repository.findAll(getSpec(criteria), pageable);
  }

  @Transactional
  @Override
  public synchronized Privilege update(Privilege privilege) {
    log.error(" PrivilegeServiceImpl.update : not yet implements");
    throw new CommonException("PrivilegeServiceImpl.update : not yet implements");
  }

  @Transactional
  @Override
  public synchronized void delete(long id) {
    repository.deleteById(id);
  }

  @Override
  public Specification<Privilege> getSpec(String criteria) {
    return StringUtils.isEmpty(criteria)
        ? (root, query, builder) -> builder.and(builder.equal(root.get("state"), Constant.STATE_ON))
        : (root, query, builder) ->
            builder.and(
                builder.equal(root.get("state"), Constant.STATE_ON),
                builder.or(
                    builder.like(root.get("exp"), "%" + criteria + "%"),
                    builder.like(root.get("remark"), "%" + criteria + "%")));
  }
}
