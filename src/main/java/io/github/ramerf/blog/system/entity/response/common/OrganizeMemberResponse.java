package io.github.ramerf.blog.system.entity.response.common;

import io.github.ramerf.wind.core.entity.response.AbstractEntityResponse;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import java.util.Optional;
import lombok.*;
import io.github.ramerf.blog.system.entity.domain.common.*;
import org.springframework.beans.BeanUtils;

/**
 * 组织成员.
 *
 * @author ramer
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
@ApiModel(value = "组织成员")
public class OrganizeMemberResponse extends AbstractEntityResponse {
  @ApiModelProperty(value = "id")
  private Long id;

  @ApiModelProperty(value = "名称")
  private String name;

  @ApiModelProperty(value = "工号")
  private String empNo;

  @ApiModelProperty(value = "手机号")
  private Long phone;

  @ApiModelProperty(value = "岗位名称")
  private String postName;

  @ApiModelProperty(value = "所处组织")
  private String organizeName;

  public static OrganizeMemberResponse of(Manager manager) {
    OrganizeMemberResponse response = new OrganizeMemberResponse();
    BeanUtils.copyProperties(manager, response);
    response.setPostName(Optional.ofNullable(manager.getPost()).map(Post::getName).orElse(""));
    response.setOrganizeName(
        Optional.ofNullable(manager.getOrganize()).map(Organize::getName).orElse(""));
    return response;
  }
}
