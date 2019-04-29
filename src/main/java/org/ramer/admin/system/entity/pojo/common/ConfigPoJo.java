package org.ramer.admin.system.entity.pojo.common;

import java.util.Objects;
import lombok.*;
import org.ramer.admin.system.entity.domain.AbstractEntity;
import org.ramer.admin.system.entity.domain.common.Config;
import org.ramer.admin.system.entity.pojo.AbstractEntityPoJo;

/**
 * 系统配置.
 *
 * @author ramer
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class ConfigPoJo extends AbstractEntityPoJo {

  private String code;

  private String name;

  private String value;

  private String remark;

  @Override
  @SuppressWarnings({"unchecked"})
  public <E extends AbstractEntity, T extends AbstractEntityPoJo> T of(
      final E entity, final Class<T> clazz) {
    if (Objects.isNull(entity)) {
      return null;
    }
    Config obj = (Config) entity;
    ConfigPoJo poJo = (ConfigPoJo) super.of(entity, clazz);
    // TODO-WARN: 添加 Domain 转 PoJo 额外规则,赋值额外字段等
    // 例如: poJo.setXxxName(Optional.ofNullable(obj.getXxx()).map(Xxx::getName).orElse(null));
    return (T) poJo;
  }

  public static ConfigPoJo of(final Config entity) {
    return new ConfigPoJo().of(entity, ConfigPoJo.class);
  }
}
