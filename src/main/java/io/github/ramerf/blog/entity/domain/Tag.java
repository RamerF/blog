package io.github.ramerf.blog.entity.domain;

import javax.persistence.Column;
import javax.persistence.Entity;
import lombok.*;
import org.hibernate.annotations.Table;
import io.github.ramerf.blog.system.entity.domain.AbstractEntity;

/**
 * 文章标签.
 *
 * @author Tang Xiaofeng
 * @version 2019/10/29
 */
@Entity(name = Tag.TABLE_NAME)
@Table(appliesTo = Tag.TABLE_NAME, comment = "文章标签")
@Data
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class Tag extends AbstractEntity {
  public static final String TABLE_NAME = "tag";

  @Column(columnDefinition = "VARCHAR(100) NOT NULL COMMENT '名称'")
  private String name;

  @Column(columnDefinition = "bigint NOT NULL DEFAULT 0 COMMENT '被文章引用数'")
  private Long usingCount;

  public static Tag of(final Long id) {
    Tag tag = new Tag();
    tag.setId(id);
    return tag;
  }
}
