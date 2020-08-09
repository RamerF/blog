package io.github.ramerf.blog.entity.domain;

import com.fasterxml.jackson.annotation.JsonBackReference;
import java.util.List;
import java.util.stream.Collectors;
import javax.persistence.*;
import lombok.*;
import org.hibernate.annotations.Table;
import org.hibernate.annotations.Where;
import io.github.ramerf.blog.system.entity.domain.AbstractEntity;
import io.github.ramerf.blog.system.entity.domain.common.Manager;
import org.springframework.util.CollectionUtils;

/**
 * @author Tang Xiaofeng<br>
 * @version 2019/10/29
 */
@Data
@Entity(name = Article.TABLE_NAME)
@Table(appliesTo = Article.TABLE_NAME, comment = "文章")
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class Article extends AbstractEntity {
  public static final String TABLE_NAME = "article";

  @Column(columnDefinition = "bigint(100) NOT NULL COMMENT '编号'")
  private Long number;

  @Column(columnDefinition = "VARCHAR(20) NOT NULL COMMENT '标题'")
  private String title;

  @Column(columnDefinition = "VARCHAR(200) NOT NULL COMMENT '简述'")
  private String description;

  @Where(clause = "is_delete = false")
  @ManyToMany
  @JoinTable
  @JsonBackReference
  private List<Tag> tags;

  @Column(columnDefinition = "TEXT NOT NULL COMMENT '内容'")
  private String content;

  @Column(columnDefinition = "TEXT NOT NULL COMMENT 'html内容'")
  private String htmlContent;

  /** 作者 */
  @Column(name = "author_id", columnDefinition = "BIGINT(20) NOT NULL COMMENT '作者'")
  private Long authorId;

  @Where(clause = "is_delete = false")
  @ManyToOne
  @JoinColumn(name = "author_id", insertable = false, updatable = false)
  @JsonBackReference
  private Manager author;

  public void setTags(final List<Long> tagIds) {
    if (!CollectionUtils.isEmpty(tagIds)) {
      this.tags = tagIds.stream().map(Tag::of).collect(Collectors.toList());
    }
  }
}
