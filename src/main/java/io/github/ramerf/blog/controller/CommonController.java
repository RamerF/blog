package io.github.ramerf.blog.controller;

import io.github.ramerf.blog.service.ArticleService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import java.util.Map;
import javax.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

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
  public String index(Map<String, Object> map) {
    map.put("articles", articleService.list(null));
    return "user/index";
  }
}
