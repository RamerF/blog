package io.github.ramerf.blog.controller.user;

import io.github.ramerf.blog.entity.response.ArticleResponse;
import io.github.ramerf.blog.service.*;
import io.github.ramerf.blog.system.entity.Constant.AccessPath;
import io.github.ramerf.blog.system.service.common.CommonService;
import io.github.ramerf.blog.system.service.common.ManagerService;
import io.github.ramerf.wind.core.entity.response.Rs;
import io.github.ramerf.wind.core.helper.ControllerHelper;
import io.swagger.annotations.*;
import java.util.Map;
import javax.annotation.Resource;
import javax.servlet.http.HttpSession;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import springfox.documentation.annotations.ApiIgnore;

/**
 * The type Article controller.
 *
 * @author ramer
 */
@Slf4j
@Controller("article_controller")
@PreAuthorize("hasRole('user')")
@RequestMapping(AccessPath.USER + "/article")
@Api(tags = "用户端: 文章接口")
public class ArticleController {
  @Resource private ManagerService managerService;
  @Resource private ArticleService service;
  @Resource private CommonService commonService;
  @Resource private AccountBrowserArticleService browserArticleService;
  @Resource private AccountService accountService;

  /**
   * View string.
   *
   * @param id the id
   * @param session the session
   * @param map the map
   * @return the string
   */
  @GetMapping("/{id}")
  @ApiOperation("文章详情页面")
  public String detail(
      @PathVariable("id") final Long id,
      @ApiIgnore HttpSession session,
      @ApiIgnore Map<String, Object> map) {
    map.put("article", service.getById(id));
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
    return ControllerHelper.page(
        service.page(criteria, page, size), article -> ArticleResponse.of(article, managerService));
  }

  /**
   * View string.
   *
   * @param authentication the authentication
   * @param map the map
   * @return the string
   */
  @GetMapping("/browser-history")
  @ApiOperation("文章浏览历史页面")
  public String browserHistory(Authentication authentication, @ApiIgnore Map<String, Object> map) {
    final Long loginId = accountService.getLoginId(authentication.getName());
    map.put("articles", service.listByIds(browserArticleService.listByAccountId(loginId)));
    return "user/article/browser-history";
  }
}
