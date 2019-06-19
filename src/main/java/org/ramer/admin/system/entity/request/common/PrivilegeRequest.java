package org.ramer.admin.system.entity.request.common;

import io.swagger.annotations.ApiModel;
import lombok.*;
import org.ramer.admin.system.entity.request.AbstractEntityRequest;

/**
 * 权限.
 *
 * @author ramer
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
@ApiModel("权限")
public class PrivilegeRequest extends AbstractEntityRequest {

  private String exp;

  private String name;

}
