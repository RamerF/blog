package io.github.ramerf.blog.controller.user;

import io.github.ramerf.blog.entity.pojo.AccountBrowserArticlePoJo;
import io.github.ramerf.blog.service.AccountBrowserArticleService;
import io.github.ramerf.blog.service.AccountService;
import io.github.ramerf.blog.system.entity.Constant.AccessPath;
import io.github.ramerf.wind.core.entity.response.ResultCode;
import io.github.ramerf.wind.core.entity.response.Rs;
import io.github.ramerf.wind.core.helper.ControllerHelper;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import javax.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

/**
 * The type Common controller.
 *
 * @author ramer
 */
@Slf4j
@RestController("account_browser_article_controller")
@PreAuthorize("hasRole('user')")
@RequestMapping(AccessPath.USER)
@Api(tags = "用户端: 账户浏览文章")
public class AccountBrowserArticleController {
  @Resource private AccountBrowserArticleService service;
  @Resource private AccountService accountService;

  @PostMapping
  @PreAuthorize("hasAnyAuthority('global:create','accountBrowserArticle:create')")
  @ApiOperation("添加浏览记录")
  public ResponseEntity<Rs<Object>> create(Authentication authentication, final long articleId) {
    AccountBrowserArticlePoJo poJo = new AccountBrowserArticlePoJo();
    poJo.setAccountId(accountService.getLoginId(authentication.getName()));
    poJo.setArticleId(articleId);
    return ControllerHelper.create(service, poJo, ResultCode.API_FAIL_EXEC_CREATE);
  }
}
