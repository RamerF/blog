package io.github.ramerf.blog.system.entity.request.common;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;
import lombok.*;
import io.github.ramerf.blog.system.entity.domain.AbstractEntity;
import io.github.ramerf.blog.system.entity.domain.common.CommonMedia;
import io.github.ramerf.blog.system.entity.domain.common.CommonMediaCategory;
import io.github.ramerf.blog.system.entity.request.AbstractEntityRequest;
import org.springframework.beans.BeanUtils;

/**
 * 通用多媒体.
 *
 * @author ramer
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class CommonMediaRequest extends AbstractEntityRequest {

  private String code;

  private String url;

  private String remark;

  private Long categoryId;

  @Override
  public <T extends AbstractEntity, E extends AbstractEntityRequest> void of(E entity, T domain) {
    if (!Objects.isNull(domain)) {
      CommonMediaRequest request = (CommonMediaRequest) entity;
      CommonMedia commonMedia = (CommonMedia) domain;
      commonMedia.setCategory(CommonMediaCategory.of(request.getCategoryId()));
    }
  }

  public static CommonMedia of(CommonMediaRequest request) {
    if (Objects.isNull(request)) {
      return null;
    }
    CommonMedia metaData = new CommonMedia();
    BeanUtils.copyProperties(request, metaData);
    new CommonMediaRequest().of(request, metaData);
    return metaData;
  }

  public static List<CommonMedia> of(List<CommonMediaRequest> requests) {
    return requests.stream().map(CommonMediaRequest::of).collect(Collectors.toList());
  }
}
