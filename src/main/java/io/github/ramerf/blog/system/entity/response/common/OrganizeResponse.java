package io.github.ramerf.blog.system.entity.response.common;

import io.github.ramerf.wind.core.entity.response.AbstractEntityResponse;
import lombok.*;
import io.github.ramerf.blog.system.entity.domain.common.Organize;
import org.springframework.beans.BeanUtils;

/**
 * 组织.
 *
 * @author ramer
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class OrganizeResponse extends AbstractEntityResponse {

  private Long id;

  private String name;

  private Long prevId;

  private Long rootId;

  private Boolean hasChild;

  private String remark;

  private Boolean isDelete;

  public static OrganizeResponse of(Organize organize) {
    OrganizeResponse poJo = new OrganizeResponse();
    // TODO-WARN:  将 Domain 对象转换成 Response 对象
    BeanUtils.copyProperties(organize, poJo);
    return poJo;
  }
}
