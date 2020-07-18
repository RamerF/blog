package io.github.ramerf.blog.entity.request;

import io.github.ramerf.wind.core.entity.request.AbstractEntityRequest;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.*;
import io.github.ramerf.blog.entity.pojo.TagPoJo;

/**
 * 文章.
 *
 * @author ramer
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
@ApiModel("文章标签")
public class TagRequest extends AbstractEntityRequest<TagPoJo> {

  @ApiModelProperty(value = "名称", example = "name")
  private String name;
}
