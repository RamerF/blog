package org.ramer.admin.system.entity.request.common;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
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

  @ApiModelProperty(value = "CODE", example = "CONFIG_CODE", required = true)
  private String code;

  @ApiModelProperty(value = "名称", example = "名称", required = true)
  private String name;

  @ApiModelProperty(value = "值", example = "值", required = true)
  private String value;

  @ApiModelProperty(value = "备注", example = "备注")
  private String remark;
}
