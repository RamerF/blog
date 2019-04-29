package org.ramer.admin.system.entity.request.common;

import lombok.*;
import org.ramer.admin.system.entity.request.AbstractEntityRequest;

/**
 * 系统配置.
 *
 * @author ramer
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class ConfigRequest extends AbstractEntityRequest {

  private String code;

  private String name;

  private String value;

  private String remark;

}
