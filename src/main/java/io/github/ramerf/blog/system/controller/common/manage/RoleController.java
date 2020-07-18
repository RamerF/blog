package io.github.ramerf.blog.system.controller.common.manage;

import io.github.ramerf.wind.core.entity.response.Rs;
import io.swagger.annotations.*;
import java.util.*;
import java.util.stream.Collectors;
import javax.annotation.Resource;
import javax.servlet.http.HttpSession;
import javax.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import io.github.ramerf.blog.system.entity.Constant.AccessPath;
import io.github.ramerf.blog.system.entity.Constant.Txt;
import io.github.ramerf.blog.system.entity.domain.AbstractEntity;
import io.github.ramerf.blog.system.entity.domain.common.*;
import io.github.ramerf.blog.system.entity.pojo.common.RolePoJo;
import io.github.ramerf.blog.system.entity.request.common.RoleRequest;
import io.github.ramerf.blog.system.entity.response.common.*;
import io.github.ramerf.blog.system.exception.CommonException;
import io.github.ramerf.blog.system.service.common.*;
import io.github.ramerf.blog.system.util.CollectionUtils;
import io.github.ramerf.blog.system.util.TextUtil;
import io.github.ramerf.blog.system.validator.common.RoleValidator;
import org.springframework.data.domain.PageImpl;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.*;
import springfox.documentation.annotations.ApiIgnore;

@Slf4j
@Controller("rolemc")
@PreAuthorize("hasAnyAuthority('global:read','manage:read','role:read')")
@RequestMapping(AccessPath.MANAGE + "/role")
@Api(tags = "管理端: 角色接口")
@SuppressWarnings("UnusedDeclaration")
public class RoleController {
  @Resource private RoleService service;
  @Resource private MenuService menuService;
  @Resource private CommonService commonService;
  @Resource private PrivilegeService privilegeService;
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
  public ResponseEntity<Rs<PageImpl<RoleResponse>>> page(
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
  public ResponseEntity<Rs<Object>> create(
      @Valid RoleRequest roleRequest, BindingResult bindingResult) {
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
  public ResponseEntity<Rs<Object>> update(
      @PathVariable("id") String idStr,
      @Valid RoleRequest roleRequest,
      BindingResult bindingResult) {
    log.info(" RoleController.update : [{}]", roleRequest);
    final long id = TextUtil.validLong(idStr, -1);
    if (id < 1) {
      return Rs.wrongFormat("id");
    }
    roleRequest.setId(id);
    return commonService.update(service, Role.class, roleRequest, idStr, bindingResult);
  }

  @DeleteMapping("/{id}")
  @ResponseBody
  @PreAuthorize("hasAnyAuthority('global:delete','role:delete')")
  @ApiOperation("删除角色")
  public ResponseEntity<Rs<Object>> delete(@PathVariable("id") String idStr) {
    log.info(" RoleController.delete : [{}]", idStr);
    return commonService.delete(service, idStr);
  }

  @DeleteMapping("/deleteBatch")
  @ResponseBody
  @PreAuthorize("hasAnyAuthority('global:delete','config:delete')")
  @ApiOperation("删除角色批量")
  public ResponseEntity<Rs<Object>> deleteBatch(@RequestParam("ids") List<Long> ids) {
    log.info(" RoleController.deleteBatch : [{}]", ids);
    return commonService.deleteBatch(service, ids);
  }

  @GetMapping("/{id}/menus")
  @ApiOperation("获取角色菜单")
  public String getMenus(@PathVariable("id") String idStr, @ApiIgnore Map<String, Object> map) {
    final long id = TextUtil.validLong(idStr, -1);
    if (TextUtil.nonValidId(id)) {
      throw CommonException.of("id 无效");
    }
    final Role role = service.getById(id);
    if (Objects.isNull(role)) {
      throw CommonException.of("id 无效");
    }
    map.put("ms", CollectionUtils.list(menuService.list(null), MenuResponse::of, null, null));
    map.put("withMenus", CollectionUtils.list(role.getMenus(), AbstractEntity::getId, null, null));
    return "manage/role/index::menus";
  }

  @GetMapping("/{id}/privileges")
  @ApiOperation("获取角色权限")
  public String getPrivileges(
      @PathVariable("id") String idStr, @ApiIgnore Map<String, Object> map) {
    final long id = TextUtil.validLong(idStr, -1);
    if (TextUtil.nonValidId(id)) {
      throw CommonException.of("id 无效");
    }
    final Role role = service.getById(id);
    if (Objects.isNull(role)) {
      throw CommonException.of("id 无效");
    }
    map.put(
        "privileges",
        CollectionUtils.list(privilegeService.list(null), PrivilegeResponse::of, null, null));
    map.put(
        "withPrivileges",
        CollectionUtils.list(role.getPrivileges(), AbstractEntity::getId, null, null));
    return "manage/role/index::privileges";
  }

  @PutMapping("/{id}/menus")
  @ApiOperation("更新角色菜单")
  public ResponseEntity<Rs<Object>> updateMenus(
      @PathVariable("id") String idStr,
      @RequestParam("menuIds") List<Long> menuIds,
      @ApiIgnore Map<String, Object> map) {
    log.info(" RoleController.updateMenus : [{},{}]", idStr, menuIds);
    final long id = TextUtil.validLong(idStr, -1);
    if (TextUtil.nonValidId(id)) {
      throw CommonException.of("id 无效");
    }
    final Role role = service.getById(id);
    if (Objects.isNull(role)) {
      throw CommonException.of("id 无效");
    }
    role.setMenus(menuIds.stream().map(Menu::of).collect(Collectors.toList()));
    service.update(role);
    return role.getId() > 0
        ? Rs.ok(role.getId(), Txt.SUCCESS_EXEC_UPDATE)
        : Rs.fail(Txt.FAIL_EXEC_UPDATE);
  }

  @PutMapping("/{id}/privileges")
  @ApiOperation("更新角色权限")
  public ResponseEntity<Rs<Object>> updatePrivileges(
      @PathVariable("id") String idStr,
      @RequestParam("privilegeIds") List<Long> privilegeIds,
      @ApiIgnore Map<String, Object> map) {
    log.info(" RoleController.updatePrivileges : [{},{}]", idStr, privilegeIds);
    final long id = TextUtil.validLong(idStr, -1);
    if (TextUtil.nonValidId(id)) {
      throw CommonException.of("id 无效");
    }
    final Role role = service.getById(id);
    if (Objects.isNull(role)) {
      throw CommonException.of("id 无效");
    }
    role.setPrivileges(privilegeIds.stream().map(Privilege::of).collect(Collectors.toList()));
    service.update(role);
    return role.getId() > 0
        ? Rs.ok(role.getId(), Txt.SUCCESS_EXEC_UPDATE)
        : Rs.fail(Txt.FAIL_EXEC_UPDATE);
  }
}
