package ${basePath}${moduleName}.entity.response${subDir};

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import ${basePath}.system.entity.domain.AbstractEntity;
import ${basePath}${moduleName}.entity.domain${subDir}.${name};
import ${basePath}${moduleName}.entity.pojo${subDir}.${name}PoJo;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import lombok.*;
import ${basePath}.system.entity.response.AbstractEntityResponse;
import org.springframework.beans.BeanUtils;

/**
 * ${description}.
 *
 * @author ramer
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
@ApiModel(value = "${description}")
public class ${name}Response extends AbstractEntityResponse {
${fieldList}
  public static ${name}Response of(final ${name} ${alia}) {
    if (Objects.isNull(${alia})) {
      return null;
    }
    ${name}Response poJo = new ${name}Response();
    // TODO-WARN: 将 Domain 对象转换成 Response 对象
    BeanUtils.copyProperties(${alia}, poJo);
    return poJo;
  }
}
