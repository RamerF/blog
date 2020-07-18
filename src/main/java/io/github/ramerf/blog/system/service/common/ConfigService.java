package io.github.ramerf.blog.system.service.common;

import io.github.ramerf.blog.system.entity.Constant.ConfigCode;
import io.github.ramerf.blog.system.entity.domain.common.Config;
import io.github.ramerf.blog.system.entity.pojo.common.ConfigPoJo;
import io.github.ramerf.blog.system.service.BaseService;

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
