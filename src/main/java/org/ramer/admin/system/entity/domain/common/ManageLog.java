package org.ramer.admin.system.entity.domain.common;

import com.fasterxml.jackson.annotation.JsonBackReference;
import lombok.*;
import org.ramer.admin.system.entity.domain.AbstractEntity;
import javax.persistence.*;
import org.hibernate.annotations.Table;

@Entity(name = ManageLog.TABLE_NAME)
@Table(appliesTo = ManageLog.TABLE_NAME, comment = "管理端日志")
@Data
@ToString(exclude = {"manager"})
@EqualsAndHashCode(callSuper = true)
public class ManageLog extends AbstractEntity {
  public static final String TABLE_NAME = "manage_log";

  @Column(length = 200, columnDefinition = "VARCHAR(200)")
  private String url;

  @OneToOne @JsonBackReference private Manager manager;

  @Column(length = 20, columnDefinition = "VARCHAR(20)")
  private String ip;

  @Column(columnDefinition = "TEXT")
  private String result;
}
