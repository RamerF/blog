package org.ramer.admin.system.controller.common;

import io.swagger.annotations.*;
import java.util.Map;
import javax.annotation.Resource;
import javax.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.ramer.admin.system.entity.domain.common.Menu;
import org.ramer.admin.system.entity.pojo.common.MenuPoJo;
import org.ramer.admin.system.entity.request.common.MenuRequest;
import org.ramer.admin.system.entity.response.CommonResponse;
import org.ramer.admin.system.entity.response.common.MenuResponse;
import org.ramer.admin.system.service.common.CommonService;
import org.ramer.admin.system.service.common.MenuService;
import org.ramer.admin.system.util.TextUtil;
import org.ramer.admin.system.validator.common.MenuValidator;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.*;

@Slf4j
@Controller("menuc")
@PreAuthorize("hasAnyAuthority('global:read','menu:read')")
@RequestMapping("/common/menu")
@Api(tags = "菜单接口")
@SuppressWarnings("UnusedDeclaration")
public class MenuController {
  @Resource private MenuService service;
  @Resource private CommonService commonService;
  @Resource private MenuValidator validator;

  @InitBinder
  void initBinder(WebDataBinder binder) {
    binder.addValidators(validator);
  }

  @GetMapping("/index")
  @ApiOperation("菜单页面")
  public String index() {
    return "menu/index";
  }

  @GetMapping("/list")
  @ResponseBody
  @ApiOperation("获取菜单列表")
  public ResponseEntity list(
      @RequestParam(value = "page", required = false, defaultValue = "1") String pageStr,
      @RequestParam(value = "size", required = false, defaultValue = "10") String sizeStr,
      @ApiParam("查询条件") @RequestParam(value = "criteria", required = false) String criteria) {
    final int[] pageAndSize = TextUtil.validFixPageAndSize(pageStr, sizeStr);
    return commonService.page(
        service.page(criteria, pageAndSize[0], pageAndSize[1]), MenuResponse::of);
  }

  @GetMapping
  @ApiOperation("添加菜单页面")
  public String create() {
    return "menu/create";
  }

  @PostMapping
  @ResponseBody
  @PreAuthorize("hasAnyAuthority('global:create','menu:create')")
  @ApiOperation("添加菜单")
  public ResponseEntity create(@Valid MenuRequest menuRequest, BindingResult bindingResult)
      throws Exception {
    log.info(" MenuController.create : [{}]", menuRequest);
    return commonService.create(service, Menu.class, menuRequest, bindingResult);
  }

  @GetMapping("/{id}")
  @ApiOperation("更新菜单页面")
  public String update(@PathVariable("id") String idStr, Map<String, Object> map) throws Exception {
    return commonService.update(service, MenuPoJo.class, idStr, "menu/update", map, "menu");
  }

  @PutMapping("/{id}")
  @ResponseBody
  @PreAuthorize("hasAnyAuthority('global:write','menu:write')")
  @ApiOperation("更新菜单")
  public ResponseEntity update(
      @PathVariable("id") String idStr, @Valid MenuRequest menuRequest, BindingResult bindingResult)
      throws Exception {
    log.info(" MenuController.update : [{}]", menuRequest);
    final long id = TextUtil.validLong(idStr, -1);
    if (id < 1) {
      return CommonResponse.wrongFormat("id");
    }
    menuRequest.setId(id);
    return commonService.update(service, Menu.class, menuRequest, idStr, bindingResult);
  }

  @DeleteMapping("/{id}")
  @ResponseBody
  @PreAuthorize("hasAnyAuthority('global:delete','menu:delete')")
  @ApiOperation("删除菜单")
  public ResponseEntity delete(@PathVariable("id") String idStr) throws Exception {
    log.info(" MenuController.delete : [{}]", idStr);
    return commonService.delete(service, idStr);
  }
}
