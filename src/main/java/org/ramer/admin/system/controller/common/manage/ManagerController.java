package org.ramer.admin.system.controller.common.manage;

import io.swagger.annotations.*;
import java.util.Map;
import javax.annotation.Resource;
import javax.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.ramer.admin.system.entity.Constant.AccessPath;
import org.ramer.admin.system.entity.domain.common.Manager;
import org.ramer.admin.system.entity.pojo.common.ManagerPoJo;
import org.ramer.admin.system.entity.request.common.ManagerRequest;
import org.ramer.admin.system.entity.response.CommonResponse;
import org.ramer.admin.system.entity.response.common.ManagerResponse;
import org.ramer.admin.system.service.common.*;
import org.ramer.admin.system.util.TextUtil;
import org.ramer.admin.system.validator.common.ManagerValidator;
import org.springframework.data.domain.PageImpl;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.*;
import springfox.documentation.annotations.ApiIgnore;

@Slf4j
@Controller
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
  public String index(@ApiIgnore Map<String, Object> map) {
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
  public String create() {
    return "manage/manager/create";
  }

  @PostMapping
  @ResponseBody
  @PreAuthorize("hasAnyAuthority('global:create','manager:create')")
  @ApiOperation("添加管理员")
  public ResponseEntity create(@Valid ManagerRequest managerRequest, BindingResult bindingResult) {
    log.info(" ManagerController.create : [{}]", managerRequest);
    return commonService.create(service, Manager.class, managerRequest, bindingResult);
  }

  @GetMapping("/{id}")
  @ApiOperation("更新管理员页面")
  public String update(@PathVariable("id") String idStr, Map<String, Object> map) {
    return commonService.update(
        service, ManagerPoJo.class, idStr, "manage/manager/update", map, "manager");
  }

  @PutMapping("/{id}")
  @ResponseBody
  @PreAuthorize("hasAnyAuthority('global:write','manager:write')")
  @ApiOperation("更新管理员")
  public ResponseEntity update(
      @PathVariable("id") String idStr,
      @Valid ManagerRequest managerRequest,
      BindingResult bindingResult) {
    log.info(" ManagerController.update : [{}]", managerRequest);
    final long id = TextUtil.validLong(idStr, -1);
    if (id < 1) {
      return CommonResponse.wrongFormat("id");
    }
    managerRequest.setId(id);
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
}
