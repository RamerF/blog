package org.ramer.admin.system.entity.response.common;

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
public class DataDictResponse extends AbstractEntityResponse {

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
