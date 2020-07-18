package io.github.ramerf.blog.system.controller.common;

import io.github.ramerf.wind.core.entity.response.Rs;
import io.swagger.annotations.*;
import java.util.Map;
import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import io.github.ramerf.blog.system.entity.domain.common.ManageLog;
import io.github.ramerf.blog.system.service.common.*;
import io.github.ramerf.blog.system.util.ImageUtil;
import io.github.ramerf.blog.system.util.IpUtils;
import io.github.ramerf.blog.system.util.PathUtil.SavingFolder;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import springfox.documentation.annotations.ApiIgnore;

/** @author ramer */
@Slf4j
@Controller("common")
@Api(tags = "全局通用接口")
public class CommonController {
  @Resource private ManagerService managerService;
  @Resource private ManageLogService manageLogService;
  @Resource private ConfigService configService;

  @ApiIgnore
  @GetMapping("/400")
  @ApiOperation("400错误")
  public String error400() {
    return "common/400";
  }

  @ApiIgnore
  @GetMapping("/401")
  @ApiOperation("401错误")
  public String error401() {
    return "common/401";
  }

  @ApiIgnore
  @GetMapping("/404")
  @ApiOperation("404错误")
  public String error404() {
    return "common/404";
  }

  @ApiIgnore
  @GetMapping("/templateEngineException")
  @ApiOperation("模板引擎异常")
  public String templateEngineException(HttpServletRequest request, Map<String, Object> map) {
    map.put("error", "Internal Server Error");
    log.info(" CommonController.templateEngineException : [{}]", request.getRequestURL());
    return "common/500";
  }

  @ApiIgnore
  @RequestMapping("/forbidden")
  @ResponseBody
  @ApiOperation("拒绝访问")
  public ResponseEntity forbidden(HttpServletRequest request, Authentication authentication) {
    ManageLog manageLogs = new ManageLog();
    manageLogs.setIp(IpUtils.getRealIP(request));
    if (authentication != null && authentication.getName() != null) {
      manageLogs.setManager(managerService.getByEmpNo(authentication.getName()));
    }
    manageLogs.setUrl(request.getRequestURL().toString());
    manageLogs.setResult(new AccessDeniedException("拒绝访问").toString());
    log.warn(
        " CommonController.forbidden : [{},{}]",
        request.getRequestURL(),
        authentication == null ? null : authentication.getName());
    try {
      manageLogService.create(manageLogs);
    } catch (Exception e1) {
      log.warn("记录操作日志失败");
    }
    return new ResponseEntity<>(Rs.fail("拒绝访问"), HttpStatus.FORBIDDEN);
  }

  @PostMapping("/upload/uploadFile")
  @ResponseBody
  @ApiOperation("通用文件上传")
  public ResponseEntity uploadFile(
      @RequestParam("file") MultipartFile file,
      @ApiParam("文件后缀") @RequestParam(value = "fileSuffix", required = false) String fileSuffix,
      @ApiParam("文件名") @RequestParam(value = "name", required = false) String name) {
    String path = ImageUtil.save(file, name, SavingFolder.PUBLIC, fileSuffix);
    return path != null ? Rs.ok(path) : Rs.fail("上传失败");
  }
}
