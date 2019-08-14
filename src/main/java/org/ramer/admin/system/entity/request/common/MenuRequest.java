package org.ramer.admin.system.entity.request.common;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.*;
import org.ramer.admin.system.entity.request.AbstractEntityRequest;

/**
 * 菜单.
 *
 * @author ramer
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
@ApiModel(value = "菜单")
public class MenuRequest extends AbstractEntityRequest {

  @ApiModelProperty(value = "名称")
  private String name;

  @ApiModelProperty(value = "链接地址")
  private String url;

  @ApiModelProperty(value = "排序")
  private Integer sortWeight;

  @ApiModelProperty(value = "图标")
  private String icon;

  @ApiModelProperty(value = "备注")
  private String remark;

  @ApiModelProperty(value = "父菜单id")
  private Long parentId;
}
