package io.github.ramerf.blog.system.service.common;

import java.util.Date;
import java.util.List;
import io.github.ramerf.blog.system.entity.domain.common.CommonMedia;
import io.github.ramerf.blog.system.entity.pojo.common.CommonMediaPoJo;
import io.github.ramerf.blog.system.service.BaseService;
import org.springframework.data.domain.Page;

/** @author ramer */
public interface CommonMediaService extends BaseService<CommonMedia, CommonMediaPoJo> {

  List<CommonMedia> listByCode(String code);

  Page<CommonMedia> pageByCode(String code, int page, int size);

  /** 指定时间之后. */
  List<CommonMedia> listByCodeAndAfterDate(String code, Date updateDate);
}
