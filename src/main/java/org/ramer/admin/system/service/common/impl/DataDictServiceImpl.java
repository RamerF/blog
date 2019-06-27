package org.ramer.admin.system.service.common.impl;

import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import java.util.*;
import javax.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.ramer.admin.system.entity.Constant.State;
import org.ramer.admin.system.entity.domain.common.*;
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
  @Resource private JPAQueryFactory jpaQueryFactory;
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
  public Page<DataDict> page(final long typeId, String criteria, int page, int size) {
    final PageRequest pageable = pageRequest(page, size);
    if (Objects.isNull(pageable)) {
      new PageImpl<>(Collections.emptyList());
    }
    final QDataDict dataDict = QDataDict.dataDict;
    final QDataDictType dataDictType = QDataDictType.dataDictType;
    BooleanExpression expression = dataDict.state.eq(State.STATE_ON);
    final JPAQuery<DataDict> query =
        jpaQueryFactory
            .selectFrom(dataDict)
            .innerJoin(dataDictType)
            .on(
                dataDictType
                    .id
                    .eq(dataDict.dataDictTypeId)
                    .and(dataDictType.id.eq(typeId))
                    .and(dataDictType.state.eq(State.STATE_ON)))
            .offset(page - 1)
            .limit(page * size);
    if (!StringUtils.isEmpty(criteria)) {
      expression =
          expression.or(dataDict.code.contains(criteria)).or(dataDict.name.contains(criteria));
    }
    query.where(expression);
    return new PageImpl<>(query.fetch(), pageable, query.fetchCount());
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
