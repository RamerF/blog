package org.ramer.admin.controller;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

/**
 * The type Common controller.
 *
 * @author ramer
 */
@Slf4j
@Controller("common_controller")
@Api(tags = "全局")
public class CommonController {

  @GetMapping
  @ApiOperation("首页")
  public String index() {
    return "index";
  }
}
