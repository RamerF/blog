package io.github.ramerf.blog.controller.user;

import io.github.ramerf.blog.system.entity.Constant.AccessPath;
import io.swagger.annotations.Api;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * The type Account controller.
 *
 * @author ramer
 */
@Slf4j
@Controller("account_controller")
@PreAuthorize("hasRole('user')")
@RequestMapping(AccessPath.USER + "/account")
@Api(tags = "账户接口")
public class AccountController {}
