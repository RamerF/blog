package io.github.ramerf.blog.system.entity.response.common;

import io.github.ramerf.wind.core.entity.response.AbstractEntityResponse;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import java.util.Objects;
import java.util.Optional;
import lombok.*;
import io.github.ramerf.blog.system.entity.domain.common.DataDict;
import io.github.ramerf.blog.system.entity.domain.common.DataDictType;
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

  @ApiModelProperty(value = "分类Id")
  private Long dataDictTypeId;

  @ApiModelProperty(value = "分类名称")
  private String dataDictTypeName;

  public static DataDictResponse of(final DataDict dataDict) {
    if (Objects.isNull(dataDict)) {
      return null;
    }
    DataDictResponse poJo = new DataDictResponse();
    BeanUtils.copyProperties(dataDict, poJo);
    poJo.setDataDictTypeName(
        Optional.ofNullable(dataDict.getDataDictType()).map(DataDictType::getName).orElse(""));
    return poJo;
  }
}
