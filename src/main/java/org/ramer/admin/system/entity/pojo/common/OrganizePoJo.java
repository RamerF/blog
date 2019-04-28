package org.ramer.admin.system.entity.pojo.common;

import org.ramer.admin.system.entity.domain.AbstractEntity;
import org.ramer.admin.system.entity.domain.common.Organize;
import org.ramer.admin.system.entity.pojo.AbstractEntityPoJo;
import java.util.Objects;
import lombok.*;

/**
 * 组织.
 *
 * @author ramer
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class OrganizePoJo extends AbstractEntityPoJo {

  private String name;

  private Long prevId;

  private Long rootId;

  private Boolean hasChild;

  private String remark;

  @Override
  @SuppressWarnings({"unchecked"})
  public <E extends AbstractEntity, T extends AbstractEntityPoJo> T of(E entity, Class<T> clazz) {
    if (Objects.isNull(entity)) {
      return null;
    }
    Organize obj = (Organize) entity;
    OrganizePoJo poJo = (OrganizePoJo) super.of(entity, clazz);
    // TODO-WARN: 添加 Domain 转 PoJo 规则,关联对象的字段等
    // 例如: poJo.setXxxName(Optional.ofNullable(obj.getXxx()).map(Xxx::getName).orElse(null));
    return (T) poJo;
  }

  public static OrganizePoJo of(Organize entity) {
    return new OrganizePoJo().of(entity, OrganizePoJo.class);
  }
}
