package org.ramer.admin.system.entity.response.common;

import org.ramer.admin.system.entity.domain.common.Manager;
import java.util.List;
import lombok.*;
import org.ramer.admin.system.entity.response.AbstractEntityResponse;
import org.springframework.beans.BeanUtils;

/**
 * 组织成员.
 *
 * @author ramer
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class OrganizeMemberResponse extends AbstractEntityResponse {

  private Long id;

  private String name;

  private String empNo;

  private String phone;

  private List<String> organizes;

  private boolean isLeader;

  public static OrganizeMemberResponse of(Manager manager) {
    OrganizeMemberResponse response = new OrganizeMemberResponse();
    BeanUtils.copyProperties(manager, response);
    return response;
  }
}
