package org.ramer.admin.system.entity.response.common;

import lombok.*;

/**
 * 模块项统计.
 *
 * @author ramer @Date 4/9/2019
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
// @EqualsAndHashCode(callSuper = true)
public class ModuleItemStatisticsResponse {
  /** 模块项名称|台区名称|处理人名称 */
  private String name;
  /** 数量 */
  private long count;
}
