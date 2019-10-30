package org.ramer.admin.entity.request;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import org.ramer.admin.entity.domain.Article;
import org.ramer.admin.system.entity.request.AbstractEntityRequest;
import org.ramer.admin.system.entity.domain.AbstractEntity;
import java.math.BigDecimal;
import java.util.Objects;
import java.util.Date;
import java.util.List;
import lombok.*;

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
public class ArticleRequest extends AbstractEntityRequest {

  @ApiModelProperty(value = "String")
  @NonNull
  private String title;

  @ApiModelProperty(value = "String")
  private String content;

  @ApiModelProperty(value = "Long")
  private Long authorId;

}
