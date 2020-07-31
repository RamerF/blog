package io.github.ramerf.blog.entity.pojo;

import io.github.ramerf.wind.core.entity.pojo.AbstractEntityPoJo;
import io.swagger.annotations.ApiModelProperty;
import javax.persistence.Entity;
import lombok.*;

/**
 * 用户浏览文章记录.
 *
 * @author ramer
 */
@Entity(name = "account_browser_article")
@Data
@AllArgsConstructor
@NoArgsConstructor
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
public class AccountBrowserArticlePoJo extends AbstractEntityPoJo {
  @ApiModelProperty(name = "账户id", example = "1")
  private Long accountId;

  @ApiModelProperty(name = "文章id", example = "1")
  private Long articleId;
}
