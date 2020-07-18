package io.github.ramerf.blog.system.entity.response.common;

import io.github.ramerf.wind.core.entity.response.AbstractEntityResponse;
import java.util.Objects;
import lombok.*;
import io.github.ramerf.blog.system.entity.domain.common.Role;
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
