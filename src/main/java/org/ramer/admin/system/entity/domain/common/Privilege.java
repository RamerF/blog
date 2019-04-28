package org.ramer.admin.system.entity.domain.common;

import org.ramer.admin.system.entity.domain.AbstractEntity;
import javax.persistence.Column;
import javax.persistence.Entity;
import lombok.*;
import org.hibernate.annotations.Table;

@Entity(name = Privilege.TABLE_NAME)
@Table(appliesTo = Privilege.TABLE_NAME, comment = "权限")
@Data
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class Privilege extends AbstractEntity {
  public static final String TABLE_NAME = "privilege";
  /** 权限表达式. eg: global:view */
  @Column(length = 100, columnDefinition = "varchar(100) not null comment '权限表达式'")
  private String exp;

  @Column(length = 50, columnDefinition = "varchar(50)")
  private String remark;

  public static Privilege of(long id) {
    final Privilege privilege = new Privilege();
    privilege.setId(id);
    return privilege;
  }
}
