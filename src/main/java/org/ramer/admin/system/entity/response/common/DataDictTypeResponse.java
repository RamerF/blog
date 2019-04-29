package org.ramer.admin.system.entity.response.common;

import java.util.Objects;
import lombok.*;
import org.ramer.admin.system.entity.domain.common.DataDictType;
import org.springframework.beans.BeanUtils;

/**
 * 数据字典类型.
 *
 * @author ramer
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class DataDictTypeResponse {

  private String code;

  private String name;

  private String remark;

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
