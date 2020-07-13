package org.ramer.admin.system.entity.request.common;

import lombok.*;
import org.ramer.admin.system.entity.request.AbstractEntityRequest;

/**
 * 通用多媒体类别.
 *
 * @author ramer
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class CommonMediaCategoryRequest extends AbstractEntityRequest {

  private String code;

  private String name;

  private String remark;

}
