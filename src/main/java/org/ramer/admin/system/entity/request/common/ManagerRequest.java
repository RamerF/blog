package org.ramer.admin.system.entity.request.common;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
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

  @ApiModelProperty(value = "工号")
  private String empNo;

  @ApiModelProperty(value = "密码")
  private String password;

  @ApiModelProperty(value = "姓名")
  private String name;

  @ApiModelProperty(value = "性别")
  private Integer gender;

  @ApiModelProperty(value = "手机号码")
  private String phone;

  @ApiModelProperty(value = "头像")
  private String avatar;

  @ApiModelProperty(value = "是否可用")
  private Boolean isActive = false;
}
