package org.ramer.admin.system.entity.pojo.common;

import io.swagger.annotations.ApiModelProperty;
import java.util.List;
import java.util.Objects;
import lombok.*;
import org.ramer.admin.system.entity.domain.AbstractEntity;
import org.ramer.admin.system.entity.domain.common.DataDictType;
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

  @ApiModelProperty(value = "String")
  private String code;

  @ApiModelProperty(value = "String")
  private String name;

  @ApiModelProperty(value = "String")
  private String remark;

  @ApiModelProperty(value = "dataDicts")
  private List<Long> dataDictsIds;

  @Override
  @SuppressWarnings({"unchecked"})
  public <E extends AbstractEntity, T extends AbstractEntityPoJo> T of(
      final E entity, final Class<T> clazz) {
    if (Objects.isNull(entity)) {
      return null;
    }
    DataDictType obj = (DataDictType) entity;
    DataDictTypePoJo poJo = (DataDictTypePoJo) super.of(entity, clazz);
    // TODO-WARN: 添加 Domain 转 PoJo 额外规则,赋值额外字段等
    // 例如: poJo.setXxxName(Optional.ofNullable(obj.getXxx()).map(Xxx::getName).orElse(null));
    return (T) poJo;
  }

  public static DataDictTypePoJo of(final DataDictType entity) {
    return new DataDictTypePoJo().of(entity, DataDictTypePoJo.class);
  }
}
