package org.ramer.admin.entity.domain;

import com.fasterxml.jackson.annotation.JsonBackReference;
import javax.persistence.*;
import lombok.*;
import org.hibernate.annotations.Table;
import org.hibernate.annotations.Where;
import org.ramer.admin.system.entity.Constant.State;
import org.ramer.admin.system.entity.domain.AbstractEntity;
import org.ramer.admin.system.entity.domain.common.Manager;

/**
 * @author Tang Xiaofeng<br>
 * @version 2019/10/29
 */
@Entity(name = Article.TABLE_NAME)
@Table(appliesTo = Article.TABLE_NAME, comment = "文章")
@Data
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class Article extends AbstractEntity {
  public static final String TABLE_NAME = "article";

  @Column(columnDefinition = "int(100) NOT NULL COMMENT '编号'")
  private Long number;

  @Column(columnDefinition = "VARCHAR(100) NOT NULL COMMENT '标题'")
  private String title;

  @Column(columnDefinition = "TEXT NOT NULL COMMENT '标题'")
  private String content;

  /** 作者 */
  @Column(name = "author_id", columnDefinition = "BIGINT(20) NOT NULL COMMENT '作者'")
  private Long authorId;

  @Where(clause = "state = " + State.STATE_ON)
  @ManyToOne
  @JoinColumn(name = "author_id", insertable = false, updatable = false)
  @JsonBackReference
  private Manager author;
}
