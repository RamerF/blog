package org.ramer.admin.system.entity.request.common;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
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
@ApiModel("数据字典类型")
public class DataDictTypeRequest extends AbstractEntityRequest {

  @ApiModelProperty(value = "CODE")
  private String code;

  @ApiModelProperty(value = "名称")
  private String name;

  @ApiModelProperty(value = "备注")
  private String remark;
}
