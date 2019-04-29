package org.ramer.admin.system.entity.request.common;

import io.swagger.annotations.ApiModel;
import java.util.List;
import lombok.*;
import org.ramer.admin.system.entity.request.AbstractEntityRequest;

/**
 * 角色.
 *
 * @author ramer
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
@ApiModel("角色")
public class RoleRequest extends AbstractEntityRequest {

  private String name;

  private String remark;

  private List<Long> menusIds;

  private List<Long> privilegesIds;

}
