package io.github.ramerf.blog.system.service.common.impl;

import java.util.Optional;
import javax.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import io.github.ramerf.blog.system.entity.domain.common.Config;
import io.github.ramerf.blog.system.exception.CommonException;
import io.github.ramerf.blog.system.repository.BaseRepository;
import io.github.ramerf.blog.system.repository.common.ConfigRepository;
import io.github.ramerf.blog.system.service.common.ConfigService;
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
    return repository.findByCodeAndIsDelete(code, Boolean.FALSE);
  }

  @Override
  public Specification<Config> getSpec(String criteria) {
    return StringUtils.isEmpty(criteria)
        ? (root, query, builder) -> builder.and(builder.equal(root.get("isDelete"), Boolean.FALSE))
        : (root, query, builder) ->
            builder.and(
                builder.equal(root.get("isDelete"), Boolean.FALSE),
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
