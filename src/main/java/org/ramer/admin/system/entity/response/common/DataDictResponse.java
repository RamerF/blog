package org.ramer.admin.system.entity.response.common;

import org.ramer.admin.system.entity.domain.AbstractEntity;
import org.ramer.admin.system.entity.domain.common.DataDict;
import org.ramer.admin.system.entity.pojo.common.DataDictPoJo;
import java.math.BigDecimal;
import java.util.Date;
import java.util.Objects;
import java.util.Optional;
import lombok.*;
import org.springframework.beans.BeanUtils;

/**
 * 数据字典.
 *
 * @author ramer
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class DataDictResponse {

  private String name;

  private String code;

  private String remark;

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
