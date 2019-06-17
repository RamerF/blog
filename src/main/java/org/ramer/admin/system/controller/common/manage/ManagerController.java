package org.ramer.admin.system.controller.common.manage;

import io.swagger.annotations.*;
import java.util.*;
import javax.annotation.Resource;
import javax.servlet.http.HttpSession;
import javax.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.ramer.admin.system.entity.Constant.AccessPath;
import org.ramer.admin.system.entity.domain.AbstractEntity;
import org.ramer.admin.system.entity.domain.common.Manager;
import org.ramer.admin.system.entity.pojo.common.ManagerPoJo;
import org.ramer.admin.system.entity.request.common.ManagerRequest;
import org.ramer.admin.system.entity.response.CommonResponse;
import org.ramer.admin.system.entity.response.common.ManagerResponse;
import org.ramer.admin.system.entity.response.common.RoleResponse;
import org.ramer.admin.system.exception.CommonException;
import org.ramer.admin.system.service.common.*;
import org.ramer.admin.system.util.*;
import org.ramer.admin.system.validator.common.ManagerValidator;
import org.springframework.data.domain.PageImpl;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.util.StringUtils;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.*;
import springfox.documentation.annotations.ApiIgnore;

@Slf4j
@Controller("MCCM")
@RequestMapping(AccessPath.MANAGE + "/manager")
@PreAuthorize("hasAnyAuthority('global:read','manager:read')")
@Api(tags = "管理端: 管理员接口")
@SuppressWarnings("UnusedDeclaration")
public class ManagerController {
  @Resource private ManagerService service;
  @Resource private MenuService menuService;
  @Resource private RoleService rolesService;
  @Resource private CommonService commonService;
  @Resource private ManagerValidator validator;

  @InitBinder
  void initBinder(WebDataBinder binder) {
    binder.addValidators(validator);
  }

  @GetMapping("/index")
  @ApiOperation("管理员管理页面")
  public String index(@ApiIgnore HttpSession session, @ApiIgnore Map<String, Object> map) {
    commonService.writeMenuAndSiteInfo(session, map);
    return "manage/manager/index";
  }

  @GetMapping("/page")
  @ResponseBody
  @ApiOperation("获取管理员列表")
  public ResponseEntity<CommonResponse<PageImpl<ManagerResponse>>> page(
      @ApiParam("页号,从1开始,当page=size=-1时,表示不分页")
          @RequestParam(value = "page", required = false, defaultValue = "1")
          String pageStr,
      @RequestParam(value = "size", required = false, defaultValue = "10") String sizeStr,
      @ApiParam("查询条件") @RequestParam(value = "criteria", required = false) String criteria) {
    final int[] pageAndSize = TextUtil.validFixPageAndSize(pageStr, sizeStr);
    return commonService.page(
        service.page(criteria, pageAndSize[0], pageAndSize[1]), ManagerResponse::of);
  }

  @GetMapping
  @ApiOperation("添加管理员页面")
  public String create(@ApiIgnore HttpSession session, @ApiIgnore Map<String, Object> map) {
    commonService.writeMenuAndSiteInfo(session, map);
    return "manage/manager/edit";
  }

  @PostMapping
  @ResponseBody
  @PreAuthorize("hasAnyAuthority('global:create','manager:create')")
  @ApiOperation("添加管理员")
  public ResponseEntity create(
      @Valid ManagerRequest managerRequest, @ApiIgnore BindingResult bindingResult) {
    log.info(" ManagerController.create : [{}]", managerRequest);
    managerRequest.setPassword(
        Optional.ofNullable(managerRequest.getPassword())
            .map(EncryptUtil::execEncrypt)
            .orElse(null));
    return commonService.create(service, Manager.class, managerRequest, bindingResult);
  }

  @GetMapping("/{id}")
  @ApiOperation("更新管理员页面")
  public String update(
      @PathVariable("id") String idStr,
      @ApiIgnore HttpSession session,
      @ApiIgnore Map<String, Object> map) {
    return commonService.update(
        service,
        ManagerPoJo.class,
        idStr,
        "manage/manager/edit",
        map,
        "manager",
        () -> commonService.writeMenuAndSiteInfo(session, map));
  }

  @PutMapping("/{id}")
  @ResponseBody
  @PreAuthorize("hasAnyAuthority('global:write','manager:write')")
  @ApiOperation("更新管理员")
  public ResponseEntity update(
      @PathVariable("id") String idStr,
      @Valid ManagerRequest managerRequest,
      @ApiIgnore BindingResult bindingResult) {
    log.info(" ManagerController.update : [{}]", managerRequest);
    // 如果修改了密码,需要先加密
    if (!StringUtils.isEmpty(managerRequest.getPassword())) {
      managerRequest.setPassword(EncryptUtil.execEncrypt(managerRequest.getPassword()));
    }
    return commonService.update(service, Manager.class, managerRequest, idStr, bindingResult);
  }

  @DeleteMapping("/{id}")
  @ResponseBody
  @PreAuthorize("hasAnyAuthority('global:delete','manager:delete')")
  @ApiOperation("删除管理员")
  public ResponseEntity delete(@PathVariable("id") String idStr) {
    log.info(" ManagerController.delete : [{}]", idStr);
    return commonService.delete(service, idStr);
  }

  @DeleteMapping("/deleteBatch")
  @ResponseBody
  @PreAuthorize("hasAnyAuthority('global:delete','config:delete')")
  @ApiOperation("删除管理员批量")
  public ResponseEntity deleteBatch(@RequestParam("ids") List<Long> ids) {
    log.info(" ManagerController.deleteBatch : [{}]", ids);
    return commonService.deleteBatch(service, ids);
  }

  @GetMapping("/{id}/roles")
  public String getRole(@PathVariable("id") String idStr, Map<String, Object> map) {
    final long id = TextUtil.validLong(idStr, -1);
    if (TextUtil.nonValidId(id)) {
      throw new CommonException("id 无效");
    }
    final Manager manager = service.getById(id);
    if (Objects.isNull(manager)) {
      throw new CommonException("id 无效");
    }
    map.put("roles", CollectionUtils.list(rolesService.list(null), RoleResponse::of, null, null));
    map.put(
        "withRoles", CollectionUtils.list(manager.getRoles(), AbstractEntity::getId, null, null));
    return "manage/manager/index::roles";
  }
}
