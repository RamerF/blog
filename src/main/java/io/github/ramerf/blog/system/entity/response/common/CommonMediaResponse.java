package io.github.ramerf.blog.system.entity.response.common;

import io.github.ramerf.wind.core.entity.response.AbstractEntityResponse;
import java.util.*;
import java.util.stream.Collectors;
import lombok.*;
import io.github.ramerf.blog.system.entity.domain.common.CommonMedia;
import org.springframework.beans.BeanUtils;
import org.springframework.util.CollectionUtils;

/**
 * 通用多媒体.
 *
 * @author ramer
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class CommonMediaResponse extends AbstractEntityResponse {

  private Long id;
  private Boolean isDelete;
  private Date createTime;
  private Date updateTime;

  private String code;

  private String url;

  private String remark;

  private Long categoryId;

  public static CommonMediaResponse of(final CommonMedia commonMedia) {
    if (Objects.isNull(commonMedia)) {
      return null;
    }
    CommonMediaResponse poJo = new CommonMediaResponse();
    // TODO-WARN:  将 Domain 对象转换成 Response 对象
    BeanUtils.copyProperties(commonMedia, poJo);
    return poJo;
  }

  public static List<CommonMediaResponse> of(List<CommonMedia> commonMedias) {
    if (CollectionUtils.isEmpty(commonMedias)) {
      return new ArrayList<>();
    }
    return commonMedias.stream().map(CommonMediaResponse::of).collect(Collectors.toList());
  }
}
