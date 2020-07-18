package io.github.ramerf.blog.system.entity.pojo.common;

import io.swagger.annotations.ApiModelProperty;
import java.util.Objects;
import lombok.*;
import io.github.ramerf.blog.system.entity.domain.AbstractEntity;
import io.github.ramerf.blog.system.entity.domain.common.Post;
import io.github.ramerf.blog.system.entity.pojo.AbstractEntityPoJo;

/**
 * 岗位.
 *
 * @author ramer
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class PostPoJo extends AbstractEntityPoJo {

  @ApiModelProperty(value = "String")
  private String name;

  @ApiModelProperty(value = "Integer")
  private Integer dataAccess;

  @ApiModelProperty(value = "Long")
  private Long organizeId;

  @Override
  @SuppressWarnings({"unchecked"})
  public <E extends AbstractEntity, T extends AbstractEntityPoJo> T of(
      final E entity, final Class<T> clazz) {
    if (Objects.isNull(entity)) {
      return null;
    }
    Post obj = (Post) entity;
    PostPoJo poJo = (PostPoJo) super.of(entity, clazz);
    // TODO-WARN: 添加 Domain 转 PoJo 额外规则,赋值额外字段等
    // 例如: poJo.setXxxName(Optional.ofNullable(obj.getXxx()).map(Xxx::getName).orElse(null));
    return (T) poJo;
  }

  public static PostPoJo of(final Post entity) {
    return new PostPoJo().of(entity, PostPoJo.class);
  }
}
