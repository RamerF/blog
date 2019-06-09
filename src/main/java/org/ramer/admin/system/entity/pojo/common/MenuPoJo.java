package org.ramer.admin.system.entity.pojo.common;

import java.time.LocalDateTime;
import java.util.Date;
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

  private Boolean leaf;

  private String name;

  private String alia;

  private String url;

  private Integer sort;

  private String icon;

  private String remark;

  private Long parentId;

  public MenuPoJo(
      final Long id,
      final Integer state,
      final String name,
      final String url,
      final Boolean leaf,
      final String icon,
      final Long pId,
      final int sort,
      final LocalDateTime createTime,
      final LocalDateTime updateTime) {
    setId(id);
    setState(state);
    setName(name);
    setUrl(url);
    setLeaf(leaf);
    setIcon(icon);
    setParentId(pId);
    setSort(sort);
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
