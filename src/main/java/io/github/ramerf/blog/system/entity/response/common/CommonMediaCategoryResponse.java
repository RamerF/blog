package io.github.ramerf.blog.system.entity.response.common;

import io.github.ramerf.wind.core.entity.response.AbstractEntityResponse;
import java.util.Objects;
import lombok.*;
import io.github.ramerf.blog.system.entity.domain.common.CommonMediaCategory;
import org.springframework.beans.BeanUtils;

/**
 * 通用多媒体类别.
 *
 * @author ramer
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class CommonMediaCategoryResponse extends AbstractEntityResponse {

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
