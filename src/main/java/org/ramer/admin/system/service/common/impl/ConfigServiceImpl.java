package org.ramer.admin.system.service.common.impl;

import java.util.Optional;
import javax.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.ramer.admin.system.entity.Constant.State;
import org.ramer.admin.system.entity.domain.common.Config;
import org.ramer.admin.system.exception.CommonException;
import org.ramer.admin.system.repository.BaseRepository;
import org.ramer.admin.system.repository.common.ConfigRepository;
import org.ramer.admin.system.service.common.ConfigService;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

/** @author ramer */
@Slf4j
@Service
public class ConfigServiceImpl implements ConfigService {
  @Resource private ConfigRepository repository;

  @Override
  public String getSiteInfo(String location) {
    return Optional.ofNullable(getByCode(location)).map(Config::getValue).orElse(location);
  }

  @Override
  public Config getByCode(String code) {
    return repository.findByCodeAndState(code, State.STATE_ON);
  }

  @Override
  public Specification<Config> getSpec(String criteria) {
    return StringUtils.isEmpty(criteria)
        ? (root, query, builder) -> builder.and(builder.equal(root.get("state"), State.STATE_ON))
        : (root, query, builder) ->
            builder.and(
                builder.equal(root.get("state"), State.STATE_ON),
                builder.or(
                    builder.like(root.get("code"), "%" + criteria + "%"),
                    builder.like(root.get("name"), "%" + criteria + "%")));
  }

  @SuppressWarnings({"unchecked"})
  @Override
  public <U extends BaseRepository<Config, Long>> U getRepository() throws CommonException {
    return (U) repository;
  }
}
