package org.ramer.admin.system.entity.pojo.common;

import java.time.LocalDateTime;
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
public class MenuPoJo extends AbstractEntityPoJo {

  private Boolean isLeaf;

  private String name;

  private String alia;

  private String url;

  private Integer sortWeight;

  private String icon;

  private String remark;

  private Long parentId;

  public MenuPoJo(
      final Long id,
      final Integer state,
      final String name,
      final String url,
      final Boolean isLeaf,
      final String icon,
      final Long pId,
      final int sortWeight,
      final LocalDateTime createTime,
      final LocalDateTime updateTime) {
    setId(id);
    setState(state);
    setName(name);
    setUrl(url);
    setIsLeaf(isLeaf);
    setIcon(icon);
    setParentId(pId);
    setSortWeight(sortWeight);
    setCreateTime(createTime);
    setUpdateTime(updateTime);
  }

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
