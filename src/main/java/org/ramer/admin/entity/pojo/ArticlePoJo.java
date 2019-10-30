package org.ramer.admin.entity.pojo;

import org.ramer.admin.system.entity.domain.AbstractEntity;
import org.ramer.admin.entity.domain.Article;
import org.ramer.admin.system.entity.pojo.AbstractEntityPoJo;
import io.swagger.annotations.ApiModelProperty;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import lombok.*;
import org.springframework.beans.BeanUtils;

/**
 * 文章.
 *
 * @author ramer
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class ArticlePoJo extends AbstractEntityPoJo {

  @ApiModelProperty(value = "String")
  private String title;

  @ApiModelProperty(value = "String")
  private String content;

  @ApiModelProperty(value = "Long")
  private Long authorId;

  @Override
  @SuppressWarnings({"unchecked"})
  public <E extends AbstractEntity, T extends AbstractEntityPoJo> T of(
      final E entity, final Class<T> clazz) {
    if (Objects.isNull(entity)) {
      return null;
    }
    Article obj = (Article) entity;
    ArticlePoJo poJo = (ArticlePoJo) super.of(entity, clazz);
    // TODO-WARN: 添加 Domain 转 PoJo 额外规则,赋值额外字段等
    // 例如: poJo.setXxxName(Optional.ofNullable(obj.getXxx()).map(Xxx::getName).orElse(null));
    return (T) poJo;
  }

  public static ArticlePoJo of(final Article entity) {
    return new ArticlePoJo().of(entity, ArticlePoJo.class);
  }
}
