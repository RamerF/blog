package org.ramer.admin.system.entity.pojo.common;

import org.ramer.admin.system.entity.domain.AbstractEntity;
import org.ramer.admin.system.entity.domain.common.Menu;
import org.ramer.admin.system.entity.pojo.AbstractEntityPoJo;
import java.util.Date;
import java.util.Objects;
import lombok.*;

/** @author ramer */
@Data
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
public final class MenuPoJo extends AbstractEntityPoJo {
  private String name;
  private String url;
  private Boolean leaf;
  private String icon;
  private Long pId;
  private String pName;
  private int sort;

  public MenuPoJo(
      final Long id,
      final Integer state,
      final String name,
      final String url,
      final Boolean leaf,
      final String icon,
      final Long pId,
      final int sort,
      final Date createTime,
      final Date updateTime) {
    setId(id);
    setState(state);
    setName(name);
    setUrl(url);
    setLeaf(leaf);
    setIcon(icon);
    setPId(pId);
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
    MenuPoJo poJo = (MenuPoJo) super.of(entity, clazz);
    Menu obj = (Menu) entity;
    if (!Objects.isNull(obj.getParent())) {
      poJo.setPId(obj.getParent().getId());
      poJo.setPName(obj.getParent().getName());
    }
    return (T) poJo;
  }

  public static MenuPoJo of(Menu menu) {
    return new MenuPoJo().of(menu, MenuPoJo.class);
  }
}
