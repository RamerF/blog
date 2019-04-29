package org.ramer.admin.system.service.common;

import org.ramer.admin.system.entity.domain.common.Config;
import org.ramer.admin.system.entity.pojo.common.ConfigPoJo;
import org.ramer.admin.system.service.BaseService;

/** @author ramer */
public interface ConfigService extends BaseService<Config, ConfigPoJo> {
  /**
   * 获取网站描述信息.
   * <pre>
   * @param location:
   *      option value:<br/>
   *      {@code Constant.SITE_TITLE}<br/>
   *      {@code Constant.SITE_NAME}<br/>
   *      {@code Constant.SITE_COPYRIGHT}
   * </pre>
   */
  String getSiteInfo(String location);

  Config getByCode(String code);
}
