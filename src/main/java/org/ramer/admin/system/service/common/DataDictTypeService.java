package org.ramer.admin.system.service.common;

import org.ramer.admin.system.entity.domain.common.DataDictType;
import org.ramer.admin.system.entity.pojo.common.DataDictTypePoJo;
import org.ramer.admin.system.service.BaseService;

/** @author ramer */
public interface DataDictTypeService extends BaseService<DataDictType, DataDictTypePoJo> {
  DataDictType getByCode(final String code);
}
