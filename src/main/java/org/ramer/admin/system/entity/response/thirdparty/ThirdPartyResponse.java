package org.ramer.admin.system.entity.response.thirdparty;

import lombok.*;
import org.ramer.admin.system.entity.Constant.ResultCode;
import org.ramer.admin.system.entity.response.AbstractEntityResponse;

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
