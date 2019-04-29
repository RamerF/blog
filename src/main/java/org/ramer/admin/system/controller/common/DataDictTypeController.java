package org.ramer.admin.system.controller.common;

import io.swagger.annotations.*;
import java.util.Map;
import javax.annotation.Resource;
import javax.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.ramer.admin.system.entity.domain.common.DataDictType;
import org.ramer.admin.system.entity.pojo.common.DataDictTypePoJo;
import org.ramer.admin.system.entity.request.common.DataDictTypeRequest;
import org.ramer.admin.system.entity.response.CommonResponse;
import org.ramer.admin.system.entity.response.common.DataDictTypeResponse;
import org.ramer.admin.system.service.common.CommonService;
import org.ramer.admin.system.service.common.DataDictTypeService;
import org.ramer.admin.system.util.TextUtil;
import org.ramer.admin.system.validator.common.DataDictTypeValidator;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.*;

@Slf4j
@Controller("dataDictTypemc")
@PreAuthorize("hasAnyAuthority('global:read','dataDictType:read')")
@RequestMapping("/common/dataDictType")
@Api(tags = "管理端: 数据字典类型接口")
@SuppressWarnings("UnusedDeclaration")
public class DataDictTypeController {
  @Resource private DataDictTypeService service;
  @Resource private CommonService commonService;
  @Resource private DataDictTypeValidator validator;

  @InitBinder
  void initBinder(WebDataBinder binder) {
    binder.addValidators(validator);
  }

  @GetMapping("/index")
  @ApiOperation("数据字典类型页面")
  public String index() {
    return "dataDictType/index";
  }

  @GetMapping("/list")
  @ResponseBody
  @ApiOperation("获取数据字典类型列表")
  public ResponseEntity list(
      @RequestParam(value = "page", required = false, defaultValue = "1") String pageStr,
      @RequestParam(value = "size", required = false, defaultValue = "10") String sizeStr,
      @ApiParam("查询条件") @RequestParam(value = "criteria", required = false) String criteria) {
    final int[] pageAndSize = TextUtil.validFixPageAndSize(pageStr, sizeStr);
    return commonService.page(
        service.page(criteria, pageAndSize[0], pageAndSize[1]), DataDictTypeResponse::of);
  }

  @GetMapping
  @ApiOperation("添加数据字典类型页面")
  public String create() {
    return "dataDictType/create";
  }

  @PostMapping
  @ResponseBody
  @PreAuthorize("hasAnyAuthority('global:create','dataDictType:create')")
  @ApiOperation("添加数据字典类型")
  public ResponseEntity create(
      @Valid DataDictTypeRequest dataDictTypeRequest, BindingResult bindingResult)
      throws Exception {
    log.info(" DataDictTypeController.create : [{}]", dataDictTypeRequest);
    return commonService.create(service, DataDictType.class, dataDictTypeRequest, bindingResult);
  }

  @GetMapping("/{id}")
  @ApiOperation("更新数据字典类型页面")
  public String update(@PathVariable("id") String idStr, Map<String, Object> map) throws Exception {
    return commonService.update(
        service, DataDictTypePoJo.class, idStr, "dataDictType/update", map, "dataDictType");
  }

  @PutMapping("/{id}")
  @ResponseBody
  @PreAuthorize("hasAnyAuthority('global:write','dataDictType:write')")
  @ApiOperation("更新数据字典类型")
  public ResponseEntity update(
      @PathVariable("id") String idStr,
      @Valid DataDictTypeRequest dataDictTypeRequest,
      BindingResult bindingResult)
      throws Exception {
    log.info(" DataDictTypeController.update : [{}]", dataDictTypeRequest);
    final long id = TextUtil.validLong(idStr, -1);
    if (id < 1) {
      return CommonResponse.wrongFormat("id");
    }
    dataDictTypeRequest.setId(id);
    return commonService.update(
        service, DataDictType.class, dataDictTypeRequest, idStr, bindingResult);
  }

  @DeleteMapping("/{id}")
  @ResponseBody
  @PreAuthorize("hasAnyAuthority('global:delete','dataDictType:delete')")
  @ApiOperation("删除数据字典类型")
  public ResponseEntity delete(@PathVariable("id") String idStr) throws Exception {
    log.info(" DataDictTypeController.delete : [{}]", idStr);
    return commonService.delete(service, idStr);
  }
}
