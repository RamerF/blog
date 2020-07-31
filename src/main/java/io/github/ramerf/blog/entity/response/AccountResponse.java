package io.github.ramerf.blog.entity.response;

import io.github.ramerf.blog.entity.domain.Account;
import io.github.ramerf.wind.core.entity.response.AbstractEntityResponse;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import java.util.Objects;
import lombok.*;
import org.springframework.beans.BeanUtils;

/**
 * 账户.
 *
 * @author ramer
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
@ApiModel(value = "账户")
public class AccountResponse extends AbstractEntityResponse {

  @ApiModelProperty(value = "String")
  private String nickName;

  @ApiModelProperty(value = "String")
  private String phone;

  @ApiModelProperty(value = "String")
  private String password;

  public static AccountResponse of(final Account account) {
    if (Objects.isNull(account)) {
      return null;
    }
    AccountResponse poJo = new AccountResponse();
    // TODO-WARN: 将 Domain 对象转换成 Response 对象
    BeanUtils.copyProperties(account, poJo);
    return poJo;
  }
}
