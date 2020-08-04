package io.github.ramerf.blog.controller.manage;

import io.github.ramerf.blog.entity.pojo.TagPoJo;
import io.github.ramerf.blog.entity.request.TagRequest;
import io.github.ramerf.blog.entity.response.TagResponse;
import io.github.ramerf.blog.service.TagService;
import io.github.ramerf.blog.system.entity.Constant.AccessPath;
import io.github.ramerf.blog.system.exception.CommonException;
import io.github.ramerf.blog.system.service.common.CommonService;
import io.github.ramerf.blog.validator.TagValidator;
import io.github.ramerf.wind.core.entity.response.Rs;
import io.github.ramerf.wind.core.helper.ControllerHelper;
import io.github.ramerf.wind.core.util.StringUtils;
import io.swagger.annotations.*;
import java.util.*;
import javax.annotation.Resource;
import javax.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.*;
import springfox.documentation.annotations.ApiIgnore;

@Slf4j
@Controller("tagc")
@PreAuthorize("hasAnyAuthority('global:read','tag:read')")
@RequestMapping(AccessPath.MANAGE + "/tag")
@Api(tags = "文章标签接口")
@SuppressWarnings({"UnusedDeclaration", "unchecked"})
public class TagController {
  @Resource private TagService service;
  @Resource private CommonService commonService;
  @Resource private TagValidator validator;

  @InitBinder
  void initBinder(WebDataBinder binder) {
    binder.addValidators(validator);
  }

  @GetMapping("/index")
  @ApiOperation("文章标签页面")
  public String index() {
    return "tag/index";
  }

  @GetMapping("/page")
  @ResponseBody
  @ApiOperation("获取文章标签列表")
  public ResponseEntity<Rs<Page<TagResponse>>> page(
      @ApiParam("页号,从1开始,当page=size=-1时,表示不分页")
          @RequestParam(value = "page", required = false, defaultValue = "1")
          int page,
      @RequestParam(value = "size", required = false, defaultValue = "10") int size,
      @ApiParam("查询条件") @RequestParam(value = "criteria", required = false) String criteria) {
    return ControllerHelper.page(
        service.page(
            condition ->
                condition.like(StringUtils.nonEmpty(criteria), TagPoJo::setName, criteria + "%"),
            page,
            size,
            null),
        TagResponse::of);
  }

  @GetMapping
  @ApiOperation("添加文章标签页面")
  public String create() {
    return "/manage/tag/edit";
  }

  @PostMapping
  @ResponseBody
  @PreAuthorize("hasAnyAuthority('global:create','tag:create')")
  @ApiOperation("添加文章标签")
  public ResponseEntity<Rs<Object>> create(
      @Valid TagRequest tagRequest, @ApiIgnore BindingResult bindingResult) {
    log.info(" TagController.create : [{}]", tagRequest);
    return ControllerHelper.create(service, tagRequest, bindingResult);
  }

  @GetMapping("/{id}")
  @ApiOperation("更新文章标签页面")
  public String update(@PathVariable("id") long id, @ApiIgnore Map<String, Object> map) {
    if (id < 1) {
      throw CommonException.of("id 格式不正确");
    }
    map.put("tag", service.getById(id));
    return "/manage/tag/edit";
  }

  @PutMapping("/{id}")
  @ResponseBody
  @PreAuthorize("hasAnyAuthority('global:write','tag:write')")
  @ApiOperation("更新文章标签")
  public ResponseEntity<Rs<Object>> update(
      @PathVariable("id") long id,
      @Valid TagRequest tagRequest,
      @ApiIgnore BindingResult bindingResult) {
    log.info("update:[{}]", tagRequest);
    return ControllerHelper.update(service, tagRequest, id, bindingResult);
  }

  @DeleteMapping("/{id}")
  @ResponseBody
  @PreAuthorize("hasAnyAuthority('global:delete','tag:delete')")
  @ApiOperation("删除文章标签")
  public ResponseEntity<Rs<Object>> delete(@PathVariable("id") long id) {
    return ControllerHelper.delete(service, id);
  }

  @DeleteMapping("/deleteBatch")
  @ResponseBody
  @PreAuthorize("hasAnyAuthority('global:delete','tag:delete')")
  @ApiOperation("删除文章标签批量")
  public ResponseEntity<Rs<String>> deleteByIds(@RequestParam("ids") List<Long> ids) {
    return ControllerHelper.deleteByIds(service, ids);
  }
}
