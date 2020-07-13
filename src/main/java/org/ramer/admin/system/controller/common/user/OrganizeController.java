package org.ramer.admin.system.controller.common.user;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import java.util.stream.Collectors;
import javax.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.ramer.admin.system.entity.Constant.AccessPath;
import org.ramer.admin.system.entity.response.Rs;
import org.ramer.admin.system.entity.response.common.OrganizeMemberResponse;
import org.ramer.admin.system.service.common.CommonService;
import org.ramer.admin.system.service.common.OrganizeService;
import org.ramer.admin.system.util.TextUtil;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController("organizeuc")
// @PreAuthorize("hasAnyAuthority('global:read','organize:read')")
@RequestMapping(AccessPath.USER + "/system/common/organize")
@Api(tags = "用户端: 组织接口")
@SuppressWarnings("UnusedDeclaration")
public class OrganizeController {
  @Resource private OrganizeService service;
  @Resource private CommonService commonService;

  @GetMapping("/listMembers/{id}")
  @ApiOperation("获取组织直属成员列表")
  public ResponseEntity list(@PathVariable("id") String idStr) {
    final long id = TextUtil.validLong(idStr, -1);
    if (id < 1) {
      return Rs.wrongFormat(idStr);
    }
    return Rs.ok(
        service.listMembers(id).stream()
            .map(OrganizeMemberResponse::of)
            .collect(Collectors.toList()));
  }
}
