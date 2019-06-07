package org.ramer.admin.system.service.thirdparty.impl;

import javax.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.ramer.admin.system.entity.Constant.State;
import org.ramer.admin.system.entity.domain.thirdparty.ThirdPartyCertificate;
import org.ramer.admin.system.entity.domain.thirdparty.ThirdPartyLog;
import org.ramer.admin.system.repository.thirdparty.ThirdPartyCertRepository;
import org.ramer.admin.system.repository.thirdparty.ThirdPartyLogRepository;
import org.ramer.admin.system.service.thirdparty.ThirdPartyCertService;
import org.ramer.admin.system.util.TextUtil;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

/** @author ramer */
@Service
@Slf4j
public class ThirdPartyCertServiceImpl implements ThirdPartyCertService {
  @Resource private ThirdPartyCertRepository repository;
  @Resource private ThirdPartyLogRepository logRepository;

  @Transactional
  @Override
  public ThirdPartyLog saveLog(ThirdPartyLog log) {
    return logRepository.saveAndFlush(log);
  }

  @Transactional
  @Override
  public synchronized ThirdPartyCertificate save(
      String code, String secret, String name, String remark) {
    ThirdPartyCertificate thirdPartyCertificate = new ThirdPartyCertificate();
    thirdPartyCertificate.setName(TextUtil.filter(name));
    thirdPartyCertificate.setRemark(TextUtil.filter(remark));
    thirdPartyCertificate.setCode(TextUtil.filter(code));
    thirdPartyCertificate.setSecret(TextUtil.filter(secret));
    return repository.saveAndFlush(thirdPartyCertificate);
  }

  @Override
  public ThirdPartyCertificate findByCode(String code) {
    return repository.findByCode(code);
  }

  @Override
  public Page<ThirdPartyCertificate> page(String name, Integer page, Integer size) {
    final PageRequest pageable = PageRequest.of(page - 1, size);
    return StringUtils.isEmpty(name)
        ? repository.findAll(pageable)
        : repository.findAll(
            (root, query, builder) ->
                builder.and(
                    builder.equal(root.get("state"), State.STATE_ON),
                    builder.like(root.get("name"), "%" + name + "%")),
            pageable);
  }

  @Transactional
  @Override
  public synchronized ThirdPartyCertificate update(
      Long id, String code, String secret, String name, String remark) {
    return repository
        .findById(id)
        .map(
            thirdPartyCertificate -> {
              thirdPartyCertificate.setName(TextUtil.filter(name));
              thirdPartyCertificate.setRemark(TextUtil.filter(remark));
              thirdPartyCertificate.setCode(TextUtil.filter(code));
              thirdPartyCertificate.setSecret(TextUtil.filter(secret));
              return repository.saveAndFlush(thirdPartyCertificate);
            })
        .orElse(null);
  }

  @Transactional
  @Override
  public synchronized void delete(Long id) {
    repository.deleteById(id);
  }
}
