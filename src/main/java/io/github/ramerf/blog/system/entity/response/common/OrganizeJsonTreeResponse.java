package io.github.ramerf.blog.system.entity.response.common;

import io.github.ramerf.wind.core.entity.response.AbstractEntityResponse;
import java.util.List;
import lombok.*;
import io.github.ramerf.blog.system.entity.domain.common.Organize;
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
