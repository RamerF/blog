package io.github.ramerf.blog.controller;

import io.github.ramerf.blog.entity.pojo.ArticlePoJo;
import io.github.ramerf.blog.service.ArticleService;
import io.github.ramerf.wind.core.condition.SortColumn;
import io.github.ramerf.wind.core.condition.SortColumn.Order;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import java.util.Map;
import javax.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

/**
 * The type Common controller.
 *
 * @author ramer
 */
@Slf4j
@Controller("common_controller")
@Api(tags = "全局")
@RequestMapping("/")
public class CommonController {
  @Resource private ArticleService articleService;

  @GetMapping
  @ApiOperation("首页")
  public String index(
      @RequestParam(value = "page", defaultValue = "1") int page, Map<String, Object> map) {
    map.put(
        "articles",
        articleService.list(null, page, 1, SortColumn.by(ArticlePoJo::getCreateTime, Order.DESC)));
    return "user/index";
  }
}
