 package org.ramer.admin.system.controller.common;

import io.swagger.annotations.*;
import java.util.Map;
import javax.annotation.Resource;
import javax.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.ramer.admin.system.entity.domain.common.Config;
import org.ramer.admin.system.entity.pojo.common.ConfigPoJo;
import org.ramer.admin.system.entity.request.common.ConfigRequest;
import org.ramer.admin.system.entity.response.CommonResponse;
import org.ramer.admin.system.entity.response.common.ConfigResponse;
import org.ramer.admin.system.service.common.CommonService;
import org.ramer.admin.system.service.common.ConfigService;
import org.ramer.admin.system.util.TextUtil;
import org.ramer.admin.system.validator.common.ConfigValidator;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.*;

@Slf4j
@Controller("configc")
@PreAuthorize("hasAnyAuthority('global:read','config:read')")
@RequestMapping( "/common/config")
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
  public String index() {
    return "config/index";
  }

  @GetMapping("/list")
  @ResponseBody
  @ApiOperation("获取系统配置列表")
  public ResponseEntity list(
      @RequestParam(value = "page", required = false, defaultValue = "1") String pageStr,
      @RequestParam(value = "size", required = false, defaultValue = "10") String sizeStr,
      @ApiParam("查询条件") @RequestParam(value = "criteria", required = false) String criteria) {
    final int[] pageAndSize = TextUtil.validFixPageAndSize(pageStr, sizeStr);
    return commonService.page(
        service.page(criteria, pageAndSize[0], pageAndSize[1]), ConfigResponse::of);
  }

  @GetMapping
  @ApiOperation("添加系统配置页面")
  public String create() {
    return "config/create";
  }

  @PostMapping
  @ResponseBody
  @PreAuthorize("hasAnyAuthority('global:create','config:create')")
  @ApiOperation("添加系统配置")
  public ResponseEntity create(@Valid ConfigRequest configRequest, BindingResult bindingResult) throws Exception {
    log.info(" ConfigController.create : [{}]", configRequest);
    return commonService.create(
        service, Config.class, configRequest, bindingResult);
  }

  @GetMapping("/{id}")
  @ApiOperation("更新系统配置页面")
  public String update(@PathVariable("id") String idStr, Map<String, Object> map) throws Exception {
    return commonService.update(
        service,
        ConfigPoJo.class,
        idStr,
        "config/update",
        map, "config");
  }

  @PutMapping("/{id}")
  @ResponseBody
  @PreAuthorize("hasAnyAuthority('global:write','config:write')")
  @ApiOperation("更新系统配置")
  public ResponseEntity update(
      @PathVariable("id") String idStr, @Valid ConfigRequest configRequest, BindingResult bindingResult)
      throws Exception {
    log.info(" ConfigController.update : [{}]", configRequest);
    final long id = TextUtil.validLong(idStr, -1);
    if (id < 1) {
      return CommonResponse.wrongFormat("id");
    }
    configRequest.setId(id);
    return commonService.update(
        service, Config.class, configRequest, idStr, bindingResult);
  }

  @DeleteMapping("/{id}")
  @ResponseBody
  @PreAuthorize("hasAnyAuthority('global:delete','config:delete')")
  @ApiOperation("删除系统配置")
  public ResponseEntity delete(@PathVariable("id") String idStr) throws Exception {
    log.info(" ConfigController.delete : [{}]", idStr);
    return commonService.delete(service, idStr);
  }
}
