package org.ramer.admin.system.entity.response.common;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import java.util.Objects;
import lombok.*;
import org.ramer.admin.system.entity.domain.common.Post;
import org.ramer.admin.system.entity.response.AbstractEntityResponse;
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

  @ApiModelProperty(value = "String")
  private String name;

  @ApiModelProperty(value = "Integer")
  private Integer dataAccess;

  @ApiModelProperty(value = "Long")
  private Long organizeId;

  public static PostResponse of(final Post post) {
    if (Objects.isNull(post)) {
      return null;
    }
    PostResponse poJo = new PostResponse();
    // TODO-WARN:  将 Domain 对象转换成 Response 对象
    BeanUtils.copyProperties(post, poJo);
    return poJo;
  }
}
