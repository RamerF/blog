package io.github.ramerf.blog.entity.response;

import io.github.ramerf.blog.entity.pojo.TagPoJo;
import io.github.ramerf.wind.core.entity.response.AbstractEntityResponse;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import java.util.*;
import java.util.stream.Collectors;
import lombok.*;
import io.github.ramerf.blog.entity.pojo.ArticlePoJo;
import io.github.ramerf.blog.system.service.common.ManagerService;
import org.springframework.beans.BeanUtils;
import org.springframework.util.CollectionUtils;

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

  @ApiModelProperty(value = "html内容")
  private String htmlContent;

  @ApiModelProperty(value = "概述")
  private String description;

  @ApiModelProperty(value = "作者id")
  private Long authorId;

  @ApiModelProperty(value = "作者名称")
  private String authorName;

  @ApiModelProperty(value = "文章标签")
  private List<TagPoJo> tags;

  public static ArticleResponse of(final ArticlePoJo article, final ManagerService service) {
    if (Objects.isNull(article)) {
      return null;
    }
    ArticleResponse poJo = new ArticleResponse();
    BeanUtils.copyProperties(article, poJo);
    if (service != null) {
      poJo.setAuthorName(
          Optional.ofNullable(article.getAuthorId())
              .map(id -> service.getIdAndName(Collections.singletonList(id)).get(0).getName())
              .orElse(null));
    }
    return poJo;
  }

  public static List<ArticleResponse> of(
      final List<ArticlePoJo> articles, final ManagerService service) {
    if (CollectionUtils.isEmpty(articles)) {
      return new ArrayList<>();
    }
    final List<ArticleResponse> responses =
        articles.stream()
            .map(
                o -> {
                  ArticleResponse poJo = new ArticleResponse();
                  BeanUtils.copyProperties(o, poJo);
                  return poJo;
                })
            .collect(Collectors.toList());
    if (service != null) {
      final Map<Long, List<ArticleResponse>> authorMap =
          responses.stream().collect(Collectors.groupingBy(ArticleResponse::getAuthorId));
      service
          .getIdAndName(articles.stream().map(ArticlePoJo::getId).collect(Collectors.toList()))
          .forEach(
              manager ->
                  Optional.ofNullable(authorMap.get(manager.getId()))
                      .ifPresent(o -> o.forEach(obj -> obj.setAuthorName(manager.getName()))));
    }
    return responses;
  }
}
