package io.github.ramerf.blog.system.controller.common.manage;

import io.github.ramerf.wind.core.entity.response.Rs;
import io.swagger.annotations.*;
import java.util.*;
import javax.annotation.Resource;
import javax.servlet.http.HttpSession;
import javax.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import io.github.ramerf.blog.system.entity.Constant.AccessPath;
import io.github.ramerf.blog.system.entity.Constant.PrivilegeEnum;
import io.github.ramerf.blog.system.entity.domain.common.Privilege;
import io.github.ramerf.blog.system.entity.pojo.common.PrivilegePoJo;
import io.github.ramerf.blog.system.entity.request.common.PrivilegeRequest;
import io.github.ramerf.blog.system.entity.response.common.PrivilegeResponse;
import io.github.ramerf.blog.system.service.common.CommonService;
import io.github.ramerf.blog.system.service.common.PrivilegeService;
import io.github.ramerf.blog.system.util.TextUtil;
import io.github.ramerf.blog.system.validator.common.PrivilegeValidator;
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
@PreAuthorize("hasAnyAuthority('global:read','manage:read','privilege:read')")
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
  public ResponseEntity<Rs<PageImpl<PrivilegeResponse>>> page(
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
  public ResponseEntity<Rs<Object>> create(
      @Valid PrivilegeRequest privilegeRequest, @ApiIgnore BindingResult bindingResult) {
    log.info(" PrivilegeController.create : [{}]", privilegeRequest);
    // 新增时,只需传递name属性,根据name自动生成五个对应的记录
    final String name = privilegeRequest.getName();
    final String alia = privilegeRequest.getExp();
    List<Privilege> privileges = new ArrayList<>();
    PrivilegeEnum.map()
        .forEach(
            (pName, pExp) -> {
              final Privilege p = new Privilege();
              p.setExp(alia + ":" + pName);
              p.setName(name + ":" + pExp);
              privileges.add(p);
            });
    service.createBatch(privileges);
    return Rs.ok();
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
  public ResponseEntity<Rs<Object>> update(
      @PathVariable("id") String idStr,
      @Valid PrivilegeRequest privilegeRequest,
      @ApiIgnore BindingResult bindingResult) {
    log.info(" PrivilegeController.update : [{}]", privilegeRequest);
    return commonService.update(service, Privilege.class, privilegeRequest, idStr, bindingResult);
  }

  @DeleteMapping("/{id}")
  @ResponseBody
  @PreAuthorize("hasAnyAuthority('global:delete','privilege:delete')")
  @ApiOperation("删除权限")
  public ResponseEntity<Rs<Object>> delete(@PathVariable("id") String idStr) {
    log.info(" PrivilegeController.delete : [{}]", idStr);
    return commonService.delete(service, idStr);
  }

  @DeleteMapping("/deleteBatch")
  @ResponseBody
  @PreAuthorize("hasAnyAuthority('global:delete','privilege:delete')")
  @ApiOperation("删除权限批量")
  public ResponseEntity<Rs<Object>> deleteBatch(@RequestParam("ids") List<Long> ids) {
    log.info(" PrivilegeController.deleteBatch : [{}]", ids);
    return commonService.deleteBatch(service, ids);
  }
}
