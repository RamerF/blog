package io.github.ramerf.blog.system.entity.request.common;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.*;
import io.github.ramerf.blog.system.entity.request.AbstractEntityRequest;

/**
 * 岗位.
 *
 * @author ramer
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
@ApiModel("岗位")
public class PostRequest extends AbstractEntityRequest {

  @ApiModelProperty(value = "名称")
  private String name;

  @ApiModelProperty(value = "数据权限")
  private Integer dataAccess;

  @ApiModelProperty(value = "组织")
  private Long organizeId;

  @ApiModelProperty(value = "备注")
  private String remark;
}
