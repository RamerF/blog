package org.ramer.admin.system.validator.common;

import java.util.Objects;
import javax.annotation.Nonnull;
import javax.annotation.Resource;
import org.ramer.admin.system.entity.domain.common.DataDict;
import org.ramer.admin.system.entity.pojo.common.DataDictPoJo;
import org.ramer.admin.system.entity.request.common.DataDictRequest;
import org.ramer.admin.system.service.common.DataDictService;
import org.ramer.admin.system.service.common.DataDictTypeService;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

/** @author ramer */
@Component
public class DataDictValidator implements Validator {
  @Resource private DataDictService service;
  @Resource private DataDictTypeService typeService;

  @Override
  public boolean supports(final Class<?> clazz) {
    return clazz.isAssignableFrom(DataDict.class)
        || clazz.isAssignableFrom(DataDictRequest.class)
        || clazz.isAssignableFrom(DataDictPoJo.class);
  }

  @Override
  public void validate(final Object target, @Nonnull final Errors errors) {
    DataDictRequest dataDict = (DataDictRequest) target;
    if (dataDict == null) {
      errors.rejectValue(null, "dataDict.null", "数据字典 不能为空");
    } else {
      final String code = dataDict.getCode();
      if (StringUtils.isEmpty(code) || code.length() > 50) {
        errors.rejectValue("code", "dataDict.code.length", "CODE 不能为空且小于50个字符");
      } else {
        final DataDict exist = service.getByCode(code);
        if (Objects.nonNull(exist) && !Objects.equals(dataDict.getId(), exist.getId())) {
          errors.rejectValue("code", "dataDict.code.exist", "CODE 已存在");
        }
      }
      final String name = dataDict.getName();
      if (StringUtils.isEmpty(name) || name.length() > 50) {
        errors.rejectValue("name", "dataDict.name.length", "名称 不能为空且小于50个字符");
      }
      final Long dataDictTypeId = dataDict.getDataDictTypeId();
      if (Objects.isNull(dataDictTypeId)) {
        errors.rejectValue("value", "dataDict.value.length", "分类 不能为空");
      }
      final String remark = dataDict.getRemark();
      if (!StringUtils.isEmpty(remark) && remark.length() > 65535) {
        errors.rejectValue("remark", "dataDict.remark.length", "备注 长度不能大于65535个字符");
      }
    }
  }
}
