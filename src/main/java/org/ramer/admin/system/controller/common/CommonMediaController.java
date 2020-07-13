package org.ramer.admin.system.controller.common;

import io.swagger.annotations.*;
import java.util.Map;
import javax.annotation.Resource;
import javax.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.ramer.admin.system.entity.domain.common.CommonMedia;
import org.ramer.admin.system.entity.pojo.common.CommonMediaPoJo;
import org.ramer.admin.system.entity.request.common.CommonMediaRequest;
import org.ramer.admin.system.entity.response.Rs;
import org.ramer.admin.system.entity.response.common.CommonMediaResponse;
import org.ramer.admin.system.service.common.CommonMediaService;
import org.ramer.admin.system.service.common.CommonService;
import org.ramer.admin.system.util.TextUtil;
import org.ramer.admin.system.validator.common.CommonMediaValidator;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.*;

@Slf4j
@Controller("commonMediamc")
@PreAuthorize("hasAnyAuthority('global:read','commonMedia:read')")
@RequestMapping("/common/commonMedia")
@Api(tags = "管理端: 通用多媒体接口")
@SuppressWarnings("UnusedDeclaration")
public class CommonMediaController {
  @Resource private CommonMediaService service;
  @Resource private CommonService commonService;
  @Resource private CommonMediaValidator validator;

  @InitBinder
  void initBinder(WebDataBinder binder) {
    binder.addValidators(validator);
  }

  @GetMapping("/index")
  @ApiOperation("通用多媒体页面")
  public String index() {
    return "commonMedia/index";
  }

  @GetMapping("/list")
  @ResponseBody
  @ApiOperation("获取通用多媒体列表")
  public ResponseEntity list(
      @RequestParam(value = "page", required = false, defaultValue = "1") String pageStr,
      @RequestParam(value = "size", required = false, defaultValue = "10") String sizeStr,
      @ApiParam("查询条件") @RequestParam(value = "criteria", required = false) String criteria) {
    final int[] pageAndSize = TextUtil.validFixPageAndSize(pageStr, sizeStr);
    return commonService.page(
        service.page(criteria, pageAndSize[0], pageAndSize[1]), CommonMediaResponse::of);
  }

  @GetMapping
  @ApiOperation("添加通用多媒体页面")
  public String create() {
    return "commonMedia/create";
  }

  @PostMapping
  @ResponseBody
  @PreAuthorize("hasAnyAuthority('global:create','commonMedia:create')")
  @ApiOperation("添加通用多媒体")
  public ResponseEntity create(
      @Valid CommonMediaRequest commonMediaRequest, BindingResult bindingResult) throws Exception {
    log.info(" CommonMediaController.create : [{}]", commonMediaRequest);
    return commonService.create(service, CommonMedia.class, commonMediaRequest, bindingResult);
  }

  @GetMapping("/{id}")
  @ApiOperation("更新通用多媒体页面")
  public String update(@PathVariable("id") String idStr, Map<String, Object> map) throws Exception {
    return commonService.update(
        service,
        CommonMediaPoJo.class,
        idStr,
        "commonMedia/update",
        map,
        "commonMedia",
        null,
        true);
  }

  @PutMapping("/{id}")
  @ResponseBody
  @PreAuthorize("hasAnyAuthority('global:write','commonMedia:write')")
  @ApiOperation("更新通用多媒体")
  public ResponseEntity update(
      @PathVariable("id") String idStr,
      @Valid CommonMediaRequest commonMediaRequest,
      BindingResult bindingResult)
      throws Exception {
    log.info(" CommonMediaController.update : [{}]", commonMediaRequest);
    final long id = TextUtil.validLong(idStr, -1);
    if (id < 1) {
      return Rs.wrongFormat("id");
    }
    commonMediaRequest.setId(id);
    return commonService.update(
        service, CommonMedia.class, commonMediaRequest, idStr, bindingResult);
  }

  @DeleteMapping("/{id}")
  @ResponseBody
  @PreAuthorize("hasAnyAuthority('global:delete','commonMedia:delete')")
  @ApiOperation("删除通用多媒体")
  public ResponseEntity delete(@PathVariable("id") String idStr) throws Exception {
    log.info(" CommonMediaController.delete : [{}]", idStr);
    return commonService.delete(service, idStr);
  }
}
