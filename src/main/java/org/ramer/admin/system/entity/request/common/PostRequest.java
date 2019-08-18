package org.ramer.admin.system.entity.request.common;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.*;
import org.ramer.admin.system.entity.request.AbstractEntityRequest;

/**
 * 岗位.
 *
 * @author ramer
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
@ApiModel("岗位")
public class PostRequest extends AbstractEntityRequest {

  @ApiModelProperty(value = "String")
  private String name;

  @ApiModelProperty(value = "Integer")
  private Integer dataAccess;

  @ApiModelProperty(value = "Long")
  private Long organizeId;

}
