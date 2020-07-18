package io.github.ramerf.blog.system.entity.request.common;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import java.util.List;
import lombok.*;
import io.github.ramerf.blog.system.entity.request.AbstractEntityRequest;

/**
 * 数据字典分类.
 *
 * @author ramer
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
@ApiModel("数据字典分类")
public class DataDictTypeRequest extends AbstractEntityRequest {

  @ApiModelProperty(value = "String")
  private String code;

  @ApiModelProperty(value = "String")
  private String name;

  @ApiModelProperty(value = "String")
  private String remark;

  @ApiModelProperty(value = "dataDicts")
  private List<Long> dataDictsIds;

}
