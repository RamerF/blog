package io.github.ramerf.blog.entity.request;

import io.github.ramerf.wind.core.entity.request.AbstractEntityRequest;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import java.util.List;
import lombok.*;
import io.github.ramerf.blog.entity.pojo.ArticlePoJo;

/**
 * 文章.
 *
 * @author ramer
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
@ApiModel("文章")
public class ArticleRequest extends AbstractEntityRequest<ArticlePoJo> {

  @ApiModelProperty(value = "Long", hidden = true)
  private Long number;

  @ApiModelProperty(value = "标签")
  private String title;

  @ApiModelProperty(value = "简述")
  private String description;

  @ApiModelProperty(value = "标签以,分割", example = "1,2,3")
  private List<Long> tagIds;

  @ApiModelProperty(value = "String")
  private String content;

  @ApiModelProperty(value = "Long")
  private Long authorId;
}
