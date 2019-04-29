 package org.ramer.admin.system.controller.common;

 import io.swagger.annotations.*;
 import java.util.Map;
 import javax.annotation.Resource;
 import javax.validation.Valid;
 import lombok.extern.slf4j.Slf4j;
 import org.ramer.admin.system.entity.domain.common.ManageLog;
 import org.ramer.admin.system.entity.pojo.common.ManageLogPoJo;
 import org.ramer.admin.system.entity.request.common.ManageLogRequest;
 import org.ramer.admin.system.entity.response.CommonResponse;
 import org.ramer.admin.system.entity.response.common.ManageLogResponse;
 import org.ramer.admin.system.service.common.CommonService;
 import org.ramer.admin.system.service.common.ManageLogService;
 import org.ramer.admin.system.util.TextUtil;
 import org.ramer.admin.system.validator.common.ManageLogValidator;
 import org.springframework.http.ResponseEntity;
 import org.springframework.security.access.prepost.PreAuthorize;
 import org.springframework.stereotype.Controller;
 import org.springframework.validation.BindingResult;
 import org.springframework.web.bind.WebDataBinder;
 import org.springframework.web.bind.annotation.*;

@Slf4j
@Controller("manageLogmc")
@PreAuthorize("hasAnyAuthority('global:read','manageLog:read')")
@RequestMapping( "/common/manageLog")
@Api(tags = "管理端: 日志接口")
@SuppressWarnings("UnusedDeclaration")
public class ManageLogController {
  @Resource private ManageLogService service;
  @Resource private CommonService commonService;
  @Resource private ManageLogValidator validator;

  @InitBinder
  void initBinder(WebDataBinder binder) {
    binder.addValidators(validator);
  }

  @GetMapping("/index")
  @ApiOperation("管理端日志页面")
  public String index() {
    return "manageLog/index";
  }

  @GetMapping("/list")
  @ResponseBody
  @ApiOperation("获取管理端日志列表")
  public ResponseEntity list(
      @RequestParam(value = "page", required = false, defaultValue = "1") String pageStr,
      @RequestParam(value = "size", required = false, defaultValue = "10") String sizeStr,
      @ApiParam("查询条件") @RequestParam(value = "criteria", required = false) String criteria) {
    final int[] pageAndSize = TextUtil.validFixPageAndSize(pageStr, sizeStr);
    return commonService.page(
        service.page(criteria, pageAndSize[0], pageAndSize[1]), ManageLogResponse::of);
  }

  @GetMapping
  @ApiOperation("添加管理端日志页面")
  public String create() {
    return "manageLog/create";
  }

  @PostMapping
  @ResponseBody
  @PreAuthorize("hasAnyAuthority('global:create','manageLog:create')")
  @ApiOperation("添加管理端日志")
  public ResponseEntity create(@Valid ManageLogRequest manageLogRequest, BindingResult bindingResult) throws Exception {
    log.info(" ManageLogController.create : [{}]", manageLogRequest);
    return commonService.create(
        service, ManageLog.class, manageLogRequest, bindingResult);
  }

  @GetMapping("/{id}")
  @ApiOperation("更新管理端日志页面")
  public String update(@PathVariable("id") String idStr, Map<String, Object> map) throws Exception {
    return commonService.update(
        service,
        ManageLogPoJo.class,
        idStr,
        "manageLog/update",
        map, "manageLog");
  }

  @PutMapping("/{id}")
  @ResponseBody
  @PreAuthorize("hasAnyAuthority('global:write','manageLog:write')")
  @ApiOperation("更新管理端日志")
  public ResponseEntity update(
      @PathVariable("id") String idStr, @Valid ManageLogRequest manageLogRequest, BindingResult bindingResult)
      throws Exception {
    log.info(" ManageLogController.update : [{}]", manageLogRequest);
    final long id = TextUtil.validLong(idStr, -1);
    if (id < 1) {
      return CommonResponse.wrongFormat("id");
    }
    manageLogRequest.setId(id);
    return commonService.update(
        service, ManageLog.class, manageLogRequest, idStr, bindingResult);
  }

  @DeleteMapping("/{id}")
  @ResponseBody
  @PreAuthorize("hasAnyAuthority('global:delete','manageLog:delete')")
  @ApiOperation("删除管理端日志")
  public ResponseEntity delete(@PathVariable("id") String idStr) throws Exception {
    log.info(" ManageLogController.delete : [{}]", idStr);
    return commonService.delete(service, idStr);
  }
}
