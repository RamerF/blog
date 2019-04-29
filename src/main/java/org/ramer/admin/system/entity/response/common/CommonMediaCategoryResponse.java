package org.ramer.admin.system.entity.response.common;

import org.ramer.admin.system.entity.domain.AbstractEntity;
import org.ramer.admin.system.entity.domain.common.CommonMediaCategory;
import org.ramer.admin.system.entity.pojo.common.CommonMediaCategoryPoJo;
import java.math.BigDecimal;
import java.util.Date;
import java.util.Objects;
import java.util.Optional;
import lombok.*;
import org.springframework.beans.BeanUtils;

/**
 * 通用多媒体类别.
 *
 * @author ramer
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class CommonMediaCategoryResponse {

  private String code;

  private String name;

  private String remark;

  public static CommonMediaCategoryResponse of(final CommonMediaCategory mediaCategory) {
    if (Objects.isNull(mediaCategory)) {
      return null;
    }
    CommonMediaCategoryResponse poJo = new CommonMediaCategoryResponse();
    // TODO-WARN:  将 Domain 对象转换成 Response 对象
    BeanUtils.copyProperties(mediaCategory, poJo);
    return poJo;
  }
}
