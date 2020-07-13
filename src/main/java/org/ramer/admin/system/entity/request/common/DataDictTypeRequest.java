package org.ramer.admin.system.entity.request.common;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import java.util.List;
import lombok.*;
import org.ramer.admin.system.entity.request.AbstractEntityRequest;

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
