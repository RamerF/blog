package io.github.ramerf.blog.system.entity.response.common;

import io.github.ramerf.wind.core.entity.response.AbstractEntityResponse;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import java.util.Objects;
import lombok.*;
import io.github.ramerf.blog.system.entity.domain.common.Config;
import org.springframework.beans.BeanUtils;

/**
 * 系统配置.
 *
 * @author ramer
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
@ApiModel(value = "系统配置")
public class ConfigResponse extends AbstractEntityResponse {

  @ApiModelProperty(value = "code")
  private String code;

  @ApiModelProperty(value = "名称")
  private String name;

  @ApiModelProperty(value = "值")
  private String value;

  @ApiModelProperty(value = "备注")
  private String remark;

  public static ConfigResponse of(final Config config) {
    if (Objects.isNull(config)) {
      return null;
    }
    ConfigResponse poJo = new ConfigResponse();
    // TODO-WARN:  将 Domain 对象转换成 Response 对象
    BeanUtils.copyProperties(config, poJo);
    return poJo;
  }
}
