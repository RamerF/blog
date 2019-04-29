package org.ramer.admin.system.service.common.impl;

import java.util.*;
import javax.annotation.Resource;
import javax.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;
import org.ramer.admin.system.entity.Constant;
import org.ramer.admin.system.entity.domain.common.Config;
import org.ramer.admin.system.repository.common.ConfigRepository;
import org.ramer.admin.system.service.common.ConfigService;
import org.ramer.admin.system.util.TextUtil;
import org.springframework.data.domain.*;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

/** @author ramer */
@Slf4j
@Service
public class ConfigServiceImpl implements ConfigService {
  @Resource private ConfigRepository repository;
  private final Map<String, String> SITE_INFO_MAP = new HashMap<>();

  @Override
  public String getSiteInfo(String location) {
    return Optional.ofNullable(SITE_INFO_MAP.get(location))
        .orElseGet(
            () -> {
              final Config siteInfo = getByCode(location);
              if (siteInfo == null) {
                return location;
              }
              SITE_INFO_MAP.put(location, siteInfo.getValue());
              return siteInfo.getValue();
            });
  }

  @Override
  public Config getByCode(String code) {
    return repository.findByCode(code);
  }

  @Transactional
  @Override
  public synchronized Config create(Config conf) {
    if (repository.findByCodeAndState(conf.getCode(), Constant.STATE_ON) != null) {
      return null;
    }
    textFilter(conf, conf);
    return repository.saveAndFlush(conf);
  }

  @Override
  public long count() {
    return repository.count();
  }

  @Override
  public Config getById(long id) {
    return repository.findById(id).orElse(null);
  }

  @Override
  public List<Config> list(final String criteria) {
    return page(criteria, -1, -1).getContent();
  }

  @Override
  public Page<Config> page(String criteria, int page, int size) {
    final PageRequest pageable = pageRequest(page, size);
    return pageable == null
        ? new PageImpl<>(Collections.emptyList())
        : repository.findAll(getSpec(criteria), pageable);
  }

  @Transactional
  @Override
  public synchronized Config update(Config conf) {
    return Optional.ofNullable(getById(conf.getId()))
        .map(
            config -> {
              textFilter(conf, config);
              return repository.saveAndFlush(config);
            })
        .orElse(null);
  }

  @Override
  public Config updateNoFilter(Config conf) {
    return Optional.ofNullable(getById(conf.getId()))
        .map(config -> repository.saveAndFlush(config))
        .orElse(null);
  }

  @Transactional
  @Override
  public synchronized void delete(long id) {
    repository.deleteById(id);
  }

  @Override
  public void textFilter(Config trans, Config filtered) {
    filtered.setValue(TextUtil.filter(trans.getValue()));
    filtered.setName(TextUtil.filter(trans.getName()));
    filtered.setRemark(TextUtil.filter(trans.getRemark()));
  }

  @Override
  public Specification<Config> getSpec(String criteria) {
    return StringUtils.isEmpty(criteria)
        ? (root, query, builder) -> builder.and(builder.equal(root.get("state"), Constant.STATE_ON))
        : (root, query, builder) ->
            builder.and(
                builder.equal(root.get("state"), Constant.STATE_ON),
                builder.or(
                    builder.like(root.get("code"), "%" + criteria + "%"),
                    builder.like(root.get("name"), "%" + criteria + "%")));
  }
}
