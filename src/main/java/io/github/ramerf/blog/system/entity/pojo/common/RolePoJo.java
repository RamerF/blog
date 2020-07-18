package io.github.ramerf.blog.system.entity.pojo.common;

import io.swagger.annotations.ApiModelProperty;
import java.util.Objects;
import lombok.*;
import io.github.ramerf.blog.system.entity.domain.AbstractEntity;
import io.github.ramerf.blog.system.entity.domain.common.Role;
import io.github.ramerf.blog.system.entity.pojo.AbstractEntityPoJo;

/**
 * 角色.
 *
 * @author ramer
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class RolePoJo extends AbstractEntityPoJo {
  @ApiModelProperty(value = "名称")
  private String name;

  @ApiModelProperty(value = "备注")
  private String remark;

  @Override
  @SuppressWarnings({"unchecked"})
  public <E extends AbstractEntity, T extends AbstractEntityPoJo> T of(
      final E entity, final Class<T> clazz) {
    if (Objects.isNull(entity)) {
      return null;
    }
    Role obj = (Role) entity;
    RolePoJo poJo = (RolePoJo) super.of(entity, clazz);
    // TODO-WARN: 添加 Domain 转 PoJo 额外规则,赋值额外字段等
    // 例如: poJo.setXxxName(Optional.ofNullable(obj.getXxx()).map(Xxx::getName).orElse(null));
    return (T) poJo;
  }

  public static RolePoJo of(final Role entity) {
    return new RolePoJo().of(entity, RolePoJo.class);
  }
}
