package org.ramer.admin.system.entity.domain.common;

import javax.persistence.Column;
import javax.persistence.Entity;
import lombok.*;
import org.hibernate.annotations.Table;
import org.ramer.admin.system.entity.domain.AbstractEntity;

@Entity(name = Config.TABLE_NAME)
@Table(appliesTo = Config.TABLE_NAME, comment = "系统配置")
@Data
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class Config extends AbstractEntity {
  public static final String TABLE_NAME = "config";

  /** CODE */
  @Column(nullable = false, columnDefinition = "VARCHAR(50) NOT NULL COMMENT 'CODE'")
  private String code;

  /** 名称 */
  @Column(nullable = false, columnDefinition = "VARCHAR(50) NOT NULL COMMENT '名称'")
  private String name;

  /** 值 */
  @Column(nullable = false, columnDefinition = "VARCHAR(100) NOT NULL COMMENT '值'")
  private String value;

  /** 备注 */
  @Column(columnDefinition = "TEXT COMMENT '备注'")
  private String remark;
}
