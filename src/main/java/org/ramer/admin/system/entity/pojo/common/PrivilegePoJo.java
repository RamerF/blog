package org.ramer.admin.system.entity.pojo.common;

import org.ramer.admin.system.entity.domain.common.Privilege;
import org.ramer.admin.system.entity.pojo.AbstractEntityPoJo;
import lombok.*;

/** 系统数据字典 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class PrivilegePoJo extends AbstractEntityPoJo {
  /** 权限表达式. eg: global:view */
  private String exp;

  private String remark;

  public static PrivilegePoJo of(Privilege privilege) {
    return new PrivilegePoJo().of(privilege, PrivilegePoJo.class);
  }
}
