package io.github.ramerf.blog.system.entity.response.common;

import io.github.ramerf.wind.core.entity.response.AbstractEntityResponse;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import java.util.Objects;
import java.util.Optional;
import lombok.*;
import io.github.ramerf.blog.system.entity.domain.common.Post;
import io.github.ramerf.blog.system.entity.domain.common.Post.DataAccess;
import org.springframework.beans.BeanUtils;

/**
 * 岗位.
 *
 * @author ramer
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
@ApiModel(value = "岗位")
public class PostResponse extends AbstractEntityResponse {

  @ApiModelProperty(value = "名称")
  private String name;

  @ApiModelProperty(value = "数据权限")
  private Integer dataAccess;

  @ApiModelProperty(value = "数据权限秒速")
  private String dataAccessDesc;

  @ApiModelProperty(value = "组织id")
  private Long organizeId;

  public static PostResponse of(final Post post) {
    if (Objects.isNull(post)) {
      return null;
    }
    PostResponse poJo = new PostResponse();
    BeanUtils.copyProperties(post, poJo);
    poJo.setDataAccessDesc(
        Optional.ofNullable(post.getDataAccess()).map(DataAccess::desc).orElse(""));
    return poJo;
  }
}
