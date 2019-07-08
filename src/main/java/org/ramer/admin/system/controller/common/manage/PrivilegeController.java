package org.ramer.admin.system.controller.common.manage;

import io.swagger.annotations.*;
import java.util.List;
import java.util.Map;
import javax.annotation.Resource;
import javax.servlet.http.HttpSession;
import javax.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.ramer.admin.system.entity.Constant.AccessPath;
import org.ramer.admin.system.entity.domain.common.Privilege;
import org.ramer.admin.system.entity.pojo.common.PrivilegePoJo;
import org.ramer.admin.system.entity.request.common.PrivilegeRequest;
import org.ramer.admin.system.entity.response.CommonResponse;
import org.ramer.admin.system.entity.response.common.PrivilegeResponse;
import org.ramer.admin.system.service.common.CommonService;
import org.ramer.admin.system.service.common.PrivilegeService;
import org.ramer.admin.system.util.TextUtil;
import org.ramer.admin.system.validator.common.PrivilegeValidator;
import org.springframework.data.domain.PageImpl;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.*;
import springfox.documentation.annotations.ApiIgnore;

@Slf4j
@Controller("privilegecm")
@PreAuthorize("hasAnyAuthority('global:read','privilege:read')")
@RequestMapping(AccessPath.MANAGE + "/privilege")
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
  public String index(@ApiIgnore HttpSession session, @ApiIgnore Map<String, Object> map) {
    commonService.writeMenuAndSiteInfo(session, map);
    return "manage/privilege/index";
  }

  @GetMapping("/page")
  @ResponseBody
  @ApiOperation("获取权限列表")
  public ResponseEntity<CommonResponse<PageImpl<PrivilegeResponse>>> page(
      @ApiParam("页号,从1开始,当page=size=-1时,表示不分页")
          @RequestParam(value = "page", required = false, defaultValue = "1")
          String pageStr,
      @RequestParam(value = "size", required = false, defaultValue = "10") String sizeStr,
      @ApiParam("查询条件") @RequestParam(value = "criteria", required = false) String criteria) {
    final int[] pageAndSize = TextUtil.validFixPageAndSize(pageStr, sizeStr);
    return commonService.page(
        service.page(criteria, pageAndSize[0], pageAndSize[1]), PrivilegeResponse::of);
  }

  @GetMapping
  @ApiOperation("添加权限页面")
  public String create(@ApiIgnore HttpSession session, @ApiIgnore Map<String, Object> map) {
    commonService.writeMenuAndSiteInfo(session, map);
    return "manage/privilege/edit";
  }

  @PostMapping
  @ResponseBody
  @PreAuthorize("hasAnyAuthority('global:create','privilege:create')")
  @ApiOperation("添加权限")
  public ResponseEntity<CommonResponse<Object>> create(
      @Valid PrivilegeRequest privilegeRequest, @ApiIgnore BindingResult bindingResult) {
    log.info(" PrivilegeController.create : [{}]", privilegeRequest);
    return commonService.create(service, Privilege.class, privilegeRequest, bindingResult);
  }

  @GetMapping("/{id}")
  @ApiOperation("更新权限页面")
  public String update(
      @PathVariable("id") String idStr,
      @ApiIgnore HttpSession session,
      @ApiIgnore Map<String, Object> map) {
    commonService.writeMenuAndSiteInfo(session, map);
    return commonService.update(
        service, PrivilegePoJo.class, idStr, "manage/privilege/edit", map, "privilege");
  }

  @PutMapping("/{id}")
  @ResponseBody
  @PreAuthorize("hasAnyAuthority('global:write','privilege:write')")
  @ApiOperation("更新权限")
  public ResponseEntity<CommonResponse<Object>> update(
      @PathVariable("id") String idStr,
      @Valid PrivilegeRequest privilegeRequest,
      @ApiIgnore BindingResult bindingResult) {
    log.info(" PrivilegeController.update : [{}]", privilegeRequest);
    final long id = TextUtil.validLong(idStr, -1);
    if (id < 1) {
      return CommonResponse.wrongFormat("id");
    }
    privilegeRequest.setId(id);
    return commonService.update(service, Privilege.class, privilegeRequest, idStr, bindingResult);
  }

  @DeleteMapping("/{id}")
  @ResponseBody
  @PreAuthorize("hasAnyAuthority('global:delete','privilege:delete')")
  @ApiOperation("删除权限")
  public ResponseEntity<CommonResponse<Object>> delete(@PathVariable("id") String idStr) {
    log.info(" PrivilegeController.delete : [{}]", idStr);
    return commonService.delete(service, idStr);
  }

  @DeleteMapping("/deleteBatch")
  @ResponseBody
  @PreAuthorize("hasAnyAuthority('global:delete','privilege:delete')")
  @ApiOperation("删除权限批量")
  public ResponseEntity<CommonResponse<Object>> deleteBatch(@RequestParam("ids") List<Long> ids) {
    log.info(" PrivilegeController.deleteBatch : [{}]", ids);
    return commonService.deleteBatch(service, ids);
  }
}
