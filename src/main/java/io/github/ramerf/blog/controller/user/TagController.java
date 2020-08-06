package io.github.ramerf.blog.controller.user;

import io.github.ramerf.blog.entity.response.ArticleResponse;
import io.github.ramerf.blog.service.TagService;
import io.github.ramerf.blog.system.entity.Constant.AccessPath;
import io.github.ramerf.wind.core.entity.response.Rs;
import io.github.ramerf.wind.core.helper.ControllerHelper;
import io.swagger.annotations.*;
import javax.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

/**
 * The type Article controller.
 *
 * @author ramer
 */
@Slf4j
@Controller("tag_controller")
// @PreAuthorize("hasRole('user')")
@RequestMapping(AccessPath.USER + "/tag")
@Api(tags = "用户端: 文章标签接口")
public class TagController {
  @Resource private TagService service;

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
  @ApiOperation("获取文章标签列表")
  public ResponseEntity<Rs<Page<ArticleResponse>>> page(
      @ApiParam("页号,从1开始,当page=size=-1时,表示不分页")
          @RequestParam(value = "page", required = false, defaultValue = "1")
          final int page,
      @RequestParam(value = "size", required = false, defaultValue = "10") final int size,
      @ApiParam("查询条件") @RequestParam(value = "criteria", required = false) String criteria) {
    return ControllerHelper.page(service.page(criteria, page, size), null);
  }
}
