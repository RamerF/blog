package org.ramer.admin.system.entity.request.common;

import lombok.*;
import org.ramer.admin.system.entity.request.AbstractEntityRequest;

/**
 * 数据字典类型.
 *
 * @author ramer
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class DataDictTypeRequest extends AbstractEntityRequest {

  private String code;

  private String name;

  private String remark;
}
