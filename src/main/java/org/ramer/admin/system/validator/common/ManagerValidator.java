package org.ramer.admin.system.validator.common;

import java.util.Objects;
import javax.annotation.Nonnull;
import javax.annotation.Resource;
import org.ramer.admin.system.entity.domain.common.Manager;
import org.ramer.admin.system.entity.pojo.common.ManagerPoJo;
import org.ramer.admin.system.entity.request.common.ManagerRequest;
import org.ramer.admin.system.service.common.ManagerService;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

/** @author ramer */
@Component
public class ManagerValidator implements Validator {
  @Resource private ManagerService service;

  @Override
  public boolean supports(@Nonnull final Class<?> clazz) {
    return clazz.isAssignableFrom(Manager.class)
        || clazz.isAssignableFrom(ManagerRequest.class)
        || clazz.isAssignableFrom(ManagerPoJo.class);
  }

  @Override
  public void validate(final Object target, @Nonnull final Errors errors) {
    ManagerRequest manager = (ManagerRequest) target;
    if (manager == null) {
      errors.rejectValue(null, "manager.null", "管理员 不能为空");
    } else {
      final String empNo = manager.getEmpNo();
      if (StringUtils.isEmpty(empNo) || empNo.length() > 15) {
        errors.rejectValue("empNo", "manager.empNo.length", "工号 不能为空且小于15位数字");
      } else {
        final Manager exist = service.getByEmpNo(empNo);
        if (Objects.nonNull(exist) && !Objects.equals(manager.getId(), exist.getId())) {
          errors.rejectValue("empNo", "manager.empNo.exist", "工号 已存在");
        }
      }
      final String name = manager.getName();
      if (StringUtils.isEmpty(name) || name.length() > 10) {
        errors.rejectValue("name", "manager.name.length", "名称 不能为空且小于10个字符");
      }
      final String phone = manager.getPhone();
      if (StringUtils.isEmpty(phone) || phone.length() > 11) {
        errors.rejectValue("phone", "manager.phone.length", "手机号码 不能为空且小于11个字符");
      }
      if (Objects.isNull(manager.getId()) && StringUtils.isEmpty(manager.getPassword())) {
        errors.rejectValue("password", "manager.password.length", "登录密码 不能为空且为6-18位字母,数字或下划线");
      }
    }
  }
}
