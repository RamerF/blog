package org.ramer.admin.system.entity.domain.common;

import org.ramer.admin.system.entity.domain.AbstractEntity;
import javax.persistence.*;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.hibernate.annotations.Table;

@Entity(name = ManageLog.TABLE_NAME)
@Table(appliesTo = ManageLog.TABLE_NAME, comment = "管理端日志")
@Data
@EqualsAndHashCode(callSuper = true)
public class ManageLog extends AbstractEntity {
  public static final String TABLE_NAME = "manage_log";

  @Column(length = 200, columnDefinition = "varchar(200)")
  private String url;

  @OneToOne private Manager manager;

  @Column(length = 20, columnDefinition = "varchar(20)")
  private String ip;

  @Column(columnDefinition = "text")
  private String result;
}
