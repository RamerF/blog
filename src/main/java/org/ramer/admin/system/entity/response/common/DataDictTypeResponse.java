package org.ramer.admin.system.entity.response.common;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import java.util.List;
import java.util.Objects;
import lombok.*;
import org.ramer.admin.system.entity.domain.common.DataDictType;
import org.ramer.admin.system.entity.response.AbstractEntityResponse;
import org.springframework.beans.BeanUtils;

/**
 * 数据字典分类.
 *
 * @author ramer
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
@ApiModel(value = "数据字典分类")
public class DataDictTypeResponse extends AbstractEntityResponse {

  @ApiModelProperty(value = "String")
  private String code;

  @ApiModelProperty(value = "String")
  private String name;

  @ApiModelProperty(value = "String")
  private String remark;

  @ApiModelProperty(value = "dataDicts")
  private List<Long> dataDictsIds;

  public static DataDictTypeResponse of(final DataDictType dataDictType) {
    if (Objects.isNull(dataDictType)) {
      return null;
    }
    DataDictTypeResponse poJo = new DataDictTypeResponse();
    // TODO-WARN:  将 Domain 对象转换成 Response 对象
    BeanUtils.copyProperties(dataDictType, poJo);
    return poJo;
  }
}
