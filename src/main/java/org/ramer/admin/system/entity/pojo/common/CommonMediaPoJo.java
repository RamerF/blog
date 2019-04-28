package org.ramer.admin.system.entity.pojo.common;

import java.util.Objects;
import lombok.*;
import org.ramer.admin.system.entity.domain.AbstractEntity;
import org.ramer.admin.system.entity.domain.common.CommonMedia;
import org.ramer.admin.system.entity.domain.common.CommonMediaCategory;
import org.ramer.admin.system.entity.pojo.AbstractEntityPoJo;

/**
 * 通用多媒体文件存储.
 *
 * @author Ramer @Date 1/10/2019
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
    CommonMediaPoJo poJo = (CommonMediaPoJo) super.of(entity, clazz);
    CommonMedia obj = (CommonMedia) entity;
    final CommonMediaCategory category = obj.getCategory();
    if (!Objects.isNull(category)) {
      poJo.setCategoryId(category.getId());
    }
    return (T) poJo;
  }

  public static CommonMediaPoJo of(CommonMedia media) {
    return new CommonMediaPoJo().of(media, CommonMediaPoJo.class);
  }
}
