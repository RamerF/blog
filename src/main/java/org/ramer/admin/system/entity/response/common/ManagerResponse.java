package org.ramer.admin.system.entity.response.common;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import java.util.List;
import java.util.Objects;
import lombok.*;
import org.ramer.admin.system.entity.domain.common.Manager;
import org.ramer.admin.system.entity.response.AbstractEntityResponse;
import org.springframework.beans.BeanUtils;

/**
 * 管理员.
 *
 * @author ramer
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
@ApiModel(value = "管理员")
public class ManagerResponse extends AbstractEntityResponse {

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

  public static ManagerResponse of(final Manager manager) {
    if (Objects.isNull(manager)) {
      return null;
    }
    ManagerResponse poJo = new ManagerResponse();
    // TODO-WARN:  将 Domain 对象转换成 Response 对象
    BeanUtils.copyProperties(manager, poJo);
    return poJo;
  }
}
