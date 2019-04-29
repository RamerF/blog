package org.ramer.admin.system.entity.response.common;

import java.util.Objects;
import lombok.*;
import org.ramer.admin.system.entity.domain.common.ManageLog;
import org.ramer.admin.system.entity.response.AbstractEntityResponse;
import org.springframework.beans.BeanUtils;

/**
 * 管理端日志.
 *
 * @author ramer
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class ManageLogResponse extends AbstractEntityResponse {

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
