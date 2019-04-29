package org.ramer.admin.system.entity.request.common;

import org.ramer.admin.system.entity.domain.common.Manager;
import org.ramer.admin.system.entity.request.AbstractEntityRequest;
import org.ramer.admin.system.entity.domain.AbstractEntity;
import java.math.BigDecimal;
import java.util.Objects;
import java.util.Date;
import java.util.List;
import lombok.*;

/**
 * 管理员.
 *
 * @author ramer
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class ManagerRequest extends AbstractEntityRequest {

  private String empNo;

  private String password;

  private String name;

  private Integer gender;

  private String phone;

  private String avatar;

  private Integer active;

  private Date validDate;

}
