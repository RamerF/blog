package org.ramer.admin.system.entity.domain;

import java.io.Serializable;
import java.time.LocalDateTime;
import javax.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;
import org.ramer.admin.system.entity.Constant.State;

/** domain类的父类 */
@Data
@MappedSuperclass
@NoArgsConstructor
public abstract class AbstractEntity implements Serializable {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @Column(columnDefinition = "TINYINT DEFAULT 1")
  private Integer state = State.STATE_ON;

  @CreationTimestamp
  @Column(columnDefinition = "DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间'")
  private LocalDateTime createTime = LocalDateTime.now();

  @UpdateTimestamp
  @Column(
      columnDefinition =
          "DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间'")
  private LocalDateTime updateTime = LocalDateTime.now();
}
