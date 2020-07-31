package io.github.ramerf.blog.entity.request;

import io.github.ramerf.blog.entity.pojo.AccountPoJo;
import io.github.ramerf.wind.core.entity.request.AbstractEntityRequest;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.*;

/**
 * 账户.
 *
 * @author ramer
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
@ApiModel("账户")
public class AccountRequest extends AbstractEntityRequest<AccountPoJo> {

  @ApiModelProperty(value = "String")
  private String nickName;

  @ApiModelProperty(value = "String")
  private String phone;

  @ApiModelProperty(value = "String")
  private String password;
}
