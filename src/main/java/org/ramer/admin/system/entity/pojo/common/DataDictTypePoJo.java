package org.ramer.admin.system.entity.pojo.common;

import io.swagger.annotations.ApiModelProperty;
import lombok.*;
import org.ramer.admin.system.entity.pojo.AbstractEntityPoJo;

/**
 * 数据字典分类.
 *
 * @author ramer
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class DataDictTypePoJo extends AbstractEntityPoJo {

  @ApiModelProperty(value = "标识")
  private String code;

  @ApiModelProperty(value = "名称")
  private String name;

  @ApiModelProperty(value = "备注")
  private String remark;
}
