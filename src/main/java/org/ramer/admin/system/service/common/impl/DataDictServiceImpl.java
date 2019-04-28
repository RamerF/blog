package org.ramer.admin.system.service.common.impl;

import org.ramer.admin.system.entity.Constant;
import org.ramer.admin.system.entity.domain.common.DataDict;
import org.ramer.admin.system.entity.domain.common.DataDictType;
import org.ramer.admin.system.exception.CommonException;
import org.ramer.admin.system.repository.common.DataDictRepository;
import org.ramer.admin.system.repository.common.DataDictTypeRepository;
import org.ramer.admin.system.service.common.DataDictService;
import org.ramer.admin.util.TextUtil;
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
public class DataDictServiceImpl implements DataDictService {
  @Resource private DataDictTypeRepository dictTypeRepository;
  @Resource private DataDictRepository repository;

  @Override
  public List<DataDictType> listType() {
    return dictTypeRepository.findAll();
  }

  @Override
  public DataDict getByCode(final String code) {
    return repository.findByCodeAndState(code, Constant.STATE_ON);
  }

  @Override
  public DataDict getByTypeCodeAndCode(String typeCode, String code) {
    //    return repository.findByDataDictTypeCodeAndCodeAndState(
    //        dictTypeRepository.findByCode(typeCode), code, Constant.STATE_ON);
    return repository.findByDataDictTypeCodeAndCodeAndState(typeCode, code, Constant.STATE_ON);
  }

  @Override
  public List<DataDict> listByTypeCode(final String typeCode, final String criteria) {
    return repository.findByDataDictTypeCodeAndState(typeCode, Constant.STATE_ON);
  }

  @Override
  public Page<DataDict> pageByTypeCode(String typeCode, String criteria, int page, int size) {
    if (page < 1 || size < 0) {
      return new PageImpl<>(Collections.emptyList());
    }
    return StringUtils.isEmpty(criteria)
        ? repository.findByTypeCodeAndState(
            typeCode, Constant.STATE_ON, PageRequest.of(page - 1, size))
        : repository.findByTypeCodeAndState(
            typeCode, "%" + criteria + "%", Constant.STATE_ON, PageRequest.of(page - 1, size));
  }

  @Transactional
  @Override
  public DataDict create(DataDict dataDict, String typeCode) {
    DataDict dict = new DataDict();
    textFilter(dataDict, dict);
    log.info(" DataDictServiceImpl.create : [{}]", typeCode);
    final DataDictType dataDictType = dictTypeRepository.findByCode(typeCode);
    if (dataDictType == null) {
      throw new CommonException("参数typeCode不正确");
    }
    dict.setDataDictType(dataDictType);
    return repository.saveAndFlush(dict);
  }

  @Transactional
  @Override
  public synchronized DataDict create(DataDict dataDict) {
    return create(dataDict, null);
  }

  @Override
  public long count() {
    return repository.count();
  }

  @Override
  public DataDict getById(final long id) {
    return repository.findById(id).orElse(null);
  }

  @Override
  public List<DataDict> list(final String criteria) {
    return page(criteria, -1, -1).getContent();
  }

  @Override
  public Page<DataDict> page(final String criteria, final int page, final int size) {
    final PageRequest pageable = pageRequest(page, size);
    return pageable == null
        ? new PageImpl<>(Collections.emptyList())
        : repository.findAll(getSpec(criteria), pageable);
  }

  @Transactional
  @Override
  public synchronized DataDict update(DataDict dataDict) {
    return repository
        .findById(dataDict.getId())
        .map(
            dict -> {
              textFilter(dataDict, dict);
              return repository.saveAndFlush(dict);
            })
        .orElse(null);
  }

  @Transactional
  @Override
  public synchronized void delete(long dicId) {
    repository.deleteById(dicId);
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
        ? (root, query, builder) -> builder.and(builder.equal(root.get("state"), Constant.STATE_ON))
        : (root, query, builder) ->
            builder.and(
                builder.equal(root.get("state"), Constant.STATE_ON),
                builder.or(
                    builder.like(root.get("name"), "%" + criteria + "%"),
                    builder.like(root.get("code"), "%" + criteria + "%"),
                    builder.like(root.get("remark"), "%" + criteria + "%")));
  }
}
