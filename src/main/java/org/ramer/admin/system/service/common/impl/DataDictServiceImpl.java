package org.ramer.admin.system.service.common.impl;

import java.util.Collections;
import java.util.List;
import javax.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.ramer.admin.system.entity.Constant.State;
import org.ramer.admin.system.entity.domain.common.DataDict;
import org.ramer.admin.system.exception.CommonException;
import org.ramer.admin.system.repository.BaseRepository;
import org.ramer.admin.system.repository.common.DataDictRepository;
import org.ramer.admin.system.service.common.DataDictService;
import org.springframework.data.domain.*;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

/** @author ramer */
@Slf4j
@Service
public class DataDictServiceImpl implements DataDictService {
  @Resource private DataDictRepository repository;

  @Override
  public DataDict getByCode(final String code) {
    return repository.findByCodeAndState(code, State.STATE_ON);
  }

  @Override
  public DataDict getByTypeCodeAndCode(String typeCode, String code) {
    return repository.findByDataDictTypeCodeAndCodeAndState(typeCode, code, State.STATE_ON);
  }

  @Override
  public List<DataDict> listByTypeCode(final String typeCode, final String criteria) {
    return repository.findByDataDictTypeCodeAndState(typeCode, State.STATE_ON);
  }

  @Override
  public Page<DataDict> page(String typeCode, String criteria, int page, int size) {
    if (page < 1 || size < 0) {
      return new PageImpl<>(Collections.emptyList());
    }
    return StringUtils.isEmpty(criteria)
        ? repository.findByTypeCodeAndState(
            typeCode, State.STATE_ON, PageRequest.of(page - 1, size))
        : repository.findByTypeCodeAndState(
            typeCode, "%" + criteria + "%", State.STATE_ON, PageRequest.of(page - 1, size));
  }

  @Override
  public Specification<DataDict> getSpec(String criteria) {
    return StringUtils.isEmpty(criteria)
        ? (root, query, builder) -> builder.and(builder.equal(root.get("state"), State.STATE_ON))
        : (root, query, builder) ->
            builder.and(
                builder.equal(root.get("state"), State.STATE_ON),
                builder.or(
                    builder.like(root.get("name"), "%" + criteria + "%"),
                    builder.like(root.get("code"), "%" + criteria + "%"),
                    builder.like(root.get("remark"), "%" + criteria + "%")));
  }

  @SuppressWarnings({"unchecked"})
  @Override
  public <U extends BaseRepository<DataDict, Long>> U getRepository() throws CommonException {
    return (U) repository;
  }
}
