package org.ramer.admin.controller.manage;

import io.swagger.annotations.*;
import java.util.List;
import java.util.Map;
import javax.annotation.Resource;
import javax.servlet.http.HttpSession;
import javax.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.ramer.admin.entity.domain.Article;
import org.ramer.admin.entity.pojo.ArticlePoJo;
import org.ramer.admin.entity.request.ArticleRequest;
import org.ramer.admin.entity.response.ArticleResponse;
import org.ramer.admin.system.entity.Constant.AccessPath;
import org.ramer.admin.system.entity.response.CommonResponse;
import org.ramer.admin.system.service.common.CommonService;
import org.ramer.admin.service.ArticleService;
import org.ramer.admin.system.util.TextUtil;
import org.ramer.admin.validator.ArticleValidator;
import org.springframework.data.domain.PageImpl;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.*;
import springfox.documentation.annotations.ApiIgnore;

@Slf4j
@Controller("articlecm")
@PreAuthorize("hasAnyAuthority('global:read','article:read')")
@RequestMapping(AccessPath.MANAGE + "/article")
@Api(tags = "管理端: 文章接口")
@SuppressWarnings("UnusedDeclaration")
public class ArticleController {
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
  public ResponseEntity<CommonResponse<PageImpl<ArticleResponse>>> page(
      @ApiParam("页号,从1开始,当page=size=-1时,表示不分页")
          @RequestParam(value = "page", required = false, defaultValue = "1")
          String pageStr,
      @RequestParam(value = "size", required = false, defaultValue = "10") String sizeStr,
      @ApiParam("查询条件") @RequestParam(value = "criteria", required = false) String criteria) {
    final int[] pageAndSize = TextUtil.validFixPageAndSize(pageStr, sizeStr);
    return commonService.page(
        service.page(criteria, pageAndSize[0], pageAndSize[1]), ArticleResponse::of);
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
  public ResponseEntity<CommonResponse<Object>> create(
      @Valid ArticleRequest articleRequest, @ApiIgnore BindingResult bindingResult) {
    log.info(" ArticleController.create : [{}]", articleRequest);
    return commonService.create(service, Article.class, articleRequest, bindingResult);
  }

  @GetMapping("/{id}")
  @ApiOperation("更新文章页面")
  public String update(
      @PathVariable("id") String idStr,
      @ApiIgnore HttpSession session,
      @ApiIgnore Map<String, Object> map) {
    return commonService.update(
        service,
        ArticlePoJo.class,
        idStr,
        "manage/article/edit",
        map,
        "article",
        id -> commonService.writeMenuAndSiteInfo(session, map));
  }

  @PutMapping("/{id}")
  @ResponseBody
  @PreAuthorize("hasAnyAuthority('global:write','article:write')")
  @ApiOperation("更新文章")
  public ResponseEntity<CommonResponse<Object>> update(
      @PathVariable("id") String idStr,
      @Valid ArticleRequest articleRequest,
      @ApiIgnore BindingResult bindingResult) {
    log.info(" ArticleController.update : [{}]", articleRequest);
    final long id = TextUtil.validLong(idStr, -1);
    if (id < 1) {
      return CommonResponse.wrongFormat("id");
    }
    articleRequest.setId(id);
    return commonService.update(service, Article.class, articleRequest, idStr, bindingResult);
  }

  @DeleteMapping("/{id}")
  @ResponseBody
  @PreAuthorize("hasAnyAuthority('global:delete','article:delete')")
  @ApiOperation("删除文章")
  public ResponseEntity<CommonResponse<Object>> delete(@PathVariable("id") String idStr) {
    log.info(" ArticleController.delete : [{}]", idStr);
    return commonService.delete(service, idStr);
  }

  @DeleteMapping("/deleteBatch")
  @ResponseBody
  @PreAuthorize("hasAnyAuthority('global:delete','article:delete')")
  @ApiOperation("删除文章批量")
  public ResponseEntity<CommonResponse<Object>> deleteBatch(@RequestParam("ids") List<Long> ids) {
    log.info(" ArticleController.deleteBatch : [{}]", ids);
    return commonService.deleteBatch(service, ids);
  }
}
