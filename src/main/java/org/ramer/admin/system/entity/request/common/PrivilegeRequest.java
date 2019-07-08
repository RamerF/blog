package org.ramer.admin.system.entity.request.common;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.*;
import org.ramer.admin.system.entity.request.AbstractEntityRequest;

/**
 * 权限.
 *
 * @author ramer
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
@ApiModel("权限")
public class PrivilegeRequest extends AbstractEntityRequest {

  @ApiModelProperty(value = "权限表达式,class前缀:(read|create|write|delete)", example = "manager:read")
  private String exp;

  @ApiModelProperty(value = "名称")
  private String name;
}
