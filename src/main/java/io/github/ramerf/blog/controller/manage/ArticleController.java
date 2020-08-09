package io.github.ramerf.blog.controller.manage;

import io.github.ramerf.blog.entity.pojo.ArticlePoJo;
import io.github.ramerf.blog.entity.pojo.ArticleTagMapPoJo;
import io.github.ramerf.blog.entity.request.ArticleRequest;
import io.github.ramerf.blog.entity.response.ArticleResponse;
import io.github.ramerf.blog.service.*;
import io.github.ramerf.blog.system.entity.Constant.AccessPath;
import io.github.ramerf.blog.system.service.common.CommonService;
import io.github.ramerf.blog.system.service.common.ManagerService;
import io.github.ramerf.wind.core.entity.response.ResultCode;
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
import org.springframework.web.bind.annotation.*;
import springfox.documentation.annotations.ApiIgnore;

import static java.util.stream.Collectors.toList;

/**
 * The type Article controller.
 * @author Tang xiaofeng
 */
@Slf4j
@Controller("articlecm")
@PreAuthorize("hasAnyAuthority('global:read','manage:read','article:read')")
@RequestMapping(AccessPath.MANAGE + "/article")
@Api(tags = "管理端: 文章接口")
public class ArticleController {
  @Resource private ManagerService managerService;
  @Resource private ArticleService service;
  @Resource private CommonService commonService;
  @Resource private TagService tagService;
  @Resource private ArticleTagMapService mapService;

  /**
   * Index string.
   *
   * @param session the session
   * @param map the map
   * @return the string
   */
@GetMapping("/index")
  @ApiOperation("文章页面")
  public String index(@ApiIgnore HttpSession session, @ApiIgnore Map<String, Object> map) {
    commonService.writeMenuAndSiteInfo(session, map);
    return "manage/article/index";
  }

  /**
   * Page response entity.
   *
   * @param page the page
   * @param size the size
   * @param criteria the criteria
   * @return the response entity
   */
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

  /**
   * Create string.
   *
   * @param session the session
   * @param map the map
   * @return the string
   */
@GetMapping
  @ApiOperation("添加文章页面")
  public String create(@ApiIgnore HttpSession session, @ApiIgnore Map<String, Object> map) {
    commonService.writeMenuAndSiteInfo(session, map);
    return "manage/article/edit";
  }

  /**
   * Create response entity.
   *
   * @param articleRequest the article request
   * @param bindingResult the binding result
   * @param authentication the authentication
   * @return the response entity
   */
@PostMapping
  @ResponseBody
  @PreAuthorize("hasAnyAuthority('global:create','article:create')")
  @ApiOperation("添加文章")
  public ResponseEntity<Rs<String>> create(
      @Valid ArticleRequest articleRequest,
      @ApiIgnore BindingResult bindingResult,
      Authentication authentication) {
    if(bindingResult.hasErrors()){
      return Rs.fail(ControllerHelper.collectBindingResult(bindingResult));
    }
    final String authName = authentication.getName();
    log.info("create:[authentication:{}]", authName);
    articleRequest.setNumber(service.generateNumber());
    articleRequest.setAuthorId(
        Objects.requireNonNull(managerService.getByEmpNo(authName), "认证对象不能为空").getId());
    log.info("create:[{}]", articleRequest);
    return ControllerHelper.exec(
        service.create(articleRequest),
        result -> Rs.ok(ResultCode.API_SUCCESS_EXEC_CREATE),
        ResultCode.API_FAIL_EXEC_CREATE);
  }

  /**
   * Update string.
   *
   * @param id the id
   * @param session the session
   * @param map the map
   * @return the string
   */
@GetMapping("/{id}")
  @ApiOperation("更新文章页面")
  public String update(
      @PathVariable("id") final Long id,
      @ApiIgnore HttpSession session,
      @ApiIgnore Map<String, Object> map) {
    final ArticlePoJo poJo = service.getById(id);
    poJo.getId();
  ArticleResponse response =
    Objects.requireNonNull(ArticleResponse.of(poJo, null));
   final List<Long> tagIds = mapService
      .list(condition -> condition.eq(ArticleTagMapPoJo::setArticleId, poJo.getId())).stream().map(ArticleTagMapPoJo::getTagId).collect(toList());
    response.setTags(tagService.listByIds(tagIds));
    map.put("article", response);
    commonService.writeMenuAndSiteInfo(session, map);
    return "manage/article/edit";
  }

  /**
   * Update response entity.
   *
   * @param articleRequest the article request
   * @param bindingResult the binding result
   * @return the response entity
   */
@PutMapping("/{id}")
  @ResponseBody
  @PreAuthorize("hasAnyAuthority('global:write','article:write')")
  @ApiOperation("更新文章")
  public ResponseEntity<Rs<String>> update(
      @Valid ArticleRequest articleRequest,
      @ApiIgnore BindingResult bindingResult) {
    log.info("update:[{}]", articleRequest);
    if(bindingResult.hasErrors()){
      return Rs.fail(ControllerHelper.collectBindingResult(bindingResult));
    }
    return ControllerHelper.exec(
        service.update(articleRequest),
        result -> Rs.ok(ResultCode.API_SUCCESS_EXEC_UPDATE),
        ResultCode.API_FAIL_EXEC_UPDATE);
  }

  /**
   * Delete response entity.
   *
   * @param id the id
   * @return the response entity
   */
@DeleteMapping("/{id}")
  @ResponseBody
  @PreAuthorize("hasAnyAuthority('global:delete','article:delete')")
  @ApiOperation("删除文章")
  public ResponseEntity<Rs<Object>> delete(@PathVariable("id") final Long id) {
    return ControllerHelper.delete(service, id);
  }

  /**
   * Delete batch response entity.
   *
   * @param ids the ids
   * @return the response entity
   */
@DeleteMapping("/deleteBatch")
  @ResponseBody
  @PreAuthorize("hasAnyAuthority('global:delete','article:delete')")
  @ApiOperation("删除文章批量")
  public ResponseEntity<Rs<String>> deleteBatch(@RequestParam("ids") List<Long> ids) {
    log.info(" ArticleController.deleteBatch : [{}]", ids);
    return ControllerHelper.deleteByIds(service, ids);
  }

  /**
   * View string.
   *
   * @param id the id
   * @param session the session
   * @param map the map
   * @return the string
   */
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
