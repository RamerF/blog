package org.ramer.admin.system.service.common;

import java.util.List;
import org.ramer.admin.system.entity.domain.common.DataDict;
import org.ramer.admin.system.entity.domain.common.DataDictType;
import org.ramer.admin.system.entity.pojo.common.DataDictPoJo;
import org.ramer.admin.system.service.BaseService;
import org.springframework.data.domain.Page;

/** @author ramer */
public interface DataDictService extends BaseService<DataDict, DataDictPoJo> {
  List<DataDictType> listType();

  DataDict getByCode(String code);

  DataDict getByTypeCodeAndCode(String typeCode, String code);

  List<DataDict> listByTypeCode(String typeCode, String criteria);

  Page<DataDict> pageByTypeCode(String typeCode, String criteria, int page, int size);

  DataDict create(DataDict dataDict, String typeCode);
}
