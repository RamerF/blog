package org.ramer.admin.system.controller.common;

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
import org.ramer.admin.system.service.common.CommonService;
import org.ramer.admin.system.service.common.ManagerService;
import org.ramer.admin.system.util.TextUtil;
import org.ramer.admin.system.validator.common.ManagerValidator;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.*;

@Slf4j
@Controller("managermc")
@PreAuthorize("hasAnyAuthority('global:read','manager:read')")
@RequestMapping(AccessPath.MANAGE + "/common/manager")
@Api(tags = "管理员接口")
@SuppressWarnings("UnusedDeclaration")
public class ManagerController {
  @Resource private ManagerService service;
  @Resource private CommonService commonService;
  @Resource private ManagerValidator validator;

  @InitBinder
  void initBinder(WebDataBinder binder) {
    binder.addValidators(validator);
  }

  @GetMapping("/index")
  @ApiOperation("管理员页面")
  public String index() {
    return "manager/index";
  }

  @GetMapping("/list")
  @ResponseBody
  @ApiOperation("获取管理员列表")
  public ResponseEntity list(
      @RequestParam(value = "page", required = false, defaultValue = "1") String pageStr,
      @RequestParam(value = "size", required = false, defaultValue = "10") String sizeStr,
      @ApiParam("查询条件") @RequestParam(value = "criteria", required = false) String criteria) {
    final int[] pageAndSize = TextUtil.validFixPageAndSize(pageStr, sizeStr);
    return commonService.page(
        service.page(criteria, pageAndSize[0], pageAndSize[1]), ManagerResponse::of);
  }

  @GetMapping
  @ApiOperation("添加管理员页面")
  public String create() {
    return "manager/create";
  }

  @PostMapping
  @ResponseBody
  @PreAuthorize("hasAnyAuthority('global:create','manager:create')")
  @ApiOperation("添加管理员")
  public ResponseEntity create(@Valid ManagerRequest managerRequest, BindingResult bindingResult)
      throws Exception {
    log.info(" ManagerController.create : [{}]", managerRequest);
    return commonService.create(service, Manager.class, managerRequest, bindingResult);
  }

  @GetMapping("/{id}")
  @ApiOperation("更新管理员页面")
  public String update(@PathVariable("id") String idStr, Map<String, Object> map) throws Exception {
    return commonService.update(
        service, ManagerPoJo.class, idStr, "manager/update", map, "manager");
  }

  @PutMapping("/{id}")
  @ResponseBody
  @PreAuthorize("hasAnyAuthority('global:write','manager:write')")
  @ApiOperation("更新管理员")
  public ResponseEntity update(
      @PathVariable("id") String idStr,
      @Valid ManagerRequest managerRequest,
      BindingResult bindingResult)
      throws Exception {
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
  public ResponseEntity delete(@PathVariable("id") String idStr) throws Exception {
    log.info(" ManagerController.delete : [{}]", idStr);
    return commonService.delete(service, idStr);
  }
}
