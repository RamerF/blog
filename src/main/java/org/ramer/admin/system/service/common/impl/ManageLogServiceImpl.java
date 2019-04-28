package org.ramer.admin.system.service.common.impl;

import org.ramer.admin.system.entity.Constant;
import org.ramer.admin.system.entity.domain.common.ManageLog;
import org.ramer.admin.system.exception.CommonException;
import org.ramer.admin.system.repository.common.ManageLogsRepository;
import org.ramer.admin.system.service.common.ManageLogService;
import java.util.Collections;
import java.util.List;
import javax.annotation.Resource;
import javax.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.*;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

/** @author ramer */
@Slf4j
@Service
public class ManageLogServiceImpl implements ManageLogService {
  @Resource private ManageLogsRepository repository;

  @Transactional
  @Override
  public synchronized ManageLog create(ManageLog manageLogs) {
    return repository.saveAndFlush(manageLogs);
  }

  @Override
  public long count() {
    return repository.count();
  }

  @Override
  public ManageLog getById(long id) {
    return repository.findById(id).orElse(null);
  }

  @Override
  public List<ManageLog> list(final String criteria) {
    return page(criteria, -1, -1).getContent();
  }

  @Override
  public Page<ManageLog> page(String criteria, int page, int size) {
    final PageRequest pageable = pageRequest(page, size);
    return pageable == null
        ? new PageImpl<>(Collections.emptyList())
        : repository.findAll(getSpec(criteria), pageable);
  }

  @Transactional
  @Override
  public synchronized ManageLog update(ManageLog manageLogs) {
    log.warn(" ManageLogsServiceImpl.update : not allowed");
    return null;
  }

  @Transactional
  @Override
  public synchronized void delete(long id) {
    log.error(" ManageLogsServiceImpl.delete : not allowed");
    throw new CommonException("ManageLogsServiceImpl.delete : not allowed");
  }

  @Override
  public Specification<ManageLog> getSpec(String criteria) {
    return StringUtils.isEmpty(criteria)
        ? (root, query, builder) -> builder.and(builder.equal(root.get("state"), Constant.STATE_ON))
        : (root, query, builder) ->
            builder.and(
                builder.equal(root.get("state"), Constant.STATE_ON),
                builder.or(
                    builder.like(root.get("url"), "%" + criteria + "%"),
                    builder.like(root.get("result"), "%" + criteria + "%")));
  }
}
