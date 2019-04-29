package org.ramer.admin.system.entity.request.common;

import lombok.*;
import org.ramer.admin.system.entity.request.AbstractEntityRequest;

/**
 * 菜单.
 *
 * @author ramer
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class MenuRequest extends AbstractEntityRequest {

  private Boolean leaf;

  private String name;

  private String alia;

  private String url;

  private Integer sort;

  private String icon;

  private String remark;

  private Long parentId;

}
