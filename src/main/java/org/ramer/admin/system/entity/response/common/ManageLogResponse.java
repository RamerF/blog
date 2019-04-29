package org.ramer.admin.system.entity.response.common;

import java.util.Objects;
import lombok.*;
import org.ramer.admin.system.entity.domain.common.ManageLog;
import org.springframework.beans.BeanUtils;

/**
 * 管理端日志.
 *
 * @author ramer
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class ManageLogResponse {

  private String url;

  private String ip;

  private String result;

  private Long managerId;

  public static ManageLogResponse of(ManageLog manageLog) {
    if (Objects.isNull(manageLog)) {
      return null;
    }
    ManageLogResponse poJo = new ManageLogResponse();
    // TODO-WARN:  将 Domain 对象转换成 Response 对象
    BeanUtils.copyProperties(manageLog, poJo);
    return poJo;
  }
}
