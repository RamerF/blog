package org.ramer.admin.system.service.common;

import java.util.List;
import org.ramer.admin.system.entity.domain.common.DataDict;
import org.ramer.admin.system.entity.pojo.common.DataDictPoJo;
import org.ramer.admin.system.service.BaseService;
import org.springframework.data.domain.Page;

/** @author ramer */
public interface DataDictService extends BaseService<DataDict, DataDictPoJo> {
  DataDict getByCode(String code);

  DataDict getByTypeCodeAndCode(String typeCode, String code);

  List<DataDict> listByTypeCode(String typeCode, String criteria);

  Page<DataDict> page(final long typeId, final String criteria, int page, int size);
}
