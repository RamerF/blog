package org.ramer.admin.system.entity.response.common;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import java.util.*;
import java.util.stream.Collectors;
import lombok.*;
import org.ramer.admin.system.entity.Constant.Gender;
import org.ramer.admin.system.entity.domain.common.Manager;
import org.ramer.admin.system.entity.domain.common.Role;
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

  @ApiModelProperty(value = "工号")
  private String empNo;

  @ApiModelProperty(value = "名称")
  private String name;

  @ApiModelProperty(value = "性别")
  private Integer gender;

  @ApiModelProperty(value = "性别: 描述")
  private String genderDesc;

  @ApiModelProperty(value = "联系电话")
  private String phone;

  @ApiModelProperty(value = "头像")
  private String avatar;

  @ApiModelProperty(value = "是否可用")
  private Boolean isActive;

  @ApiModelProperty(value = "是否可用: 描述")
  private String isActiveDesc;

  @ApiModelProperty(value = "角色id")
  private List<Long> rolesIds;

  @ApiModelProperty(value = "角色: 描述")
  private List<String> rolesDesc;

  public static ManagerResponse of(final Manager manager) {
    if (Objects.isNull(manager)) {
      return null;
    }
    ManagerResponse poJo = new ManagerResponse();
    BeanUtils.copyProperties(manager, poJo);
    poJo.setIsActiveDesc(manager.getIsActive() ? "是" : "否");
    poJo.setRolesDesc(
        Optional.ofNullable(manager.getRoles()).orElse(new ArrayList<>()).stream()
            .map(Role::getName)
            .collect(Collectors.toList()));
    poJo.setGenderDesc(Gender.desc(manager.getGender()));
    return poJo;
  }
}
