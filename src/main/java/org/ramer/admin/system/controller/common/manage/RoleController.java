package org.ramer.admin.system.controller.common.manage;

import io.swagger.annotations.*;
import java.util.List;
import java.util.Map;
import javax.annotation.Resource;
import javax.servlet.http.HttpSession;
import javax.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.ramer.admin.system.entity.Constant.AccessPath;
import org.ramer.admin.system.entity.domain.common.Role;
import org.ramer.admin.system.entity.pojo.common.RolePoJo;
import org.ramer.admin.system.entity.request.common.RoleRequest;
import org.ramer.admin.system.entity.response.CommonResponse;
import org.ramer.admin.system.entity.response.common.RoleResponse;
import org.ramer.admin.system.service.common.CommonService;
import org.ramer.admin.system.service.common.RoleService;
import org.ramer.admin.system.util.TextUtil;
import org.ramer.admin.system.validator.common.RoleValidator;
import org.springframework.data.domain.PageImpl;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.*;
import springfox.documentation.annotations.ApiIgnore;

@Slf4j
@Controller("rolec")
@PreAuthorize("hasAnyAuthority('global:read','role:read')")
@RequestMapping(AccessPath.MANAGE + "/role")
@Api(tags = "管理端: 角色接口")
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
  public String index(@ApiIgnore HttpSession session, @ApiIgnore Map<String, Object> map) {
    commonService.writeMenuAndSiteInfo(session, map);
    return "manage/role/index";
  }

  @GetMapping("/page")
  @ResponseBody
  @ApiOperation("获取角色列表")
  public ResponseEntity<CommonResponse<PageImpl<RoleResponse>>> page(
      @ApiParam("页号,从1开始,当page=size=-1时,表示不分页")
          @RequestParam(value = "page", required = false, defaultValue = "1")
          String pageStr,
      @RequestParam(value = "size", required = false, defaultValue = "10") String sizeStr,
      @ApiParam("查询条件") @RequestParam(value = "criteria", required = false) String criteria) {
    final int[] pageAndSize = TextUtil.validFixPageAndSize(pageStr, sizeStr);
    return commonService.page(
        service.page(criteria, pageAndSize[0], pageAndSize[1]), RoleResponse::of);
  }

  @GetMapping
  @ApiOperation("添加角色页面")
  public String create(@ApiIgnore HttpSession session, @ApiIgnore Map<String, Object> map) {
    commonService.writeMenuAndSiteInfo(session, map);
    return "manage/role/edit";
  }

  @PostMapping
  @ResponseBody
  @PreAuthorize("hasAnyAuthority('global:create','role:create')")
  @ApiOperation("添加角色")
  public ResponseEntity create(@Valid RoleRequest roleRequest, BindingResult bindingResult) {
    log.info(" RoleController.create : [{}]", roleRequest);
    return commonService.create(service, Role.class, roleRequest, bindingResult);
  }

  @GetMapping("/{id}")
  @ApiOperation("更新角色页面")
  public String update(
      @PathVariable("id") String idStr,
      @ApiIgnore HttpSession session,
      @ApiIgnore Map<String, Object> map) {
    commonService.writeMenuAndSiteInfo(session, map);
    return commonService.update(service, RolePoJo.class, idStr, "manage/role/edit", map, "role");
  }

  @PutMapping("/{id}")
  @ResponseBody
  @PreAuthorize("hasAnyAuthority('global:write','role:write')")
  @ApiOperation("更新角色")
  public ResponseEntity update(
      @PathVariable("id") String idStr,
      @Valid RoleRequest roleRequest,
      BindingResult bindingResult) {
    log.info(" RoleController.update : [{}]", roleRequest);
    final long id = TextUtil.validLong(idStr, -1);
    if (id < 1) {
      return CommonResponse.wrongFormat("id");
    }
    roleRequest.setId(id);
    return commonService.update(service, Role.class, roleRequest, idStr, bindingResult);
  }

  @DeleteMapping("/{id}")
  @ResponseBody
  @PreAuthorize("hasAnyAuthority('global:delete','role:delete')")
  @ApiOperation("删除角色")
  public ResponseEntity delete(@PathVariable("id") String idStr) {
    log.info(" RoleController.delete : [{}]", idStr);
    return commonService.delete(service, idStr);
  }

  @DeleteMapping("/deleteBatch")
  @ResponseBody
  @PreAuthorize("hasAnyAuthority('global:delete','config:delete')")
  @ApiOperation("删除角色批量")
  public ResponseEntity deleteBatch(@RequestParam("ids") List<Long> ids) {
    log.info(" ManagerController.deleteBatch : [{}]", ids);
    return commonService.deleteBatch(service, ids);
  }
}
