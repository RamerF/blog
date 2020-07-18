package io.github.ramerf.blog.entity.pojo;

import io.github.ramerf.wind.core.entity.pojo.AbstractEntityPoJo;
import javax.persistence.Entity;
import lombok.*;

/**
 * 文章.
 *
 * @author ramer
 */
@Entity(name = "article_tag_map")
@Data
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class ArticleTagMapPoJo extends AbstractEntityPoJo {

  private Long articleId;

  private Long tagId;
}
