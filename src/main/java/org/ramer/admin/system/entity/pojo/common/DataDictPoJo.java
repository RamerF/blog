package org.ramer.admin.system.entity.pojo.common;

import java.util.Objects;
import java.util.Optional;
import lombok.*;
import org.ramer.admin.system.entity.domain.AbstractEntity;
import org.ramer.admin.system.entity.domain.common.DataDict;
import org.ramer.admin.system.entity.pojo.AbstractEntityPoJo;

/**
 * 数据字典.
 *
 * @author ramer
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class DataDictPoJo extends AbstractEntityPoJo {

  private String name;

  private String code;

  private String remark;

  private Long dataDictTypeId;

  @Override
  @SuppressWarnings({"unchecked"})
  public <E extends AbstractEntity, T extends AbstractEntityPoJo> T of(
      final E entity, final Class<T> clazz) {
    if (Objects.isNull(entity)) {
      return null;
    }
    DataDict obj = (DataDict) entity;
    DataDictPoJo poJo = (DataDictPoJo) super.of(entity, clazz);
    poJo.setDataDictTypeId(
        Optional.ofNullable(obj.getDataDictType()).map(AbstractEntity::getId).orElse(null));
    return (T) poJo;
  }

  public static DataDictPoJo of(final DataDict entity) {
    return new DataDictPoJo().of(entity, DataDictPoJo.class);
  }
}
