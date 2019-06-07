package org.ramer.admin.system.service.common.impl;

import javax.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.ramer.admin.system.entity.Constant.State;
import org.ramer.admin.system.entity.domain.common.DataDictType;
import org.ramer.admin.system.exception.CommonException;
import org.ramer.admin.system.repository.BaseRepository;
import org.ramer.admin.system.repository.common.DataDictTypeRepository;
import org.ramer.admin.system.service.common.DataDictTypeService;
import org.ramer.admin.system.util.TextUtil;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

/** @author ramer */
@Slf4j
@Service
public class DataDictTypeServiceImpl implements DataDictTypeService {
  @Resource private DataDictTypeRepository repository;

  @Override
  public void textFilter(DataDictType trans, DataDictType filtered) {
    filtered.setName(TextUtil.filter(trans.getName()));
    filtered.setRemark(TextUtil.filter(trans.getRemark()));
    if (!StringUtils.isEmpty(trans.getCode())) {
      filtered.setCode(TextUtil.filter(trans.getCode()));
    }
  }

  @Override
  public Specification<DataDictType> getSpec(final String criteria) {
    return (root, query, builder) ->
        builder.and(
            builder.equal(root.get("state"), State.STATE_ON),
            builder.or(
                builder.like(root.get("name"), "%" + criteria + "%"),
                builder.like(root.get("code"), "%" + criteria + "%"),
                builder.like(root.get("remark"), "%" + criteria + "%")));
  }

  @SuppressWarnings({"unchecked"})
  @Override
  public <U extends BaseRepository<DataDictType, Long>> U getRepository() throws CommonException {
    return (U) repository;
  }
}
