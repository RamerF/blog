package org.ramer.admin.system.controller.common.manage;

import io.swagger.annotations.*;
import java.util.*;
import java.util.stream.Collectors;
import javax.annotation.Resource;
import javax.servlet.http.HttpSession;
import javax.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.ramer.admin.system.entity.Constant.AccessPath;
import org.ramer.admin.system.entity.domain.common.Menu;
import org.ramer.admin.system.entity.pojo.common.MenuPoJo;
import org.ramer.admin.system.entity.request.common.MenuRequest;
import org.ramer.admin.system.entity.response.CommonResponse;
import org.ramer.admin.system.entity.response.common.MenuResponse;
import org.ramer.admin.system.service.common.CommonService;
import org.ramer.admin.system.service.common.MenuService;
import org.ramer.admin.system.util.TextUtil;
import org.ramer.admin.system.validator.common.MenuValidator;
import org.springframework.data.domain.PageImpl;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.*;
import springfox.documentation.annotations.ApiIgnore;

@Slf4j
@Controller("menucm")
@PreAuthorize("hasAnyAuthority('global:read','menu:read')")
@RequestMapping(AccessPath.MANAGE + "/menu")
@Api(tags = "管理端: 菜单接口")
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
  public String index(@ApiIgnore HttpSession session, @ApiIgnore Map<String, Object> map) {
    commonService.writeMenuAndSiteInfo(session, map);
    return "manage/menu/index";
  }

  @GetMapping("/page")
  @ResponseBody
  @ApiOperation("获取菜单列表")
  public ResponseEntity<CommonResponse<PageImpl<MenuResponse>>> page(
      @ApiParam("页号,从1开始,当page=size=-1时,表示不分页")
          @RequestParam(value = "page", required = false, defaultValue = "1")
          String pageStr,
      @RequestParam(value = "size", required = false, defaultValue = "10") String sizeStr,
      @ApiParam("查询条件") @RequestParam(value = "criteria", required = false) String criteria) {
    final int[] pageAndSize = TextUtil.validFixPageAndSize(pageStr, sizeStr);
    return commonService.page(
        service.page(criteria, pageAndSize[0], pageAndSize[1]), MenuResponse::of);
  }

  @GetMapping
  @ApiOperation("添加菜单页面")
  public String create(@ApiIgnore HttpSession session, @ApiIgnore Map<String, Object> map) {
    commonService.writeMenuAndSiteInfo(session, map);
    map.put("ms", service.list(null));
    return "manage/menu/edit";
  }

  @PostMapping
  @ResponseBody
  @PreAuthorize("hasAnyAuthority('global:create','menu:create')")
  @ApiOperation("添加菜单")
  public ResponseEntity<CommonResponse<Object>> create(
      @Valid MenuRequest menuRequest, @ApiIgnore BindingResult bindingResult) {
    log.info(" MenuController.create : [{}]", menuRequest);
    return commonService.create(service, Menu.class, menuRequest, bindingResult);
  }

  @GetMapping("/{id}")
  @ApiOperation("更新菜单页面")
  public String update(
      @PathVariable("id") String idStr,
      @ApiIgnore HttpSession session,
      @ApiIgnore Map<String, Object> map) {
    commonService.writeMenuAndSiteInfo(session, map);
    return commonService.update(
        service,
        MenuPoJo.class,
        idStr,
        "manage/menu/edit",
        map,
        "menu",
        id -> {
          map.put(
              "ms",
              service.list(null).stream()
                  .filter(m -> !Objects.equals(m.getId(), id))
                  .collect(Collectors.toList()));
          map.put("menu", service.getById(id));
        },
        false);
  }

  @PutMapping("/{id}")
  @ResponseBody
  @PreAuthorize("hasAnyAuthority('global:write','menu:write')")
  @ApiOperation("更新菜单")
  public ResponseEntity<CommonResponse<Object>> update(
      @PathVariable("id") String idStr,
      @Valid MenuRequest menuRequest,
      @ApiIgnore BindingResult bindingResult) {
    log.info(" MenuController.update : [{}]", menuRequest);
    return commonService.update(service, Menu.class, menuRequest, idStr, bindingResult, "parentId");
  }

  @DeleteMapping("/{id}")
  @ResponseBody
  @PreAuthorize("hasAnyAuthority('global:delete','menu:delete')")
  @ApiOperation("删除菜单")
  public ResponseEntity<CommonResponse<Object>> delete(@PathVariable("id") String idStr) {
    log.info(" MenuController.delete : [{}]", idStr);
    return commonService.delete(service, idStr);
  }

  @DeleteMapping("/deleteBatch")
  @ResponseBody
  @PreAuthorize("hasAnyAuthority('global:delete','menu:delete')")
  @ApiOperation("删除菜单批量")
  public ResponseEntity<CommonResponse<Object>> deleteBatch(@RequestParam("ids") List<Long> ids) {
    log.info(" MenuController.deleteBatch : [{}]", ids);
    return commonService.deleteBatch(service, ids);
  }
}
