package org.ramer.admin.system.entity.request.common;

import org.ramer.admin.system.entity.request.AbstractEntityRequest;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import java.util.List;
import lombok.*;

/**
 * 组织.
 *
 * @author ramer
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
@ApiModel("组织成员/负责人")
public class OrganizeMemberRequest extends AbstractEntityRequest {
  @ApiModelProperty(example = "1", value = "成员/负责人 id数组", required = true)
  private List<Long> ids;

  @ApiModelProperty(example = "1", value = "操作类型: 0:删除,1:添加", required = true)
  private Integer bindingOperate;

  @ApiModelProperty(example = "0", value = "操作角色: 0:成员,1(非0):负责人")
  private Integer role;
}
