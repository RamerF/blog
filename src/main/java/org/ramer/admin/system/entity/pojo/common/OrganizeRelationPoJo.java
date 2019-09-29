package org.ramer.admin.system.entity.pojo.common;

import io.swagger.annotations.ApiModelProperty;
import java.util.Objects;
import lombok.*;
import org.ramer.admin.system.entity.domain.AbstractEntity;
import org.ramer.admin.system.entity.domain.common.OrganizeRelation;
import org.ramer.admin.system.entity.pojo.AbstractEntityPoJo;

/**
 * 组织关系.
 *
 * @author ramer
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class OrganizeRelationPoJo extends AbstractEntityPoJo {

  @ApiModelProperty(value = "Long")
  private Long prevId;

  @ApiModelProperty(value = "Long")
  private Long nextId;

  @ApiModelProperty(value = "Integer")
  private Integer depth;

  @Override
  @SuppressWarnings({"unchecked"})
  public <E extends AbstractEntity, T extends AbstractEntityPoJo> T of(
      final E entity, final Class<T> clazz) {
    if (Objects.isNull(entity)) {
      return null;
    }
    OrganizeRelation obj = (OrganizeRelation) entity;
    OrganizeRelationPoJo poJo = (OrganizeRelationPoJo) super.of(entity, clazz);
    // TODO-WARN: 添加 Domain 转 PoJo 额外规则,赋值额外字段等
    // 例如: poJo.setXxxName(Optional.ofNullable(obj.getXxx()).map(Xxx::getName).orElse(null));
    return (T) poJo;
  }

  public static OrganizeRelationPoJo of(final OrganizeRelation entity) {
    return new OrganizeRelationPoJo().of(entity, OrganizeRelationPoJo.class);
  }
}
