package io.github.ramerf.blog.system.entity.pojo.common;

import java.util.Objects;
import java.util.Optional;
import lombok.*;
import io.github.ramerf.blog.system.entity.domain.AbstractEntity;
import io.github.ramerf.blog.system.entity.domain.common.ManageLog;
import io.github.ramerf.blog.system.entity.pojo.AbstractEntityPoJo;

/**
 * 管理端日志.
 *
 * @author ramer
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class ManageLogPoJo extends AbstractEntityPoJo {

  private String url;

  private String ip;

  private String result;

  private Long managerId;

  @Override
  @SuppressWarnings({"unchecked"})
  public <E extends AbstractEntity, T extends AbstractEntityPoJo> T of(
      final E entity, final Class<T> clazz) {
    if (Objects.isNull(entity)) {
      return null;
    }
    ManageLog obj = (ManageLog) entity;
    ManageLogPoJo poJo = (ManageLogPoJo) super.of(entity, clazz);
    poJo.setManagerId(
        Optional.ofNullable(obj.getManager()).map(AbstractEntity::getId).orElse(null));
    return (T) poJo;
  }

  public static ManageLogPoJo of(ManageLog entity) {
    return new ManageLogPoJo().of(entity, ManageLogPoJo.class);
  }
}
