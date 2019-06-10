package org.ramer.admin.system.service.common.impl;

import java.util.*;
import javax.annotation.Resource;
import javax.transaction.Transactional;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.ramer.admin.system.entity.Constant.State;
import org.ramer.admin.system.entity.domain.common.Manager;
import org.ramer.admin.system.exception.CommonException;
import org.ramer.admin.system.repository.BaseRepository;
import org.ramer.admin.system.repository.common.ManagerRepository;
import org.ramer.admin.system.service.common.ManagerService;
import org.ramer.admin.system.util.EncryptUtil;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

/** @author ramer */
@Slf4j
@Service
public class ManagerServiceImpl implements ManagerService {
  @Resource private ManagerRepository repository;

  private static Map<String, ManagerLogin> LOGIN_STATUS_MAP = new HashMap<>();
  /** 用户端登录信息.key: empNo,value: uuid */
  private static Map<String, String> USER_LOGIN_MAP = new HashMap<>();

  @Override
  public Manager getByEmpNo(String empNo) {
    return repository.findByEmpNoAndState(empNo, State.STATE_ON);
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
      return manager.getId() > 0 ? 1 : 0;
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
    if (manager.getIsActive() == null) {
      manager.setIsActive(false);
    }
    if (manager.getState() == null) {
      manager.setState(State.STATE_OFF);
    }
    return repository.saveAndFlush(manager);
  }

  @Override
  public Specification<Manager> getSpec(String criteria) {
    return StringUtils.isEmpty(criteria)
        ? (root, query, builder) -> builder.and(builder.equal(root.get("state"), State.STATE_ON))
        : (root, query, builder) ->
            builder.and(
                builder.equal(root.get("state"), State.STATE_ON),
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

  @SuppressWarnings({"unchecked"})
  @Override
  public <U extends BaseRepository<Manager, Long>> U getRepository() throws CommonException {
    return (U) repository;
  }
}
