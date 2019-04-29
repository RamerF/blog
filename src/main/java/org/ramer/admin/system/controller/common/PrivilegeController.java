 package org.ramer.admin.system.controller.common;

import org.ramer.admin.system.entity.Constant.AccessPath;
import org.ramer.admin.system.entity.domain.common.Privilege;
import org.ramer.admin.system.entity.pojo.common.PrivilegePoJo;
import org.ramer.admin.system.entity.request.common.PrivilegeRequest;
import org.ramer.admin.system.entity.response.common.PrivilegeResponse;
import org.ramer.admin.system.entity.response.CommonResponse;
import org.ramer.admin.system.service.common.CommonService;
import org.ramer.admin.system.service.common.PrivilegeService;
import org.ramer.admin.system.util.TextUtil;
import org.ramer.admin.system.validator.common.PrivilegeValidator;
import io.swagger.annotations.*;
import java.util.Map;
import javax.annotation.Resource;
import javax.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.*;

@Slf4j
@Controller("privilegemc")
@PreAuthorize("hasAnyAuthority('global:read','privilege:read')")
@RequestMapping( "/common/privilege")
@Api(tags = "管理端: 权限接口")
@SuppressWarnings("UnusedDeclaration")
public class PrivilegeController {
  @Resource private PrivilegeService service;
  @Resource private CommonService commonService;
  @Resource private PrivilegeValidator validator;

  @InitBinder
  void initBinder(WebDataBinder binder) {
    binder.addValidators(validator);
  }

  @GetMapping("/index")
  @ApiOperation("权限页面")
  public String index() {
    return "privilege/index";
  }

  @GetMapping("/list")
  @ResponseBody
  @ApiOperation("获取权限列表")
  public ResponseEntity list(
      @RequestParam(value = "page", required = false, defaultValue = "1") String pageStr,
      @RequestParam(value = "size", required = false, defaultValue = "10") String sizeStr,
      @ApiParam("查询条件") @RequestParam(value = "criteria", required = false) String criteria) {
    final int[] pageAndSize = TextUtil.validFixPageAndSize(pageStr, sizeStr);
    return commonService.page(
        service.page(criteria, pageAndSize[0], pageAndSize[1]), PrivilegeResponse::of);
  }

  @GetMapping
  @ApiOperation("添加权限页面")
  public String create() {
    return "privilege/create";
  }

  @PostMapping
  @ResponseBody
  @PreAuthorize("hasAnyAuthority('global:create','privilege:create')")
  @ApiOperation("添加权限")
  public ResponseEntity create(@Valid PrivilegeRequest privilegeRequest, BindingResult bindingResult) throws Exception {
    log.info(" PrivilegeController.create : [{}]", privilegeRequest);
    return commonService.create(
        service, Privilege.class, privilegeRequest, bindingResult);
  }

  @GetMapping("/{id}")
  @ApiOperation("更新权限页面")
  public String update(@PathVariable("id") String idStr, Map<String, Object> map) throws Exception {
    return commonService.update(
        service,
        PrivilegePoJo.class,
        idStr,
        "privilege/update",
        map, "privilege");
  }

  @PutMapping("/{id}")
  @ResponseBody
  @PreAuthorize("hasAnyAuthority('global:write','privilege:write')")
  @ApiOperation("更新权限")
  public ResponseEntity update(
      @PathVariable("id") String idStr, @Valid PrivilegeRequest privilegeRequest, BindingResult bindingResult)
      throws Exception {
    log.info(" PrivilegeController.update : [{}]", privilegeRequest);
    final long id = TextUtil.validLong(idStr, -1);
    if (id < 1) {
      return CommonResponse.wrongFormat("id");
    }
    privilegeRequest.setId(id);
    return commonService.update(
        service, Privilege.class, privilegeRequest, idStr, bindingResult);
  }

  @DeleteMapping("/{id}")
  @ResponseBody
  @PreAuthorize("hasAnyAuthority('global:delete','privilege:delete')")
  @ApiOperation("删除权限")
  public ResponseEntity delete(@PathVariable("id") String idStr) throws Exception {
    log.info(" PrivilegeController.delete : [{}]", idStr);
    return commonService.delete(service, idStr);
  }
}
