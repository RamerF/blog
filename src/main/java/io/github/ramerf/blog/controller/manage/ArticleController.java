package io.github.ramerf.blog.controller.manage;

import io.github.ramerf.blog.entity.pojo.ArticlePoJo;
import io.github.ramerf.blog.entity.request.ArticleRequest;
import io.github.ramerf.blog.entity.response.ArticleResponse;
import io.github.ramerf.blog.service.ArticleService;
import io.github.ramerf.blog.system.entity.Constant.AccessPath;
import io.github.ramerf.blog.system.service.common.CommonService;
import io.github.ramerf.blog.system.service.common.ManagerService;
import io.github.ramerf.blog.validator.ArticleValidator;
import io.github.ramerf.wind.core.entity.response.Rs;
import io.github.ramerf.wind.core.helper.ControllerHelper;
import io.swagger.annotations.*;
import java.util.*;
import javax.annotation.Resource;
import javax.servlet.http.HttpSession;
import javax.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.*;
import springfox.documentation.annotations.ApiIgnore;

@Slf4j
@Controller("articlecm")
@PreAuthorize("hasAnyAuthority('global:read','manage:read','article:read')")
@RequestMapping(AccessPath.MANAGE + "/article")
@Api(tags = "管理端: 文章接口")
@SuppressWarnings({"UnusedDeclaration", "unchecked"})
public class ArticleController {
  @Resource private ManagerService managerService;
  @Resource private ArticleService service;
  @Resource private CommonService commonService;
  @Resource private ArticleValidator validator;

  @InitBinder
  void initBinder(WebDataBinder binder) {
    binder.addValidators(validator);
  }

  @GetMapping("/index")
  @ApiOperation("文章页面")
  public String index(@ApiIgnore HttpSession session, @ApiIgnore Map<String, Object> map) {
    commonService.writeMenuAndSiteInfo(session, map);
    return "manage/article/index";
  }

  @GetMapping("/page")
  @ResponseBody
  @ApiOperation("获取文章列表")
  public ResponseEntity<Rs<Page<ArticleResponse>>> page(
      @ApiParam("页号,从1开始,当page=size=-1时,表示不分页")
          @RequestParam(value = "page", required = false, defaultValue = "1")
          final int page,
      @RequestParam(value = "size", required = false, defaultValue = "10") final int size,
      @ApiParam("查询条件") @RequestParam(value = "criteria", required = false) String criteria) {
    final Page<ArticlePoJo> poJos = service.page(criteria, page, size);
    return Rs.ok(
        new PageImpl<>(
            ArticleResponse.of(poJos.getContent(), managerService),
            poJos.getPageable(),
            poJos.getTotalElements()));
  }

  @GetMapping
  @ApiOperation("添加文章页面")
  public String create(@ApiIgnore HttpSession session, @ApiIgnore Map<String, Object> map) {
    commonService.writeMenuAndSiteInfo(session, map);
    return "manage/article/edit";
  }

  @PostMapping
  @ResponseBody
  @PreAuthorize("hasAnyAuthority('global:create','article:create')")
  @ApiOperation("添加文章")
  public ResponseEntity<Rs<Object>> create(
      @Valid ArticleRequest articleRequest,
      @ApiIgnore BindingResult bindingResult,
      Authentication authentication) {
    final String authName = authentication.getName();
    log.info("create:[authentication:{}]", authName);
    articleRequest.setNumber(service.generateNumber());
    articleRequest.setAuthorId(
        Objects.requireNonNull(managerService.getByEmpNo(authName), "认证对象不能为空").getId());
    log.info("create : [{}]", articleRequest);
    return ControllerHelper.create(service, articleRequest, bindingResult);
  }

  @GetMapping("/{id}")
  @ApiOperation("更新文章页面")
  public String update(
      @PathVariable("id") final Long id,
      @ApiIgnore HttpSession session,
      @ApiIgnore Map<String, Object> map) {
    map.put("article", service.getById(id));
    commonService.writeMenuAndSiteInfo(session, map);
    return "manage/article/edit";
  }

  @PutMapping("/{id}")
  @ResponseBody
  @PreAuthorize("hasAnyAuthority('global:write','article:write')")
  @ApiOperation("更新文章")
  public ResponseEntity<Rs<Object>> update(
      @PathVariable("id") final Long id,
      @Valid ArticleRequest articleRequest,
      @ApiIgnore BindingResult bindingResult) {
    log.info(" ArticleController.update : [{}]", articleRequest);
    return ControllerHelper.update(service, articleRequest, id, bindingResult);
  }

  @DeleteMapping("/{id}")
  @ResponseBody
  @PreAuthorize("hasAnyAuthority('global:delete','article:delete')")
  @ApiOperation("删除文章")
  public ResponseEntity<Rs<Object>> delete(@PathVariable("id") final Long id) {
    return ControllerHelper.delete(service, id);
  }

  @DeleteMapping("/deleteBatch")
  @ResponseBody
  @PreAuthorize("hasAnyAuthority('global:delete','article:delete')")
  @ApiOperation("删除文章批量")
  public ResponseEntity<Rs<String>> deleteBatch(@RequestParam("ids") List<Long> ids) {
    log.info(" ArticleController.deleteBatch : [{}]", ids);
    return ControllerHelper.deleteByIds(service, ids);
  }

  @GetMapping("/{id}/view")
  @ApiOperation("查看文章页面")
  public String view(
      @PathVariable("id") final Long id,
      @ApiIgnore HttpSession session,
      @ApiIgnore Map<String, Object> map) {
    map.put("article", service.getById(id));
    commonService.writeMenuAndSiteInfo(session, map);
    return "manage/article/view";
  }
}
