package io.github.ramerf.blog.system.service.common;

import java.util.Date;
import java.util.List;
import io.github.ramerf.blog.system.entity.domain.common.CommonMediaCategory;
import io.github.ramerf.blog.system.entity.pojo.common.CommonMediaCategoryPoJo;
import io.github.ramerf.blog.system.service.BaseService;

/** @author ramer */
public interface CommonMediaCategoryService
    extends BaseService<CommonMediaCategory, CommonMediaCategoryPoJo> {
  List<CommonMediaCategory> getByCode(String code);

  List<CommonMediaCategory> listAfterDate(String code, Date updateTime);
}
