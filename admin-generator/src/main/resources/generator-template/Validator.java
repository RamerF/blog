package ${basePath}${moduleName}.validator${subDir};

import ${basePath}${moduleName}.entity.domain${subDir}.${name};
import ${basePath}${moduleName}.entity.pojo${subDir}.${name}PoJo;
import ${basePath}${moduleName}.entity.request${subDir}.${name}Request;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;


/** @author ramer */
@Component
public class ${name}Validator implements Validator {
  @Override
  public boolean supports(final Class<?> clazz) {
    return clazz.isAssignableFrom(${name}.class)
        || clazz.isAssignableFrom(${name}Request.class)
        || clazz.isAssignableFrom(${name}PoJo.class);
  }

  @Override
  public void validate(final Object target, final Errors errors) {
    ${name}Request ${alia} = (${name}Request) target;
    if (${alia} == null) {
      errors.rejectValue(null, "${alia}.null", "${description} 不能为空");
    } else {
      // TODO-WARN: 添加${description}校验规则
    }
  }
}
