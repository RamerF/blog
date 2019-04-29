package org.ramer.admin.system.service.common;

import java.util.Date;
import java.util.List;
import org.ramer.admin.system.entity.domain.common.CommonMediaCategory;
import org.ramer.admin.system.entity.pojo.common.CommonMediaCategoryPoJo;
import org.ramer.admin.system.service.BaseService;

/** @author ramer */
public interface CommonMediaCategoryService
    extends BaseService<CommonMediaCategory, CommonMediaCategoryPoJo> {
  List<CommonMediaCategory> getByCode(String code);

  List<CommonMediaCategory> listAfterDate(String code, Date updateTime);
}
