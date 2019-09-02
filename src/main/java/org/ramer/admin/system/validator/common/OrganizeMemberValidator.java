package org.ramer.admin.system.validator.common;

import javax.annotation.Nonnull;
import org.ramer.admin.system.entity.request.common.OrganizeMemberRequest;
import org.ramer.admin.system.util.TextUtil;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

/** @author ramer */
@Component
public class OrganizeMemberValidator implements Validator {

  @Override
  public boolean supports(@Nonnull final Class<?> clazz) {
    return clazz.isAssignableFrom(OrganizeMemberRequest.class);
  }

  @Override
  public void validate(final Object target, @Nonnull final Errors errors) {
    OrganizeMemberRequest organizeMember = (OrganizeMemberRequest) target;
    if (organizeMember == null) {
      errors.rejectValue(null, "organizeMember.null", "组织成员 不能为空");
    } else {
      if (TextUtil.nonValidId(organizeMember.getMemberId())) {
        errors.rejectValue("memberId", "organizeMember.memberId.length", "成员 不能为空");
      }
      if (TextUtil.nonValidId(organizeMember.getOrganizeId())) {
        errors.rejectValue("organizeId", "organizeMember.organizeId.length", "组织 不能为空");
      }
      if (TextUtil.nonValidId(organizeMember.getPostId())) {
        errors.rejectValue("postId", "organizeMember.postId.length", "岗位 不能为空");
      }
    }
  }
}
