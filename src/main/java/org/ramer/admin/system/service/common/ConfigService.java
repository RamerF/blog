package org.ramer.admin.system.service.common;

import org.ramer.admin.system.entity.Constant.ConfigCode;
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
   *      {@link ConfigCode#SITE_TITLE}<br/>
   *      {@link ConfigCode#SITE_NAME}<br/>
   *      {@link ConfigCode#SITE_COPYRIGHT}
   * </pre>
   */
  String getSiteInfo(String location);

  Config getByCode(String code);
}
