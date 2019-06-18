package org.ramer.admin.system.entity.pojo.common;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import java.util.Objects;
import lombok.*;
import org.ramer.admin.system.entity.domain.AbstractEntity;
import org.ramer.admin.system.entity.domain.common.Menu;
import org.ramer.admin.system.entity.pojo.AbstractEntityPoJo;

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
public class MenuPoJo extends AbstractEntityPoJo {

  @ApiModelProperty(value = "名称")
  private String name;

  @ApiModelProperty(value = "权限别名")
  private String alia;

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

  @Override
  @SuppressWarnings({"unchecked"})
  public <E extends AbstractEntity, T extends AbstractEntityPoJo> T of(
      final E entity, final Class<T> clazz) {
    if (Objects.isNull(entity)) {
      return null;
    }
    Menu obj = (Menu) entity;
    MenuPoJo poJo = (MenuPoJo) super.of(entity, clazz);
    if (!Objects.isNull(obj.getParent())) {
      poJo.setParentId(obj.getParent().getId());
    }
    return (T) poJo;
  }

  public static MenuPoJo of(final Menu entity) {
    return new MenuPoJo().of(entity, MenuPoJo.class);
  }
}
