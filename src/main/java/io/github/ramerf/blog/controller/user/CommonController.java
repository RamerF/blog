package io.github.ramerf.blog.controller.user;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import io.github.ramerf.blog.system.entity.Constant.AccessPath;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * The type Common controller.
 *
 * @author ramer
 */
@Slf4j
@Controller("user_common_controller")
@PreAuthorize("hasRole('user')")
@RequestMapping(AccessPath.USER)
@Api(tags = "用户端: 通用")
public class CommonController {

  @GetMapping
  @ApiOperation("首页")
  public String index() {
    return "index";
  }
}
