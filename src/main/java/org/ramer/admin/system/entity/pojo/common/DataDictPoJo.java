package org.ramer.admin.system.entity.pojo.common;

import java.util.Objects;
import java.util.Optional;
import lombok.*;
import org.ramer.admin.system.entity.domain.AbstractEntity;
import org.ramer.admin.system.entity.domain.common.DataDict;
import org.ramer.admin.system.entity.pojo.AbstractEntityPoJo;

/** 系统数据字典. */
@Data
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class DataDictPoJo extends AbstractEntityPoJo {
  private Long dataDictTypeId;
  private String name;
  private String code;
  private String remark;

  @Override
  @SuppressWarnings({"unchecked"})
  public <E extends AbstractEntity, T extends AbstractEntityPoJo> T of(
      final E entity, final Class<T> clazz) {
    if (Objects.isNull(entity)) {
      return null;
    }
    DataDictPoJo poJo = (DataDictPoJo) super.of(entity, clazz);
    DataDict obj = (DataDict) entity;
    poJo.setDataDictTypeId(
        Optional.ofNullable(obj.getDataDictType()).map(AbstractEntity::getId).orElse(null));
    return (T) poJo;
  }

  public static DataDictPoJo of(DataDict dataDict) {
    return new DataDictPoJo().of(dataDict, DataDictPoJo.class);
  }
}
