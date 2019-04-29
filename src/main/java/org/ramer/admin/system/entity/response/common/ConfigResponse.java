package org.ramer.admin.system.entity.response.common;

import java.util.Objects;
import lombok.*;
import org.ramer.admin.system.entity.domain.common.Config;
import org.ramer.admin.system.entity.response.AbstractEntityResponse;
import org.springframework.beans.BeanUtils;

/**
 * 系统配置.
 *
 * @author ramer
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class ConfigResponse extends AbstractEntityResponse {

  private String code;

  private String name;

  private String value;

  private String remark;

  public static ConfigResponse of(final Config config) {
    if (Objects.isNull(config)) {
      return null;
    }
    ConfigResponse poJo = new ConfigResponse();
    // TODO-WARN:  将 Domain 对象转换成 Response 对象
    BeanUtils.copyProperties(config, poJo);
    return poJo;
  }
}
