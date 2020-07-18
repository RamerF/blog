package io.github.ramerf.blog.system.controller.common;

import io.github.ramerf.wind.core.entity.response.Rs;
import io.swagger.annotations.*;
import java.util.Map;
import javax.annotation.Resource;
import javax.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import io.github.ramerf.blog.system.entity.domain.common.DataDict;
import io.github.ramerf.blog.system.entity.pojo.common.DataDictPoJo;
import io.github.ramerf.blog.system.entity.request.common.DataDictRequest;
import io.github.ramerf.blog.system.entity.response.common.DataDictResponse;
import io.github.ramerf.blog.system.service.common.CommonService;
import io.github.ramerf.blog.system.service.common.DataDictService;
import io.github.ramerf.blog.system.util.TextUtil;
import io.github.ramerf.blog.system.validator.common.DataDictValidator;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.*;

@Slf4j
@Controller("dataDictmc")
@PreAuthorize("hasAnyAuthority('global:read','dataDict:read')")
@RequestMapping("/common/dataDict")
@Api(tags = "管理端: 数据字典接口")
@SuppressWarnings("UnusedDeclaration")
public class DataDictController {
  @Resource private DataDictService service;
  @Resource private CommonService commonService;
  @Resource private DataDictValidator validator;

  @InitBinder
  void initBinder(WebDataBinder binder) {
    binder.addValidators(validator);
  }

  @GetMapping("/index")
  @ApiOperation("数据字典页面")
  public String index() {
    return "dataDict/index";
  }

  @GetMapping("/list")
  @ResponseBody
  @ApiOperation("获取数据字典列表")
  public ResponseEntity list(
      @RequestParam(value = "page", required = false, defaultValue = "1") String pageStr,
      @RequestParam(value = "size", required = false, defaultValue = "10") String sizeStr,
      @ApiParam("查询条件") @RequestParam(value = "criteria", required = false) String criteria) {
    final int[] pageAndSize = TextUtil.validFixPageAndSize(pageStr, sizeStr);
    return commonService.page(
        service.page(criteria, pageAndSize[0], pageAndSize[1]), DataDictResponse::of);
  }

  @GetMapping
  @ApiOperation("添加数据字典页面")
  public String create() {
    return "dataDict/create";
  }

  @PostMapping
  @ResponseBody
  @PreAuthorize("hasAnyAuthority('global:create','dataDict:create')")
  @ApiOperation("添加数据字典")
  public ResponseEntity create(@Valid DataDictRequest dataDictRequest, BindingResult bindingResult)
      throws Exception {
    log.info(" DataDictController.create : [{}]", dataDictRequest);
    return commonService.create(service, DataDict.class, dataDictRequest, bindingResult);
  }

  @GetMapping("/{id}")
  @ApiOperation("更新数据字典页面")
  public String update(@PathVariable("id") String idStr, Map<String, Object> map) throws Exception {
    return commonService.update(
        service, DataDictPoJo.class, idStr, "dataDict/update", map, "dataDict", null);
  }

  @PutMapping("/{id}")
  @ResponseBody
  @PreAuthorize("hasAnyAuthority('global:write','dataDict:write')")
  @ApiOperation("更新数据字典")
  public ResponseEntity update(
      @PathVariable("id") String idStr,
      @Valid DataDictRequest dataDictRequest,
      BindingResult bindingResult)
      throws Exception {
    log.info(" DataDictController.update : [{}]", dataDictRequest);
    final long id = TextUtil.validLong(idStr, -1);
    if (id < 1) {
      return Rs.wrongFormat("id");
    }
    dataDictRequest.setId(id);
    return commonService.update(service, DataDict.class, dataDictRequest, idStr, bindingResult);
  }

  @DeleteMapping("/{id}")
  @ResponseBody
  @PreAuthorize("hasAnyAuthority('global:delete','dataDict:delete')")
  @ApiOperation("删除数据字典")
  public ResponseEntity delete(@PathVariable("id") String idStr) throws Exception {
    log.info(" DataDictController.delete : [{}]", idStr);
    return commonService.delete(service, idStr);
  }
}
