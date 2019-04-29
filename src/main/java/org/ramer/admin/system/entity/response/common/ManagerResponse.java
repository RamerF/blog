package org.ramer.admin.system.entity.response.common;

import java.util.Date;
import java.util.Objects;
import lombok.*;
import org.ramer.admin.system.entity.domain.common.Manager;
import org.springframework.beans.BeanUtils;

/**
 * 管理员.
 *
 * @author ramer
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class ManagerResponse {

  private String empNo;

  private String password;

  private String name;

  private Integer gender;

  private String phone;

  private String avatar;

  private Integer active;

  private Date validDate;

  public static ManagerResponse of(Manager manager) {
    if (Objects.isNull(manager)) {
      return null;
    }
    ManagerResponse poJo = new ManagerResponse();
    // TODO-WARN:  将 Domain 对象转换成 Response 对象
    BeanUtils.copyProperties(manager, poJo);
    return poJo;
  }
}
