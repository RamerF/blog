package org.ramer.admin.system.entity.response.common;

import org.ramer.admin.system.entity.domain.AbstractEntity;
import org.ramer.admin.system.entity.domain.common.Role;
import org.ramer.admin.system.entity.pojo.common.RolePoJo;
import java.math.BigDecimal;
import java.util.Date;
import java.util.Objects;
import java.util.Optional;
import lombok.*;
import org.ramer.admin.system.entity.response.AbstractEntityResponse;
import org.springframework.beans.BeanUtils;

/**
 * 角色.
 *
 * @author ramer
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class RoleResponse extends AbstractEntityResponse {

  private String name;

  private String remark;

  public static RoleResponse of(final Role role) {
    if (Objects.isNull(role)) {
      return null;
    }
    RoleResponse poJo = new RoleResponse();
    // TODO-WARN:  将 Domain 对象转换成 Response 对象
    BeanUtils.copyProperties(role, poJo);
    return poJo;
  }
}
