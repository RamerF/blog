package org.ramer.admin.entity.response;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import org.ramer.admin.system.entity.domain.AbstractEntity;
import org.ramer.admin.entity.domain.Article;
import org.ramer.admin.entity.pojo.ArticlePoJo;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import lombok.*;
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

  @ApiModelProperty(value = "String")
  private String title;

  @ApiModelProperty(value = "String")
  private String content;

  @ApiModelProperty(value = "Long")
  private Long authorId;

  public static ArticleResponse of(final Article article) {
    if (Objects.isNull(article)) {
      return null;
    }
    ArticleResponse poJo = new ArticleResponse();
    // TODO-WARN:  将 Domain 对象转换成 Response 对象
    BeanUtils.copyProperties(article, poJo);
    return poJo;
  }
}
