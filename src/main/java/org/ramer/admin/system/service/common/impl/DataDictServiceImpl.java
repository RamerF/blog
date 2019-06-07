package org.ramer.admin.system.service.common.impl;

import java.util.Collections;
import java.util.List;
import javax.annotation.Resource;
import javax.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;
import org.ramer.admin.system.entity.Constant.State;
import org.ramer.admin.system.entity.domain.common.DataDict;
import org.ramer.admin.system.entity.domain.common.DataDictType;
import org.ramer.admin.system.exception.CommonException;
import org.ramer.admin.system.repository.BaseRepository;
import org.ramer.admin.system.repository.common.DataDictRepository;
import org.ramer.admin.system.repository.common.DataDictTypeRepository;
import org.ramer.admin.system.service.common.DataDictService;
import org.ramer.admin.system.util.TextUtil;
import org.springframework.data.domain.*;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

/** @author ramer */
@Slf4j
@Service
public class DataDictServiceImpl implements DataDictService {
  @Resource private DataDictTypeRepository typeRepository;
  @Resource private DataDictRepository repository;

  @Override
  public List<DataDictType> listType() {
    return typeRepository.findAll();
  }

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
  public Page<DataDict> pageByTypeCode(String typeCode, String criteria, int page, int size) {
    if (page < 1 || size < 0) {
      return new PageImpl<>(Collections.emptyList());
    }
    return StringUtils.isEmpty(criteria)
        ? repository.findByTypeCodeAndState(
            typeCode, State.STATE_ON, PageRequest.of(page - 1, size))
        : repository.findByTypeCodeAndState(
            typeCode, "%" + criteria + "%", State.STATE_ON, PageRequest.of(page - 1, size));
  }

  @Transactional
  @Override
  public DataDict create(DataDict dataDict, String typeCode) {
    DataDict dict = new DataDict();
    textFilter(dataDict, dict);
    log.info(" DataDictServiceImpl.create : [{}]", typeCode);
    final DataDictType dataDictType = typeRepository.findByCode(typeCode);
    if (dataDictType == null) {
      throw new CommonException("参数[类型]不正确");
    }
    dict.setDataDictType(dataDictType);
    return repository.saveAndFlush(dict);
  }

  @Override
  public void textFilter(DataDict trans, DataDict filtered) {
    filtered.setName(TextUtil.filter(trans.getName()));
    filtered.setRemark(TextUtil.filter(trans.getRemark()));
    if (!StringUtils.isEmpty(trans.getCode())) {
      filtered.setCode(TextUtil.filter(trans.getCode()));
    }
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
