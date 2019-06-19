package org.ramer.admin.system.entity.response.common;

import java.util.Objects;
import lombok.*;
import org.ramer.admin.system.entity.domain.common.Privilege;
import org.ramer.admin.system.entity.response.AbstractEntityResponse;
import org.springframework.beans.BeanUtils;

/**
 * 权限.
 *
 * @author ramer
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class PrivilegeResponse extends AbstractEntityResponse {

  private String exp;

  private String name;

  public static PrivilegeResponse of(final Privilege privilege) {
    if (Objects.isNull(privilege)) {
      return null;
    }
    PrivilegeResponse poJo = new PrivilegeResponse();
    // TODO-WARN:  将 Domain 对象转换成 Response 对象
    BeanUtils.copyProperties(privilege, poJo);
    return poJo;
  }
}
