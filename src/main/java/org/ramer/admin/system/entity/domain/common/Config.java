package org.ramer.admin.system.entity.domain.common;

import org.ramer.admin.system.entity.domain.AbstractEntity;
import javax.persistence.Column;
import javax.persistence.Entity;
import lombok.*;
import org.hibernate.annotations.Table;

@Entity(name = Config.TABLE_NAME)
@Table(appliesTo = Config.TABLE_NAME, comment = "系统配置")
@Data
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class Config extends AbstractEntity {
  public static final String TABLE_NAME = "config";

  @Column(nullable = false, length = 50, columnDefinition = "varchar(50) not null")
  private String code;

  @Column(nullable = false, length = 50, columnDefinition = "varchar(50) not null")
  private String name;

  @Column(nullable = false, columnDefinition = "text not null")
  private String value;

  @Column(columnDefinition = "text")
  private String remark;
}
