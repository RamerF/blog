package io.github.ramerf.blog.system.service.common.impl;

import javax.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import io.github.ramerf.blog.system.entity.domain.common.DataDictType;
import io.github.ramerf.blog.system.exception.CommonException;
import io.github.ramerf.blog.system.repository.BaseRepository;
import io.github.ramerf.blog.system.repository.common.DataDictTypeRepository;
import io.github.ramerf.blog.system.service.common.DataDictTypeService;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

/** @author ramer */
@Slf4j
@Service
public class DataDictTypeServiceImpl implements DataDictTypeService {
  @Resource private DataDictTypeRepository repository;

  @Override
  public Specification<DataDictType> getSpec(final String criteria) {
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
  public <U extends BaseRepository<DataDictType, Long>> U getRepository() throws CommonException {
    return (U) repository;
  }
}
