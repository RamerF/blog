package org.ramer.admin.system.entity.request.common;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import org.ramer.admin.system.entity.domain.common.DataDictType;
import org.ramer.admin.system.entity.request.AbstractEntityRequest;
import org.ramer.admin.system.entity.domain.AbstractEntity;
import java.math.BigDecimal;
import java.util.Objects;
import java.util.Date;
import java.util.List;
import lombok.*;

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
