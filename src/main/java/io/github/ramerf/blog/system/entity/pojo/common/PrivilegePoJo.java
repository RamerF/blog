package io.github.ramerf.blog.system.entity.pojo.common;

import io.swagger.annotations.ApiModelProperty;
import lombok.*;
import io.github.ramerf.blog.system.entity.domain.common.Privilege;
import io.github.ramerf.blog.system.entity.pojo.AbstractEntityPoJo;

/**
 * 权限.
 *
 * @author ramer
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class PrivilegePoJo extends AbstractEntityPoJo {

  @ApiModelProperty(value = "权限表达式,class前缀:(read|create|write|delete)", example = "manager:read")
  private String exp;

  @ApiModelProperty(value = "名称")
  private String name;

  public static PrivilegePoJo of(final Privilege entity) {
    return new PrivilegePoJo().of(entity, PrivilegePoJo.class);
  }
}
