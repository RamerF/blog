package org.ramer.admin.entity.response;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import java.util.Objects;
import java.util.Optional;
import lombok.*;
import org.ramer.admin.entity.domain.Article;
import org.ramer.admin.system.entity.domain.common.Manager;
import org.ramer.admin.system.entity.response.AbstractEntityResponse;
import org.springframework.beans.BeanUtils;

/**
 * 文章.
 *
 * @author ramer
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
@ApiModel(value = "文章")
public class ArticleResponse extends AbstractEntityResponse {

  @ApiModelProperty(value = "编号")
  private Long number;

  @ApiModelProperty(value = "标题")
  private String title;

  @ApiModelProperty(value = "内容")
  private String content;

  @ApiModelProperty(value = "作者id")
  private Long authorId;

  @ApiModelProperty(value = "作者名称")
  private String authorName;

  public static ArticleResponse of(final Article article) {
    if (Objects.isNull(article)) {
      return null;
    }
    ArticleResponse poJo = new ArticleResponse();
    BeanUtils.copyProperties(article, poJo);
    poJo.setAuthorName(Optional.ofNullable(article.getAuthor()).map(Manager::getName).orElse(""));
    return poJo;
  }
}
