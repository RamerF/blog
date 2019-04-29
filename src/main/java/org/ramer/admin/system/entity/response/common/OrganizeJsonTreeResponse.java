package org.ramer.admin.system.entity.response.common;

import java.util.List;
import lombok.*;
import org.ramer.admin.system.entity.domain.common.Organize;
import org.ramer.admin.system.entity.response.AbstractEntityResponse;
import org.springframework.beans.BeanUtils;

/**
 * 组织json树.
 *
 * @author ramer
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class OrganizeJsonTreeResponse extends AbstractEntityResponse {
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
