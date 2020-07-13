package org.ramer.admin.system.controller.common.manage;

import io.swagger.annotations.*;
import java.util.List;
import java.util.Map;
import javax.annotation.Resource;
import javax.servlet.http.HttpSession;
import javax.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.ramer.admin.system.entity.Constant.AccessPath;
import org.ramer.admin.system.entity.domain.common.DataDictType;
import org.ramer.admin.system.entity.pojo.common.DataDictTypePoJo;
import org.ramer.admin.system.entity.request.common.DataDictTypeRequest;
import org.ramer.admin.system.entity.response.Rs;
import org.ramer.admin.system.entity.response.common.DataDictTypeResponse;
import org.ramer.admin.system.service.common.CommonService;
import org.ramer.admin.system.service.common.DataDictTypeService;
import org.ramer.admin.system.util.TextUtil;
import org.ramer.admin.system.validator.common.DataDictTypeValidator;
import org.springframework.data.domain.PageImpl;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.*;
import springfox.documentation.annotations.ApiIgnore;

@Slf4j
@Controller("dataDictTypec")
@PreAuthorize("hasAnyAuthority('global:read','manage:read','dataDictType:read')")
@RequestMapping(AccessPath.MANAGE + "/dataDictType")
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
  public String index(@ApiIgnore HttpSession session, @ApiIgnore Map<String, Object> map) {
    commonService.writeMenuAndSiteInfo(session, map);
    return "manage/data_dict_type/index";
  }

  @GetMapping("/page")
  @ResponseBody
  @ApiOperation("获取数据字典类型列表")
  public ResponseEntity<Rs<PageImpl<DataDictTypeResponse>>> page(
      @ApiParam("页号,从1开始,当page=size=-1时,表示不分页")
          @RequestParam(value = "page", required = false, defaultValue = "1")
          String pageStr,
      @RequestParam(value = "size", required = false, defaultValue = "10") String sizeStr,
      @ApiParam("查询条件") @RequestParam(value = "criteria", required = false) String criteria) {
    final int[] pageAndSize = TextUtil.validFixPageAndSize(pageStr, sizeStr);
    return commonService.page(
        service.page(criteria, pageAndSize[0], pageAndSize[1]), DataDictTypeResponse::of);
  }

  @GetMapping
  @ApiOperation("添加数据字典类型页面")
  public String create(@ApiIgnore HttpSession session, @ApiIgnore Map<String, Object> map) {
    commonService.writeMenuAndSiteInfo(session, map);
    return "manage/data_dict_type/edit";
  }

  @PostMapping
  @ResponseBody
  @PreAuthorize("hasAnyAuthority('global:create','dataDictType:create')")
  @ApiOperation("添加数据字典类型")
  public ResponseEntity<Rs<Object>> create(
      @Valid DataDictTypeRequest dataDictTypeRequest, @ApiIgnore BindingResult bindingResult) {
    log.info(" DataDictTypeController.create : [{}]", dataDictTypeRequest);
    return commonService.create(service, DataDictType.class, dataDictTypeRequest, bindingResult);
  }

  @GetMapping("/{id}")
  @ApiOperation("更新数据字典类型页面")
  public String update(
      @PathVariable("id") String idStr,
      @ApiIgnore HttpSession session,
      @ApiIgnore Map<String, Object> map) {
    return commonService.update(
        service,
        DataDictTypePoJo.class,
        idStr,
        "manage/data_dict_type/edit",
        map,
        "dataDictType",
        id -> commonService.writeMenuAndSiteInfo(session, map));
  }

  @PutMapping("/{id}")
  @ResponseBody
  @PreAuthorize("hasAnyAuthority('global:write','dataDictType:write')")
  @ApiOperation("更新数据字典类型")
  public ResponseEntity<Rs<Object>> update(
      @PathVariable("id") String idStr,
      @Valid DataDictTypeRequest dataDictTypeRequest,
      @ApiIgnore BindingResult bindingResult) {
    log.info(" DataDictTypeController.update : [{}]", dataDictTypeRequest);
    final long id = TextUtil.validLong(idStr, -1);
    if (id < 1) {
      return Rs.wrongFormat("id");
    }
    dataDictTypeRequest.setId(id);
    return commonService.update(
        service, DataDictType.class, dataDictTypeRequest, idStr, bindingResult);
  }

  @DeleteMapping("/{id}")
  @ResponseBody
  @PreAuthorize("hasAnyAuthority('global:delete','dataDictType:delete')")
  @ApiOperation("删除数据字典类型")
  public ResponseEntity<Rs<Object>> delete(@PathVariable("id") String idStr) {
    log.info(" DataDictTypeController.delete : [{}]", idStr);
    return commonService.delete(service, idStr);
  }

  @DeleteMapping("/deleteBatch")
  @ResponseBody
  @PreAuthorize("hasAnyAuthority('global:delete','dataDictType:delete')")
  @ApiOperation("删除数据字典类型批量")
  public ResponseEntity<Rs<Object>> deleteBatch(@RequestParam("ids") List<Long> ids) {
    log.info(" DataDictTypeController.deleteBatch : [{}]", ids);
    return commonService.deleteBatch(service, ids);
  }
}
