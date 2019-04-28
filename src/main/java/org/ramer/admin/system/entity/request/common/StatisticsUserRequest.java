package org.ramer.admin.system.entity.request.common;

import org.ramer.admin.system.entity.request.AbstractEntityRequest;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import java.util.Date;
import lombok.*;
import org.springframework.format.annotation.DateTimeFormat;

/**
 * 组织.
 *
 * @author ramer
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
@ApiModel("用户统计请求模型")
public class StatisticsUserRequest extends AbstractEntityRequest {
  @ApiModelProperty(example = "1", value = "供电所 id")
  private Long gongDianSuoId;

  @ApiModelProperty(example = "1", value = "变压器 id")
  private Long bianYaQiId;

  @ApiModelProperty(example = "1", value = "提交人 id")
  private Long submitterId;

  @ApiModelProperty(example = "2019-04-24 11:30:00", value = "开始日期")
  @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
  private Date startDate;

  @ApiModelProperty(example = "2019-04-24 11:30:00", value = "结束日期")
  @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
  private Date endDate;
}
