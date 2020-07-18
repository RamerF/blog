package io.github.ramerf.blog.entity.pojo;

import io.github.ramerf.wind.core.entity.pojo.AbstractEntityPoJo;
import javax.persistence.Column;
import javax.persistence.Entity;
import lombok.*;

/**
 * 文章标签.
 *
 * @author Tang Xiaofeng
 * @version 2019/10/29
 */
@Entity(name = "tag")
@Data
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class TagPoJo extends AbstractEntityPoJo {
  @Column(columnDefinition = "VARCHAR(100) NOT NULL COMMENT '名称'")
  private String name;

  @Column(columnDefinition = "bigint NOT NULL DEFAULT 0 COMMENT '被文章引用数'")
  private Long usingCount;

  public static TagPoJo of(final Long id) {
    TagPoJo tag = new TagPoJo();
    tag.setId(id);
    return tag;
  }
}
