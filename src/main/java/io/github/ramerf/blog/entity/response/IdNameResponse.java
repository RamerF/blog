package io.github.ramerf.blog.entity.response;

import io.github.ramerf.wind.core.entity.response.AbstractEntityResponse;
import io.swagger.annotations.ApiModel;
import lombok.*;

/**
 * 文章标签.
 *
 * @author ramer
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
@ApiModel(value = "文章标签")
public class IdNameResponse extends AbstractEntityResponse {

  private Long id;

  private String name;
}
