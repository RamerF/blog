package org.ramer.admin.system.entity.response.common;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import java.util.List;
import lombok.*;
import org.ramer.admin.system.entity.domain.common.Menu;
import org.ramer.admin.system.entity.pojo.common.MenuPoJo;
import org.ramer.admin.system.entity.response.AbstractEntityResponse;
import org.springframework.beans.BeanUtils;

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
public class MenuResponse extends AbstractEntityResponse {
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

  @ApiModelProperty(value = "上级id")
  private Long parentId;

  @ApiModelProperty(value = "是否有子菜单")
  private Boolean hasChild = false;

  @ApiModelProperty(value = "子菜单")
  private List<MenuResponse> children;

  public static MenuResponse of(MenuPoJo menuPoJo) {
    MenuResponse response = new MenuResponse();
    BeanUtils.copyProperties(menuPoJo, response);
    return response;
  }

  public static MenuResponse of(Menu menu) {
    MenuResponse response = new MenuResponse();
    BeanUtils.copyProperties(menu, response);
    return response;
  }
}
