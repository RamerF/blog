package org.ramer.admin.system.entity.request.common;

import io.swagger.annotations.ApiModel;
import org.ramer.admin.system.entity.domain.common.Privilege;
import org.ramer.admin.system.entity.request.AbstractEntityRequest;
import org.ramer.admin.system.entity.domain.AbstractEntity;
import java.math.BigDecimal;
import java.util.Objects;
import java.util.Date;
import java.util.List;
import lombok.*;

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

  private String remark;

}
