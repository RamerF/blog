package io.github.ramerf.blog.entity.domain;

import javax.persistence.Column;
import javax.persistence.Entity;
import lombok.*;
import org.hibernate.annotations.Table;
import io.github.ramerf.blog.system.entity.domain.AbstractEntity;

/**
 * 文章.
 *
 * @author ramer
 */
@Entity(name = ArticleTagMap.TABLE_NAME)
@Table(appliesTo = ArticleTagMap.TABLE_NAME, comment = "文章")
@Data
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class ArticleTagMap extends AbstractEntity {
  public static final String TABLE_NAME = "article_tag_map";

  @Column(columnDefinition = "bigint(100) NOT NULL COMMENT '文章'")
  private Long articleId;

  @Column(columnDefinition = "bigint(100) NOT NULL COMMENT '标签'")
  private Long tagId;
}
