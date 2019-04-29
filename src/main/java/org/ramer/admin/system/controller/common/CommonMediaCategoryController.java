 package org.ramer.admin.system.controller.common;

import io.swagger.annotations.*;
import java.util.Map;
import javax.annotation.Resource;
import javax.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.ramer.admin.system.entity.domain.common.CommonMediaCategory;
import org.ramer.admin.system.entity.pojo.common.CommonMediaCategoryPoJo;
import org.ramer.admin.system.entity.request.common.CommonMediaCategoryRequest;
import org.ramer.admin.system.entity.response.CommonResponse;
import org.ramer.admin.system.entity.response.common.CommonMediaCategoryResponse;
import org.ramer.admin.system.service.common.CommonMediaCategoryService;
import org.ramer.admin.system.service.common.CommonService;
import org.ramer.admin.system.util.TextUtil;
import org.ramer.admin.system.validator.common.CommonMediaCategoryValidator;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.*;

@Slf4j
@Controller("mediaCategorymc")
@PreAuthorize("hasAnyAuthority('global:read','mediaCategory:read')")
@RequestMapping( "/common/mediaCategory")
@Api(tags = "管理端: 通用多媒体类别接口")
@SuppressWarnings("UnusedDeclaration")
public class CommonMediaCategoryController {
  @Resource private CommonMediaCategoryService service;
  @Resource private CommonService commonService;
  @Resource private CommonMediaCategoryValidator validator;

  @InitBinder
  void initBinder(WebDataBinder binder) {
    binder.addValidators(validator);
  }

  @GetMapping("/index")
  @ApiOperation("通用多媒体类别页面")
  public String index() {
    return "mediaCategory/index";
  }

  @GetMapping("/list")
  @ResponseBody
  @ApiOperation("获取通用多媒体类别列表")
  public ResponseEntity list(
      @RequestParam(value = "page", required = false, defaultValue = "1") String pageStr,
      @RequestParam(value = "size", required = false, defaultValue = "10") String sizeStr,
      @ApiParam("查询条件") @RequestParam(value = "criteria", required = false) String criteria) {
    final int[] pageAndSize = TextUtil.validFixPageAndSize(pageStr, sizeStr);
    return commonService.page(
        service.page(criteria, pageAndSize[0], pageAndSize[1]), CommonMediaCategoryResponse::of);
  }

  @GetMapping
  @ApiOperation("添加通用多媒体类别页面")
  public String create() {
    return "mediaCategory/create";
  }

  @PostMapping
  @ResponseBody
  @PreAuthorize("hasAnyAuthority('global:create','mediaCategory:create')")
  @ApiOperation("添加通用多媒体类别")
  public ResponseEntity create(@Valid CommonMediaCategoryRequest mediaCategoryRequest, BindingResult bindingResult) throws Exception {
    log.info(" CommonMediaCategoryController.create : [{}]", mediaCategoryRequest);
    return commonService.create(
        service, CommonMediaCategory.class, mediaCategoryRequest, bindingResult);
  }

  @GetMapping("/{id}")
  @ApiOperation("更新通用多媒体类别页面")
  public String update(@PathVariable("id") String idStr, Map<String, Object> map) throws Exception {
    return commonService.update(
        service,
        CommonMediaCategoryPoJo.class,
        idStr,
        "mediaCategory/update",
        map, "mediaCategory");
  }

  @PutMapping("/{id}")
  @ResponseBody
  @PreAuthorize("hasAnyAuthority('global:write','mediaCategory:write')")
  @ApiOperation("更新通用多媒体类别")
  public ResponseEntity update(
      @PathVariable("id") String idStr, @Valid CommonMediaCategoryRequest mediaCategoryRequest, BindingResult bindingResult)
      throws Exception {
    log.info(" CommonMediaCategoryController.update : [{}]", mediaCategoryRequest);
    final long id = TextUtil.validLong(idStr, -1);
    if (id < 1) {
      return CommonResponse.wrongFormat("id");
    }
    mediaCategoryRequest.setId(id);
    return commonService.update(
        service, CommonMediaCategory.class, mediaCategoryRequest, idStr, bindingResult);
  }

  @DeleteMapping("/{id}")
  @ResponseBody
  @PreAuthorize("hasAnyAuthority('global:delete','mediaCategory:delete')")
  @ApiOperation("删除通用多媒体类别")
  public ResponseEntity delete(@PathVariable("id") String idStr) throws Exception {
    log.info(" CommonMediaCategoryController.delete : [{}]", idStr);
    return commonService.delete(service, idStr);
  }
}
