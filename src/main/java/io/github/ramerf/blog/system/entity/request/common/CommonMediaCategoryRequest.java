package io.github.ramerf.blog.system.entity.request.common;

import lombok.*;
import io.github.ramerf.blog.system.entity.request.AbstractEntityRequest;

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
