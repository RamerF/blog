package org.ramer.admin.system.entity.request.common;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import java.util.List;
import lombok.*;
import org.ramer.admin.system.entity.request.AbstractEntityRequest;

/**
 * 管理员.
 *
 * @author ramer
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
@ApiModel("管理员")
public class ManagerRequest extends AbstractEntityRequest {

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
}
