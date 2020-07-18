package io.github.ramerf.blog.system.service.common.impl;

import java.util.*;
import javax.annotation.Resource;
import javax.persistence.criteria.Predicate;
import lombok.extern.slf4j.Slf4j;
import io.github.ramerf.blog.system.entity.domain.common.DataDict;
import io.github.ramerf.blog.system.exception.CommonException;
import io.github.ramerf.blog.system.repository.BaseRepository;
import io.github.ramerf.blog.system.repository.common.DataDictRepository;
import io.github.ramerf.blog.system.service.common.DataDictService;
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
    return repository.findByCodeAndIsDelete(code, Boolean.FALSE);
  }

  @Override
  public DataDict getByTypeCodeAndCode(String typeCode, String code) {
    return repository.findByDataDictTypeCodeAndCodeAndIsDelete(typeCode, code, Boolean.FALSE);
  }

  @Override
  public List<DataDict> listByTypeCode(final String typeCode, final String criteria) {
    return repository.findByDataDictTypeCodeAndIsDelete(typeCode, Boolean.FALSE);
  }

  @Override
  public Page<DataDict> page(final long typeId, String criteria, int page, int size) {
    final PageRequest pageable = pageRequest(page, size);
    if (Objects.isNull(pageable)) {
      new PageImpl<>(Collections.emptyList());
    }
    return repository.findAll(
        (root, query, builder) -> {
          Predicate predicate = builder.and(builder.equal(root.get("isDelete"), Boolean.FALSE));
          if (typeId != -1) {
            predicate = builder.and(builder.equal(root.get("dataDictTypeId"), typeId));
          }
          if (!StringUtils.isEmpty(criteria)) {
            predicate =
                builder.and(
                    builder.or(
                        builder.like(root.get("code"), "%" + criteria + "%"),
                        builder.like(root.get("name"), "%" + criteria + "%")));
          }
          return predicate;
        },
        pageable);
  }

  @Override
  public Specification<DataDict> getSpec(String criteria) {
    return StringUtils.isEmpty(criteria)
        ? (root, query, builder) -> builder.and(builder.equal(root.get("isDelete"), Boolean.FALSE))
        : (root, query, builder) ->
            builder.and(
                builder.equal(root.get("isDelete"), Boolean.FALSE),
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
