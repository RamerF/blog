package org.ramer.admin.system.entity.pojo.common;

import org.ramer.admin.system.entity.domain.common.Role;
import org.ramer.admin.system.entity.pojo.AbstractEntityPoJo;
import lombok.Data;
import lombok.EqualsAndHashCode;

/** @author ramer created on 11/5/18 */
@Data
@EqualsAndHashCode(callSuper = true)
public class RolesPoJo extends AbstractEntityPoJo {
  private String name;
  private String remark;

  public static RolesPoJo of(Role roles) {
    return new RolesPoJo().of(roles, RolesPoJo.class);
  }
}
