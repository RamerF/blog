package org.ramer.admin.system.entity.pojo.common;

import java.util.Objects;
import java.util.Optional;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.ramer.admin.system.entity.domain.AbstractEntity;
import org.ramer.admin.system.entity.domain.common.ManageLog;
import org.ramer.admin.system.entity.pojo.AbstractEntityPoJo;

@Data
@EqualsAndHashCode(callSuper = true)
public class ManageLogPoJo extends AbstractEntityPoJo {
  private String url;
  private Long managerId;
  private String ip;
  private String result;

  @Override
  @SuppressWarnings({"unchecked"})
  public <E extends AbstractEntity, T extends AbstractEntityPoJo> T of(
      final E entity, final Class<T> clazz) {
    if (Objects.isNull(entity)) {
      return null;
    }
    ManageLogPoJo poJo = (ManageLogPoJo) super.of(entity, clazz);
    ManageLog obj = (ManageLog) entity;
    poJo.setManagerId(
        Optional.ofNullable(obj.getManager()).map(AbstractEntity::getId).orElse(null));
    return (T) poJo;
  }

  public static ManageLogPoJo of(ManageLog log) {
    return new ManageLogPoJo().of(log, ManageLogPoJo.class);
  }
}
