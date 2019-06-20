package org.ramer.admin.system.entity.response.common;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import java.util.Objects;
import lombok.*;
import org.ramer.admin.system.entity.domain.common.DataDict;
import org.ramer.admin.system.entity.response.AbstractEntityResponse;
import org.springframework.beans.BeanUtils;

/**
 * 数据字典.
 *
 * @author ramer
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
@ApiModel("数据字典")
public class DataDictResponse extends AbstractEntityResponse {

  @ApiModelProperty(value = "CODE")
  private String code;

  @ApiModelProperty(value = "名称")
  private String name;

  @ApiModelProperty(value = "备注")
  private String remark;

  @ApiModelProperty(value = "类型Id")
  private Long dataDictTypeId;

  public static DataDictResponse of(final DataDict dataDict) {
    if (Objects.isNull(dataDict)) {
      return null;
    }
    DataDictResponse poJo = new DataDictResponse();
    // TODO-WARN:  将 Domain 对象转换成 Response 对象
    BeanUtils.copyProperties(dataDict, poJo);
    return poJo;
  }
}
