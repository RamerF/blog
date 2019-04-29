package org.ramer.admin.system.entity.pojo.common;

import java.util.Objects;
import lombok.*;
import org.ramer.admin.system.entity.domain.AbstractEntity;
import org.ramer.admin.system.entity.domain.common.CommonMedia;
import org.ramer.admin.system.entity.domain.common.CommonMediaCategory;
import org.ramer.admin.system.entity.pojo.AbstractEntityPoJo;

/**
 * 通用多媒体.
 *
 * @author ramer
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class CommonMediaPoJo extends AbstractEntityPoJo {

  private String code;

  private String url;

  private String remark;

  private Long categoryId;

  @Override
  @SuppressWarnings({"unchecked"})
  public <E extends AbstractEntity, T extends AbstractEntityPoJo> T of(
      final E entity, final Class<T> clazz) {
    if (Objects.isNull(entity)) {
      return null;
    }
    CommonMedia obj = (CommonMedia) entity;
    CommonMediaPoJo poJo = (CommonMediaPoJo) super.of(entity, clazz);
    final CommonMediaCategory category = obj.getCategory();
    if (!Objects.isNull(category)) {
      poJo.setCategoryId(category.getId());
    }
    return (T) poJo;
  }

  public static CommonMediaPoJo of(final CommonMedia entity) {
    return new CommonMediaPoJo().of(entity, CommonMediaPoJo.class);
  }
}
