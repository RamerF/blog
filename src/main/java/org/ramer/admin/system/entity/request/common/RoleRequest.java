package org.ramer.admin.system.entity.request.common;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.*;
import org.ramer.admin.system.entity.request.AbstractEntityRequest;

/**
 * 角色.
 *
 * @author ramer
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
@ApiModel("角色")
public class RoleRequest extends AbstractEntityRequest {
  @ApiModelProperty(value = "名称")
  private String name;

  @ApiModelProperty(value = "备注")
  private String remark;
}
