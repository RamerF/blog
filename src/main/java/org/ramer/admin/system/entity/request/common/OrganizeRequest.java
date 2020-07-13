package org.ramer.admin.system.entity.request.common;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.*;
import org.ramer.admin.system.entity.request.AbstractEntityRequest;

/**
 * 组织.
 *
 * @author ramer
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
@ApiModel("组织")
public class OrganizeRequest extends AbstractEntityRequest {

  @ApiModelProperty(example = "名称", value = "名称", required = true)
  private String name;

  @ApiModelProperty(example = "1", value = "上级组织")
  private Long prevId;

  @ApiModelProperty(example = "备注", value = "备注")
  private String remark;
}
