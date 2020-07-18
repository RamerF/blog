package io.github.ramerf.blog.system.entity.pojo.common;

import java.util.Objects;
import lombok.*;
import io.github.ramerf.blog.system.entity.domain.AbstractEntity;
import io.github.ramerf.blog.system.entity.domain.common.CommonMediaCategory;
import io.github.ramerf.blog.system.entity.pojo.AbstractEntityPoJo;

/**
 * 通用多媒体类别.
 *
 * @author ramer
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class CommonMediaCategoryPoJo extends AbstractEntityPoJo {

  private String code;

  private String name;

  private String remark;

  @Override
  @SuppressWarnings({"unchecked"})
  public <E extends AbstractEntity, T extends AbstractEntityPoJo> T of(
      final E entity, final Class<T> clazz) {
    if (Objects.isNull(entity)) {
      return null;
    }
    CommonMediaCategory obj = (CommonMediaCategory) entity;
    CommonMediaCategoryPoJo poJo = (CommonMediaCategoryPoJo) super.of(entity, clazz);
    // TODO-WARN: 添加 Domain 转 PoJo 额外规则,赋值额外字段等
    // 例如: poJo.setXxxName(Optional.ofNullable(obj.getXxx()).map(Xxx::getName).orElse(null));
    return (T) poJo;
  }

  public static CommonMediaCategoryPoJo of(final CommonMediaCategory entity) {
    return new CommonMediaCategoryPoJo().of(entity, CommonMediaCategoryPoJo.class);
  }
}
