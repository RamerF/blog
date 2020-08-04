package io.github.ramerf.blog.entity.response;

import io.github.ramerf.blog.entity.pojo.TagPoJo;
import io.github.ramerf.wind.core.entity.response.AbstractEntityResponse;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import java.util.Objects;
import lombok.*;
import org.springframework.beans.BeanUtils;

/**
 * 文章标签.
 *
 * @author ramer
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
@ApiModel(value = "文章标签")
public class TagResponse extends AbstractEntityResponse {

  @ApiModelProperty(value = "String")
  private String name;

  @ApiModelProperty(value = "Long")
  private Long usingCount;

  public static TagResponse of(final TagPoJo tag) {
    if (Objects.isNull(tag)) {
      return null;
    }
    TagResponse poJo = new TagResponse();
    // TODO-WARN: 将 Domain 对象转换成 Response 对象
    BeanUtils.copyProperties(tag, poJo);
    return poJo;
  }
}
