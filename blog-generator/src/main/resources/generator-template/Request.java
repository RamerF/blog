package ${basePath}${moduleName}.entity.request${subDir};

import io.github.ramerf.wind.core.entity.request.AbstractEntityRequest;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import ${basePath}${moduleName}.entity.domain${subDir}.${name};
import ${basePath}.system.entity.domain.AbstractEntity;
import java.math.BigDecimal;
import java.util.Objects;
import java.util.Date;
import java.util.List;
import lombok.*;

/**
 * ${description}.
 *
 * @author ramer
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
@ApiModel("${description}")
public class ${name}Request extends AbstractEntityRequest {
${fieldList}
}
