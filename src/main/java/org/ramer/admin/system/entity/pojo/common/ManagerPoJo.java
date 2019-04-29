package org.ramer.admin.system.entity.pojo.common;

import java.util.*;
import lombok.*;
import org.ramer.admin.system.entity.domain.AbstractEntity;
import org.ramer.admin.system.entity.domain.common.Manager;
import org.ramer.admin.system.entity.pojo.AbstractEntityPoJo;

/**
 * 管理员.
 *
 * @author ramer
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class ManagerPoJo extends AbstractEntityPoJo {

  private String empNo;

  private String password;

  private String name;

  private Integer gender;

  private String phone;

  private String avatar;

  private Integer active;

  private Date validDate;

  @Override
  @SuppressWarnings({"unchecked"})
  public <E extends AbstractEntity, T extends AbstractEntityPoJo> T of(E entity, Class<T> clazz) {
    if (Objects.isNull(entity)) {
      return null;
    }
    Manager obj = (Manager) entity;
    ManagerPoJo poJo = (ManagerPoJo) super.of(entity, clazz);
    // TODO-WARN: 添加 Domain 转 PoJo 规则,关联对象的字段等
    // 例如: poJo.setXxxName(Optional.ofNullable(obj.getXxx()).map(Xxx::getName).orElse(null));
    return (T) poJo;
  }

  public static ManagerPoJo of(Manager entity) {
    return new ManagerPoJo().of(entity, ManagerPoJo.class);
  }
}
