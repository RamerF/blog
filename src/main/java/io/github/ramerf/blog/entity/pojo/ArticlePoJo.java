package io.github.ramerf.blog.entity.pojo;

import com.vladsch.flexmark.html.HtmlRenderer;
import com.vladsch.flexmark.parser.Parser;
import com.vladsch.flexmark.profiles.pegdown.Extensions;
import com.vladsch.flexmark.profiles.pegdown.PegdownOptionsAdapter;
import com.vladsch.flexmark.util.options.DataHolder;
import io.github.ramerf.wind.core.annotation.TableInfo;
import io.github.ramerf.wind.core.entity.pojo.AbstractEntityPoJo;
import io.swagger.annotations.ApiModelProperty;
import javax.persistence.Column;
import lombok.*;

/**
 * 文章.
 *
 * @author ramer
 */
@TableInfo(name = "article")
@Data
@AllArgsConstructor
@NoArgsConstructor
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
public class ArticlePoJo extends AbstractEntityPoJo {

  @ApiModelProperty(value = "Long")
  private Long number;

  @ApiModelProperty(value = "String")
  private String title;

  @ApiModelProperty(value = "String")
  private String description;

  @ApiModelProperty(value = "标签,该字段主要用于查询")
  private String tagsStr;

  @ApiModelProperty(value = "String")
  private String content;

  @ApiModelProperty(value = "String")
  private String htmlContent;

  @ApiModelProperty(value = "Long")
  @Column(name = "author_id")
  private Long authorId;

  @ApiModelProperty(value = "被查看次数")
  private Long viewCount;

  @ApiModelProperty(value = "被收藏次数")
  private Long favouriteCount;

  @ApiModelProperty(value = "被加星/点赞次数")
  private Long starCount;

  public void setContent(final String content) {
    this.content = content;
    DataHolder dataHolder =
        PegdownOptionsAdapter.flexmarkOptions(true, Extensions.ALL_WITH_OPTIONALS);
    Parser parser = Parser.builder(dataHolder).build();
    HtmlRenderer renderer = HtmlRenderer.builder(dataHolder).build();
    setHtmlContent(renderer.render(parser.parse(content)));
  }
}
