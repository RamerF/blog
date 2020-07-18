package io.github.ramerf.blog.entity.pojo;

import io.github.ramerf.wind.core.entity.pojo.AbstractEntityPoJo;
import io.swagger.annotations.ApiModelProperty;
import java.math.BigInteger;
import javax.persistence.Column;
import javax.persistence.Entity;
import lombok.*;

/**
 * 文章.
 *
 * @author ramer
 */
@Entity(name = "article")
@Data
@AllArgsConstructor
@NoArgsConstructor
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

  @ApiModelProperty(value = "Long")
  @Column(name = "author_id")
  private Long authorId;

  @ApiModelProperty(value = "被查看次数")
  private BigInteger viewCount;

  @ApiModelProperty(value = "被收藏次数")
  private BigInteger favouriteCount;

  @ApiModelProperty(value = "被加星/点赞次数")
  private BigInteger starCount;
}
