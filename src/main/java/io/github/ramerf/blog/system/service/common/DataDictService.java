package io.github.ramerf.blog.system.service.common;

import java.util.List;
import io.github.ramerf.blog.system.entity.domain.common.DataDict;
import io.github.ramerf.blog.system.entity.pojo.common.DataDictPoJo;
import io.github.ramerf.blog.system.service.BaseService;
import org.springframework.data.domain.Page;

/** @author ramer */
public interface DataDictService extends BaseService<DataDict, DataDictPoJo> {
  DataDict getByCode(String code);

  DataDict getByTypeCodeAndCode(String typeCode, String code);

  List<DataDict> listByTypeCode(String typeCode, String criteria);

  Page<DataDict> page(final long typeId, final String criteria, int page, int size);
}
