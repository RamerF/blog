package org.ramer.admin.system.entity.pojo.common;

import org.ramer.admin.system.entity.domain.common.Manager;
import org.ramer.admin.system.entity.pojo.AbstractEntityPoJo;
import java.util.Date;
import lombok.*;

/** @author ramer */
@Data
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
public final class ManagerPoJo extends AbstractEntityPoJo {
  private String empNo;
  private String password;
  private String name;
  private Integer gender;
  private String phone;
  private String avatar;
  private Boolean active;
  private Date validDate;

  public static ManagerPoJo of(Manager manager) {
    return new ManagerPoJo().of(manager, ManagerPoJo.class);
  }
}
