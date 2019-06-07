package org.ramer.admin.system.entity.request.common;

import io.swagger.annotations.ApiModel;
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
@ApiModel("系统配置")
public class ConfigRequest extends AbstractEntityRequest {

  private String code;

  private String name;

  private String value;

  private String remark;

}
