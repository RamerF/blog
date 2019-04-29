package org.ramer.admin.system.entity.response.common;

import lombok.*;
import org.ramer.admin.system.entity.response.AbstractEntityResponse;

/**
 * 组织成员所属组织.
 *
 * @author ramer
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class OrganizeMemberRelationResponse extends AbstractEntityResponse {
  /** 成员id */
  private Long memberId;
  /** 成员名称 */
  private String memberName;
  /** 组织id */
  private Long organizeId;
  /** 组织名称 */
  private String organizeName;
}
