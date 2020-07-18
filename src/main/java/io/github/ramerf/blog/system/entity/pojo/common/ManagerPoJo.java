package io.github.ramerf.blog.system.entity.pojo.common;

import io.swagger.annotations.ApiModelProperty;
import java.util.List;
import java.util.Objects;
import lombok.*;
import io.github.ramerf.blog.system.entity.domain.AbstractEntity;
import io.github.ramerf.blog.system.entity.domain.common.Manager;
import io.github.ramerf.blog.system.entity.pojo.AbstractEntityPoJo;

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

  @ApiModelProperty(value = "String")
  private String empNo;

  @ApiModelProperty(value = "String")
  private String password;

  @ApiModelProperty(value = "String")
  private String name;

  @ApiModelProperty(value = "Integer")
  private Integer gender;

  @ApiModelProperty(value = "String")
  private String phone;

  @ApiModelProperty(value = "String")
  private String avatar;

  @ApiModelProperty(value = "Boolean")
  private Boolean isActive;

  @ApiModelProperty(value = "roles")
  private List<Long> rolesIds;

  @Override
  @SuppressWarnings({"unchecked"})
  public <E extends AbstractEntity, T extends AbstractEntityPoJo> T of(
      final E entity, final Class<T> clazz) {
    if (Objects.isNull(entity)) {
      return null;
    }
    Manager obj = (Manager) entity;
    ManagerPoJo poJo = (ManagerPoJo) super.of(entity, clazz);
    // TODO-WARN: 添加 Domain 转 PoJo 额外规则,赋值额外字段等
    // 例如: poJo.setXxxName(Optional.ofNullable(obj.getXxx()).map(Xxx::getName).orElse(null));
    return (T) poJo;
  }

  public static ManagerPoJo of(final Manager entity) {
    return new ManagerPoJo().of(entity, ManagerPoJo.class);
  }
}
