package org.ramer.admin.system.controller.common.manage;

import io.swagger.annotations.*;
import java.util.List;
import java.util.Map;
import javax.annotation.Resource;
import javax.servlet.http.HttpSession;
import javax.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.ramer.admin.system.entity.Constant.AccessPath;
import org.ramer.admin.system.entity.domain.common.Config;
import org.ramer.admin.system.entity.pojo.common.ConfigPoJo;
import org.ramer.admin.system.entity.request.common.ConfigRequest;
import org.ramer.admin.system.entity.response.CommonResponse;
import org.ramer.admin.system.entity.response.common.ConfigResponse;
import org.ramer.admin.system.service.common.CommonService;
import org.ramer.admin.system.service.common.ConfigService;
import org.ramer.admin.system.util.TextUtil;
import org.ramer.admin.system.validator.common.ConfigValidator;
import org.springframework.data.domain.PageImpl;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.*;
import springfox.documentation.annotations.ApiIgnore;

@Slf4j
@Controller("configcm")
@PreAuthorize("hasAnyAuthority('global:read','config:read')")
@RequestMapping(AccessPath.MANAGE + "/config")
@Api(tags = "管理端: 系统配置接口")
@SuppressWarnings("UnusedDeclaration")
public class ConfigController {
  @Resource private ConfigService service;
  @Resource private CommonService commonService;
  @Resource private ConfigValidator validator;

  @InitBinder
  void initBinder(WebDataBinder binder) {
    binder.addValidators(validator);
  }

  @GetMapping("/index")
  @ApiOperation("系统配置页面")
  public String index(@ApiIgnore HttpSession session, @ApiIgnore Map<String, Object> map) {
    commonService.writeMenuAndSiteInfo(session, map);
    return "manage/config/index";
  }

  @GetMapping("/page")
  @ResponseBody
  @ApiOperation("获取系统配置列表")
  public ResponseEntity<CommonResponse<PageImpl<ConfigResponse>>> page(
      @ApiParam("页号,从1开始,当page=size=-1时,表示不分页")
          @RequestParam(value = "page", required = false, defaultValue = "1")
          String pageStr,
      @RequestParam(value = "size", required = false, defaultValue = "10") String sizeStr,
      @ApiParam("模糊查询条件") @RequestParam(value = "criteria", required = false) String criteria) {
    final int[] pageAndSize = TextUtil.validFixPageAndSize(pageStr, sizeStr);
    return commonService.page(
        service.page(criteria, pageAndSize[0], pageAndSize[1]), ConfigResponse::of);
  }

  @GetMapping
  @ApiOperation("添加系统配置页面")
  public String create(@ApiIgnore HttpSession session, @ApiIgnore Map<String, Object> map) {
    commonService.writeMenuAndSiteInfo(session, map);
    return "manage/config/edit";
  }

  @PostMapping
  @ResponseBody
  @PreAuthorize("hasAnyAuthority('global:create','config:create')")
  @ApiOperation("添加系统配置")
  public ResponseEntity create(
      @Valid ConfigRequest configRequest, @ApiIgnore BindingResult bindingResult) {
    log.info(" ConfigController.create : [{}]", configRequest);
    return commonService.create(service, Config.class, configRequest, bindingResult);
  }

  @GetMapping("/{id}")
  @ApiOperation("更新系统配置页面")
  public String update(
      @PathVariable("id") String idStr,
      @ApiIgnore HttpSession session,
      @ApiIgnore Map<String, Object> map) {
    return commonService.update(
        service,
        ConfigPoJo.class,
        idStr,
        "manage/config/edit",
        map,
        "config",
        () -> commonService.writeMenuAndSiteInfo(session, map));
  }

  @PutMapping("/{id}")
  @ResponseBody
  @PreAuthorize("hasAnyAuthority('global:write','config:write')")
  @ApiOperation("更新系统配置")
  public ResponseEntity update(
      @PathVariable("id") String idStr,
      @Valid ConfigRequest configRequest,
      @ApiIgnore BindingResult bindingResult) {
    log.info(" ConfigController.update : [{}]", configRequest);
    return commonService.update(service, Config.class, configRequest, idStr, bindingResult);
  }

  @DeleteMapping("/{id}")
  @ResponseBody
  @PreAuthorize("hasAnyAuthority('global:delete','config:delete')")
  @ApiOperation("删除系统配置")
  public ResponseEntity delete(@PathVariable("id") String idStr) {
    log.info(" ConfigController.delete : [{}]", idStr);
    return commonService.delete(service, idStr);
  }

  @DeleteMapping("/deleteBatch")
  @ResponseBody
  @PreAuthorize("hasAnyAuthority('global:delete','config:delete')")
  @ApiOperation("删除系统配置批量")
  public ResponseEntity deleteBatch(@RequestParam("ids") List<Long> ids) {
    log.info(" ConfigController.deleteBatch : [{}]", ids);
    return commonService.deleteBatch(service, ids);
  }
}
