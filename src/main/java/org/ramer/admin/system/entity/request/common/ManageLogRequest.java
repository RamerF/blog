package org.ramer.admin.system.entity.request.common;

import lombok.*;
import org.ramer.admin.system.entity.request.AbstractEntityRequest;

/**
 * 管理端日志.
 *
 * @author ramer
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class ManageLogRequest extends AbstractEntityRequest {

  private String url;

  private String ip;

  private String result;

  private Long managerId;

}
