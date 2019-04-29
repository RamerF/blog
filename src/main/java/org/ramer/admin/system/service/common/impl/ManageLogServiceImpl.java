package org.ramer.admin.system.service.common.impl;

import javax.annotation.Resource;
import javax.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;
import org.ramer.admin.system.entity.Constant;
import org.ramer.admin.system.entity.domain.common.ManageLog;
import org.ramer.admin.system.exception.CommonException;
import org.ramer.admin.system.repository.BaseRepository;
import org.ramer.admin.system.repository.common.ManageLogRepository;
import org.ramer.admin.system.service.common.ManageLogService;
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
    throw new CommonException("ManageLogsServiceImpl.update : not allowed");
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

  @SuppressWarnings({"unchecked"})
  @Override
  public <U extends BaseRepository<ManageLog, Long>> U getRepository() throws CommonException {
    return (U) repository;
  }
}
