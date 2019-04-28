package org.ramer.admin.system.entity.response.common;

import org.ramer.admin.system.entity.domain.common.Manager;
import java.util.Date;
import lombok.*;
import org.springframework.beans.BeanUtils;

/** @author ramer */
@Data
@AllArgsConstructor
@NoArgsConstructor
public final class ManagerResponse {
  private Long id;
  private String empNo;
  private String name;
  private Integer gender;
  private String phone;
  private String avatar;
  private Boolean active;
  private Date validDate;

  public static ManagerResponse of(Manager manager) {
    final ManagerResponse response = new ManagerResponse();
    BeanUtils.copyProperties(manager, response);
    return response;
  }
}
