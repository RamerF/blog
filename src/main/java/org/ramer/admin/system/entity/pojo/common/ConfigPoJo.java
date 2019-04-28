package org.ramer.admin.system.entity.pojo.common;

import org.ramer.admin.system.entity.domain.common.Config;
import org.ramer.admin.system.entity.pojo.AbstractEntityPoJo;
import lombok.*;

/** @author ramer */
@Data
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
public final class ConfigPoJo extends AbstractEntityPoJo {
  private String code;
  private String name;
  private String value;
  private String remark;

  public static ConfigPoJo of(Config config) {
    return new ConfigPoJo().of(config, ConfigPoJo.class);
  }
}
