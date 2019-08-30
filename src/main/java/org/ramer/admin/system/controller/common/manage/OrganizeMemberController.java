package org.ramer.admin.system.controller.common.manage;

import io.swagger.annotations.Api;
import javax.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.ramer.admin.system.entity.Constant.AccessPath;
import org.ramer.admin.system.service.common.*;
import org.ramer.admin.system.validator.common.PostValidator;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Slf4j
@Controller("organizemcm")
@PreAuthorize("hasAnyAuthority('global:read','post:read')")
@RequestMapping(AccessPath.MANAGE + "/organize/member")
@Api(tags = "管理端: 组织成员接口")
@SuppressWarnings("UnusedDeclaration")
public class OrganizeMemberController {
  @Resource private ManagerService service;
  @Resource private CommonService commonService;
  @Resource private OrganizeService organizeService;
  @Resource private PostValidator validator;
}
