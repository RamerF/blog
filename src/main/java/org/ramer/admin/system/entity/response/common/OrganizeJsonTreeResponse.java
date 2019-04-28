package org.ramer.admin.system.entity.response.common;

import org.ramer.admin.system.entity.domain.common.Organize;
import java.util.List;
import lombok.*;
import org.springframework.beans.BeanUtils;

/**
 * 组织json树.
 *
 * @author ramer
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class OrganizeJsonTreeResponse {
  private Long id;

  private String name;

  private Long prevId;

  private Long rootId;

  private String remark;

  private Boolean hasChild;

  List<OrganizeJsonTreeResponse> children;

  public static OrganizeJsonTreeResponse of(Organize organize) {
    OrganizeJsonTreeResponse response = new OrganizeJsonTreeResponse();
    BeanUtils.copyProperties(organize, response);
    return response;
  }
}
