package ${basePath}${moduleName}.entity.pojo${subDir};

import ${basePath}.system.entity.domain.AbstractEntity;
import ${basePath}${moduleName}.entity.domain${subDir}.${name};
import ${basePath}.system.entity.pojo.AbstractEntityPoJo;
import io.swagger.annotations.ApiModelProperty;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import lombok.*;
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
public class ${name}PoJo extends AbstractEntityPoJo {
${fieldList}
  @Override
  @SuppressWarnings({"unchecked"})
  public <E extends AbstractEntity, T extends AbstractEntityPoJo> T of(
      final E entity, final Class<T> clazz) {
    if (Objects.isNull(entity)) {
      return null;
    }
    ${name} obj = (${name}) entity;
    ${name}PoJo poJo = (${name}PoJo) super.of(entity, clazz);
    // TODO-WARN: 添加 Domain 转 PoJo 额外规则,赋值额外字段等
    // 例如: poJo.setXxxName(Optional.ofNullable(obj.getXxx()).map(Xxx::getName).orElse(null));
    return (T) poJo;
  }

  public static ${name}PoJo of(final ${name} entity) {
    return new ${name}PoJo().of(entity, ${name}PoJo.class);
  }
}
