package org.ramer.admin.entity.domain;

import com.fasterxml.jackson.annotation.JsonBackReference;
import java.util.List;
import javax.persistence.*;
import lombok.*;
import org.hibernate.annotations.Table;
import org.hibernate.annotations.Where;
import org.ramer.admin.system.entity.Constant.State;
import org.ramer.admin.system.entity.domain.AbstractEntity;
import org.ramer.admin.system.entity.domain.common.Manager;
import org.ramer.admin.system.entity.domain.common.Organize;

/**
 * @author Tang Xiaofeng<br>
 * @version 2019/10/29
 */
@Entity(name = Task.TABLE_NAME)
@Table(appliesTo = Task.TABLE_NAME, comment = "任务")
@Data
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class Task extends AbstractEntity {
  public static final String TABLE_NAME = "task";

  @Column(columnDefinition = "VARCHAR(100) NOT NULL COMMENT '名称'")
  private String name;

  @Column(columnDefinition = "VARCHAR(500) NOT NULL COMMENT '描述'")
  private String description;

  /** 所属组织 */
  @Column(name = "organize_id", columnDefinition = "BIGINT(20) NOT NULL COMMENT '作者'")
  private Long organizeId;

  @Where(clause = "state = " + State.STATE_ON)
  @ManyToOne
  @JoinColumn(name = "author_id", insertable = false, updatable = false)
  @JsonBackReference
  private Organize organize;

  /** 执行人/参与人 */
  @OneToMany @JoinTable @JsonBackReference private List<Manager> executors;
}
