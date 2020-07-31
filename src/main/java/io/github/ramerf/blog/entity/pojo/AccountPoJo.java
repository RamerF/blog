package io.github.ramerf.blog.entity.pojo;

import io.github.ramerf.wind.core.entity.pojo.AbstractEntityPoJo;
import io.swagger.annotations.ApiModelProperty;
import javax.persistence.Entity;
import lombok.*;

/**
 * 用户/账户.
 *
 * @author ramer
 */
@Entity(name = "account")
@Data
@AllArgsConstructor
@NoArgsConstructor
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
public class AccountPoJo extends AbstractEntityPoJo {
  @ApiModelProperty(name = "昵称", example = "昵称")
  private String nickName;

  @ApiModelProperty(name = "手机号", example = "18990000000")
  private String phone;

  @ApiModelProperty(name = "密码", example = "12345")
  private String password;
}
