package io.github.ramerf.blog.system.service.common.impl;

import javax.annotation.Resource;
import javax.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;
import io.github.ramerf.blog.system.entity.domain.common.ManageLog;
import io.github.ramerf.blog.system.exception.CommonException;
import io.github.ramerf.blog.system.repository.BaseRepository;
import io.github.ramerf.blog.system.repository.common.ManageLogRepository;
import io.github.ramerf.blog.system.service.common.ManageLogService;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

/** @author ramer */
@Slf4j
@Service
public class ManageLogServiceImpl implements ManageLogService {
  @Resource private ManageLogRepository repository;

  @Transactional
  @Override
  public synchronized ManageLog update(ManageLog manageLogs) {
    log.warn(" ManageLogsServiceImpl.update : not allowed");
    throw CommonException.of("ManageLogsServiceImpl.update : not allowed");
  }

  @Transactional
  @Override
  public synchronized void delete(long id) {
    log.error(" ManageLogsServiceImpl.delete : not allowed");
    throw CommonException.of("ManageLogsServiceImpl.delete : not allowed");
  }

  @Override
  public Specification<ManageLog> getSpec(String criteria) {
    return StringUtils.isEmpty(criteria)
        ? (root, query, builder) -> builder.and(builder.equal(root.get("isDelete"), Boolean.FALSE))
        : (root, query, builder) ->
            builder.and(
                builder.equal(root.get("isDelete"), Boolean.FALSE),
                builder.or(
                    builder.like(root.get("url"), "%" + criteria + "%"),
                    builder.like(root.get("result"), "%" + criteria + "%")));
  }

  @SuppressWarnings({"unchecked"})
  @Override
  public <U extends BaseRepository<ManageLog, Long>> U getRepository() throws CommonException {
    return (U) repository;
  }
}
