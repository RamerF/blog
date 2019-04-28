package org.ramer.admin.system.service.common;

import org.ramer.admin.system.service.BaseService;
import org.ramer.admin.system.entity.domain.common.CommonMediaCategory;
import org.ramer.admin.system.entity.pojo.common.CommonMediaCategoryPoJo;
import java.util.Date;
import java.util.List;

/**
 * 通用多媒体文件存储.
 *
 * @author ramer
 */
public interface CommonMediaCategoryService
    extends BaseService<CommonMediaCategory, CommonMediaCategoryPoJo> {
  List<CommonMediaCategory> getByCode(String code);

  List<CommonMediaCategory> listAfterDate(String code, Date updateTime);
}
