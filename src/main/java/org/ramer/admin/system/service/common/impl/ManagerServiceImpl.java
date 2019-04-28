package org.ramer.admin.system.service.common.impl;

import org.ramer.admin.system.entity.Constant;
import org.ramer.admin.system.entity.domain.AbstractEntity;
import org.ramer.admin.system.entity.domain.common.Manager;
import org.ramer.admin.system.entity.domain.common.Roles;
import org.ramer.admin.system.repository.common.ManagerRepository;
import org.ramer.admin.system.service.common.ManagerService;
import org.ramer.admin.util.EncryptUtil;
import org.ramer.admin.util.TextUtil;
import java.util.*;
import java.util.stream.Collectors;
import javax.annotation.Resource;
import javax.transaction.Transactional;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.*;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

@Service
@Slf4j
public class ManagerServiceImpl implements ManagerService {
  @Resource private ManagerRepository repository;
  private static Map<String, ManagerLogin> LOGIN_STATUS_MAP = new HashMap<>();
  /** 用户端登录信息.key: empNo,value: uuid */
  private static Map<String, String> USER_LOGIN_MAP = new HashMap<>();

  @Transactional
  @Override
  public synchronized Manager save(Manager manager, List<Long> roleIds) {
    if (roleIds != null && roleIds.size() > 0) {
      List<Roles> roles = new ArrayList<>();
      roleIds.forEach(roleId -> roles.add(Roles.of(roleId)));
      manager.setRoleses(roles);
      manager.setPassword(EncryptUtil.execEncrypt(manager.getPassword()));
    }
    return create(manager);
  }

  @Override
  public List<Manager> listAfterDate(final Date updateTime) {
    return repository.findByUpdateTimeGreaterThan(updateTime);
  }

  @Override
  public Manager getByEmpNo(String empNo) {
    return repository.findByEmpNoAndState(empNo, Constant.STATE_ON);
  }

  @Transactional
  @Override
  public synchronized Manager update(Manager manager, List<Long> roleIds) {
    Manager m = repository.findByIdAndState(manager.getId(), Constant.STATE_ON);
    if (m == null) {
      return null;
    }
    if (!StringUtils.isEmpty(manager.getPassword())) {
      m.setPassword(manager.getPassword());
    }
    if (!StringUtils.isEmpty(manager.getPhone())) {
      m.setPhone(manager.getPhone());
    }
    textFilter(manager, m);
    if (manager.getActive() != null
        && (manager.getActive() == Constant.ACTIVE_TRUE
            || manager.getActive() == Constant.ACTIVE_FALSE)) {
      m.setActive(manager.getActive());
    }
    if (Constant.Gender.ordinalList().contains(manager.getGender())) {
      m.setGender(manager.getGender());
    }
    if (roleIds != null && roleIds.size() > 0) {
      List<Roles> roles = new ArrayList<>();
      roleIds.forEach(roleId -> roles.add(Roles.of(roleId)));
      m.setRoleses(roles);
    }
    return repository.saveAndFlush(m);
  }

  @Transactional
  @Override
  public synchronized int updatePassword(Long id, String old, String password) {
    Optional<Manager> optionalManager = repository.findById(id);
    if (optionalManager.isPresent()) {
      Manager person = optionalManager.get();
      if (!person.getPassword().equals(old)) {
        return -1;
      }
      person.setPassword(EncryptUtil.execEncrypt(password));
      Manager manager = repository.saveAndFlush(person);
      return manager != null && manager.getId() > 0 ? 1 : 0;
    } else {
      return -2;
    }
  }

  @Override
  public synchronized ManagerLogin getLoginStatus(String empNo) {
    final ManagerLogin managerLogin = LOGIN_STATUS_MAP.get(empNo);
    if (managerLogin != null
        && System.currentTimeMillis() - managerLogin.getDuring() / (24 * 60 * 60 * 1000) >= 1) {
      // 一天后重置
      managerLogin.setDuring(System.currentTimeMillis());
      managerLogin.setCount(0);
    }
    return managerLogin;
  }

  @Override
  public synchronized void setLoginStatus(String empNo) {
    Optional.ofNullable(LOGIN_STATUS_MAP.get(empNo))
        .map(
            s -> {
              s.setCount(s.getCount() + 1);
              LOGIN_STATUS_MAP.put(empNo, s);
              return LOGIN_STATUS_MAP;
            })
        .orElseGet(
            () -> {
              ManagerLogin login = new ManagerLogin();
              login.setCount(1);
              login.setDuring(System.currentTimeMillis());
              LOGIN_STATUS_MAP.put(empNo, login);
              return LOGIN_STATUS_MAP;
            });
  }

  @Override
  public void setUserLoginMap(String empNo, String uuid) {
    USER_LOGIN_MAP.put(empNo, uuid);
  }

  @Override
  public String userAutoLogin(String empNo, String uuid) {
    return Optional.ofNullable(USER_LOGIN_MAP.get(empNo))
        .map(s -> s.equals(uuid) ? uuid : null)
        .orElseGet(
            () -> {
              USER_LOGIN_MAP.put(empNo, uuid);
              return uuid;
            });
  }

  @Transactional
  @Override
  public Manager create(Manager manager) {
    textFilter(manager, manager);
    if (manager.getActive() == null) {
      manager.setActive(Constant.ACTIVE_FALSE);
    }
    if (manager.getState() == null) {
      manager.setState(Constant.STATE_OFF);
    }
    return repository.saveAndFlush(manager);
  }

  @Override
  public long count() {
    return repository.count();
  }

  @Override
  public Manager getById(long id) {
    return repository.findById(id).orElse(null);
  }

  @Override
  public List<Manager> list(final String criteria) {
    return page(criteria, -1, -1).getContent();
  }

  @Override
  public Page<Manager> page(final String criteria, final int page, final int size) {
    final PageRequest pageable = pageRequest(page, size);
    return pageable == null
        ? new PageImpl<>(Collections.emptyList())
        : repository.findAll(getSpec(criteria), pageable);
  }

  @Transactional
  @Override
  public synchronized Manager update(Manager manager) {
    return update(
        manager,
        Optional.ofNullable(manager.getRoleses()).orElseGet(ArrayList::new).stream()
            .mapToLong(AbstractEntity::getId)
            .boxed()
            .collect(Collectors.toList()));
  }

  @Transactional
  @Override
  public synchronized void delete(long id) {
    repository.deleteById(id);
  }

  @Override
  public void textFilter(Manager trans, Manager filtered) {
    if (!StringUtils.isEmpty(trans.getEmpNo())) {
      filtered.setEmpNo(TextUtil.filter(trans.getEmpNo()));
    }
    if (!StringUtils.isEmpty(trans.getName())) {
      filtered.setName(TextUtil.filter(trans.getName()));
    }
  }

  @Override
  public Specification<Manager> getSpec(String criteria) {
    return StringUtils.isEmpty(criteria)
        ? (root, query, builder) -> builder.and(builder.equal(root.get("state"), Constant.STATE_ON))
        : (root, query, builder) ->
            builder.and(
                builder.equal(root.get("state"), Constant.STATE_ON),
                builder.or(
                    builder.like(root.get("empNo"), "%" + criteria + "%"),
                    builder.like(root.get("name"), "%" + criteria + "%"),
                    builder.like(root.get("phone"), "%" + criteria + "%")));
  }

  @Data
  public static class ManagerLogin {
    private Integer count;
    private long during;
  }
}
