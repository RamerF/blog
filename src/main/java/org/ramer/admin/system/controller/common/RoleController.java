 package org.ramer.admin.system.controller.common;

import io.swagger.annotations.*;
import java.util.Map;
import javax.annotation.Resource;
import javax.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.ramer.admin.system.entity.domain.common.Role;
import org.ramer.admin.system.entity.pojo.common.RolePoJo;
import org.ramer.admin.system.entity.request.common.RoleRequest;
import org.ramer.admin.system.entity.response.CommonResponse;
import org.ramer.admin.system.entity.response.common.RoleResponse;
import org.ramer.admin.system.service.common.CommonService;
import org.ramer.admin.system.service.common.RoleService;
import org.ramer.admin.system.util.TextUtil;
import org.ramer.admin.system.validator.common.RoleValidator;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.*;

@Slf4j
@Controller("rolemc")
@PreAuthorize("hasAnyAuthority('global:read','role:read')")
@RequestMapping( "/common/role")
@Api(tags = "角色接口")
@SuppressWarnings("UnusedDeclaration")
public class RoleController {
  @Resource private RoleService service;
  @Resource private CommonService commonService;
  @Resource private RoleValidator validator;

  @InitBinder
  void initBinder(WebDataBinder binder) {
    binder.addValidators(validator);
  }

  @GetMapping("/index")
  @ApiOperation("角色页面")
  public String index() {
    return "role/index";
  }

  @GetMapping("/list")
  @ResponseBody
  @ApiOperation("获取角色列表")
  public ResponseEntity list(
      @RequestParam(value = "page", required = false, defaultValue = "1") String pageStr,
      @RequestParam(value = "size", required = false, defaultValue = "10") String sizeStr,
      @ApiParam("查询条件") @RequestParam(value = "criteria", required = false) String criteria) {
    final int[] pageAndSize = TextUtil.validFixPageAndSize(pageStr, sizeStr);
    return commonService.page(
        service.page(criteria, pageAndSize[0], pageAndSize[1]), RoleResponse::of);
  }

  @GetMapping
  @ApiOperation("添加角色页面")
  public String create() {
    return "role/create";
  }

  @PostMapping
  @ResponseBody
  @PreAuthorize("hasAnyAuthority('global:create','role:create')")
  @ApiOperation("添加角色")
  public ResponseEntity create(@Valid RoleRequest roleRequest, BindingResult bindingResult) throws Exception {
    log.info(" RoleController.create : [{}]", roleRequest);
    return commonService.create(
        service, Role.class, roleRequest, bindingResult);
  }

  @GetMapping("/{id}")
  @ApiOperation("更新角色页面")
  public String update(@PathVariable("id") String idStr, Map<String, Object> map) throws Exception {
    return commonService.update(
        service,
        RolePoJo.class,
        idStr,
        "role/update",
        map, "role");
  }

  @PutMapping("/{id}")
  @ResponseBody
  @PreAuthorize("hasAnyAuthority('global:write','role:write')")
  @ApiOperation("更新角色")
  public ResponseEntity update(
      @PathVariable("id") String idStr, @Valid RoleRequest roleRequest, BindingResult bindingResult)
      throws Exception {
    log.info(" RoleController.update : [{}]", roleRequest);
    final long id = TextUtil.validLong(idStr, -1);
    if (id < 1) {
      return CommonResponse.wrongFormat("id");
    }
    roleRequest.setId(id);
    return commonService.update(
        service, Role.class, roleRequest, idStr, bindingResult);
  }

  @DeleteMapping("/{id}")
  @ResponseBody
  @PreAuthorize("hasAnyAuthority('global:delete','role:delete')")
  @ApiOperation("删除角色")
  public ResponseEntity delete(@PathVariable("id") String idStr) throws Exception {
    log.info(" RoleController.delete : [{}]", idStr);
    return commonService.delete(service, idStr);
  }
}
