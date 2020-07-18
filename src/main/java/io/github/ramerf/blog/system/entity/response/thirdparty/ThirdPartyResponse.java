package io.github.ramerf.blog.system.entity.response.thirdparty;

import io.github.ramerf.wind.core.entity.response.AbstractEntityResponse;
import lombok.*;
import io.github.ramerf.blog.system.entity.Constant.ResultCode;

/** @author ramer */
@Data
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class ThirdPartyResponse extends AbstractEntityResponse {
  private boolean success;
  private String message;
  private String errcode;
  private Object data;

  public ThirdPartyResponse(boolean success, ResultCode errorCode) {
    this.success = success;
    this.message = errorCode.toString();
    this.errcode = errorCode.name();
  }
}
