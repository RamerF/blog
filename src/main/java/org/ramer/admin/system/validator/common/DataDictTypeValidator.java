package org.ramer.admin.system.validator.common;

import java.util.Objects;
import javax.annotation.Nonnull;
import javax.annotation.Resource;
import org.ramer.admin.system.entity.domain.common.DataDictType;
import org.ramer.admin.system.entity.pojo.common.DataDictTypePoJo;
import org.ramer.admin.system.entity.request.common.DataDictTypeRequest;
import org.ramer.admin.system.service.common.DataDictTypeService;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

/** @author ramer */
@Component
public class DataDictTypeValidator implements Validator {
  @Resource private DataDictTypeService service;

  @Override
  public boolean supports(final Class<?> clazz) {
    return clazz.isAssignableFrom(DataDictType.class)
        || clazz.isAssignableFrom(DataDictTypeRequest.class)
        || clazz.isAssignableFrom(DataDictTypePoJo.class);
  }

  @Override
  public void validate(final Object target, @Nonnull final Errors errors) {
    DataDictTypeRequest dataDictType = (DataDictTypeRequest) target;
    if (dataDictType == null) {
      errors.rejectValue(null, "dataDictType.null", "数据字典分类 不能为空");
    } else {
      final String code = dataDictType.getCode();
      if (StringUtils.isEmpty(code) || code.length() > 50) {
        errors.rejectValue("code", "dataDictType.code.length", "标识 不能为空且小于50个字符");
      } else {
        final DataDictType exist = service.getByCode(code);
        if (Objects.nonNull(exist) && !Objects.equals(dataDictType.getId(), exist.getId())) {
          errors.rejectValue("code", "dataDictType.code.exist", "标识 已存在");
        }
      }
      final String name = dataDictType.getName();
      if (StringUtils.isEmpty(name) || name.length() > 50) {
        errors.rejectValue("name", "dataDictType.name.length", "名称 不能为空且小于50个字符");
      }
      final String remark = dataDictType.getRemark();
      if (!StringUtils.isEmpty(remark) && remark.length() > 100) {
        errors.rejectValue("remark", "dataDictType.remark.length", "备注 长度不能大于100个字符");
      }
    }
  }
}
