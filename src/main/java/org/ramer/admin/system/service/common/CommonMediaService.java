package org.ramer.admin.system.service.common;

import java.util.Date;
import java.util.List;
import org.ramer.admin.system.entity.domain.common.CommonMedia;
import org.ramer.admin.system.entity.pojo.common.CommonMediaPoJo;
import org.ramer.admin.system.service.BaseService;
import org.springframework.data.domain.Page;

/** @author ramer */
public interface CommonMediaService extends BaseService<CommonMedia, CommonMediaPoJo> {
  List<CommonMedia> createBatch(List<CommonMedia> medias);

  List<CommonMedia> listByCode(String code);

  Page<CommonMedia> pageByCode(String code, int page, int size);

  /** 指定时间之后. */
  List<CommonMedia> listByCodeAndAfterDate(String code, Date updateDate);
}
