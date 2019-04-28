package org.ramer.admin.system.entity.response.common;

import org.ramer.admin.system.entity.domain.common.Organize;
import lombok.*;
import org.springframework.beans.BeanUtils;

/**
 * 组织.
 *
 * @author ramer
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class OrganizeResponse {

  private Long id;

  private String name;

  private Long prevId;

  private Long rootId;

  private Boolean hasChild;

  private String remark;

  private Integer state;

  public static OrganizeResponse of(Organize organize) {
    OrganizeResponse poJo = new OrganizeResponse();
    // TODO-WARN:  将 Domain 对象转换成 Response 对象
    BeanUtils.copyProperties(organize, poJo);
    return poJo;
  }
}
