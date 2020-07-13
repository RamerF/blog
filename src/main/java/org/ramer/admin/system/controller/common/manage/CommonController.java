package org.ramer.admin.system.controller.common.manage;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import java.util.Map;
import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import lombok.extern.slf4j.Slf4j;
import org.ramer.admin.system.entity.Constant.AccessPath;
import org.ramer.admin.system.entity.Constant.ConfigCode;
import org.ramer.admin.system.entity.domain.common.ManageLog;
import org.ramer.admin.system.entity.domain.common.Manager;
import org.ramer.admin.system.entity.response.Rs;
import org.ramer.admin.system.service.common.*;
import org.ramer.admin.system.util.ImageUtil;
import org.ramer.admin.system.util.IpUtils;
import org.ramer.admin.system.util.PathUtil.SavingFolder;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@Slf4j
@Controller("cmcc")
@RequestMapping(AccessPath.MANAGE)
@Api(tags = "[系统: 管理端]: 通用接口")
public class CommonController {
  @Resource private CommonService commonService;
  @Resource private ManagerService managerService;
  @Resource private ConfigService configService;
  @Resource private ManageLogService manageLogService;

  @PostMapping("/upload/uploadFile")
  @ApiOperation("文件上传")
  public ResponseEntity uploadFile(
      @RequestParam("file") MultipartFile file,
      @RequestParam(value = "fileSuffix", required = false) String fileSuffix,
      @RequestParam(value = "name", required = false) String name) {
    String path = ImageUtil.save(file, name, SavingFolder.MANAGER, fileSuffix);
    return path != null ? Rs.ok(path) : Rs.fail("上传失败");
  }

  @GetMapping("/welcome")
  @ApiOperation("欢迎页面")
  public String welcome(HttpSession session, Map<String, Object> map) {
    commonService.writeMenuAndSiteInfo(session, map);
    return "manage/welcome";
  }

  @GetMapping("/index")
  @ApiOperation("首页")
  public String index(HttpSession session, Map<String, Object> map) {
    commonService.writeMenuAndSiteInfo(session, map);
    return "manage/index";
  }

  @GetMapping("/login")
  @ApiOperation("登录页")
  public String login(Map<String, Object> map) {
    map.put("title", configService.getSiteInfo(ConfigCode.SITE_TITLE));
    return "login";
  }

  @PostMapping(value = "/sign_in")
  @ApiOperation("认证登录")
  public ResponseEntity signIn(HttpServletRequest request, HttpSession session) {
    log.info(
        " CommonController.signIn : [{}]", session.getAttribute("SPRING_SECURITY_SAVED_REQUEST"));
    final Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
    ManageLog manageLogs = new ManageLog();
    manageLogs.setIp(IpUtils.getRealIP(request));
    manageLogs.setUrl(request.getRequestURL().toString());
    if (authentication != null && authentication.isAuthenticated()) {
      log.debug(" CommonController.signIn : [{}]", authentication.getName());
      final Manager manager = managerService.getByEmpNo(authentication.getName());
      manageLogs.setManager(manager);
      manageLogs.setResult("login success");
      manageLogService.create(manageLogs);
      session.setAttribute("manager", manager);
      return Rs.ok(null, "登录成功");
    }
    manageLogs.setResult("login fail");
    manageLogService.create(manageLogs);
    return Rs.fail("用户名或密码错误");
  }
}
