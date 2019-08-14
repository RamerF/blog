package org.ramer.admin.system.config;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.ramer.admin.system.entity.domain.common.ManageLog;
import org.ramer.admin.system.entity.response.CommonResponse;
import org.ramer.admin.system.exception.CommonException;
import org.ramer.admin.system.service.common.ManageLogService;
import org.ramer.admin.system.service.common.ManagerService;
import org.ramer.admin.system.util.IpUtils;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.Authentication;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.support.StandardMultipartHttpServletRequest;

@RestController
@ControllerAdvice
@Slf4j
public class GlobalExceptionHandler {
  @Resource private ManagerService managerService;
  @Resource private ManageLogService manageLogService;

  @ExceptionHandler(value = Exception.class)
  public ResponseEntity handle(
      HttpServletRequest request, Exception exception, Authentication authentication) {
    log.error(request.getRequestURL().toString());
    if (request instanceof StandardMultipartHttpServletRequest) {
      log.warn("上传文件过大");
      return CommonResponse.fail("上传文件过大");
    }
    if (exception instanceof AccessDeniedException) {
      log.warn(exception.getMessage());
      ManageLog manageLogs = new ManageLog();
      manageLogs.setIp(IpUtils.getRealIP(request));
      if (authentication != null && authentication.getName() != null) {
        manageLogs.setManager(managerService.getByEmpNo(authentication.getName()));
      }
      manageLogs.setUrl(request.getRequestURL().toString());
      manageLogs.setResult(exception.toString());
      try {
        manageLogService.create(manageLogs);
      } catch (Exception e) {
        log.error("记录操作日志失败");
        log.error(e.getMessage(), e);
      }
      return CommonResponse.fail("拒绝访问");
    }
    if (exception instanceof CommonException) {
      log.error(exception.getMessage(), exception);
      request.setAttribute("error", exception.getMessage());
      return CommonResponse.fail(exception.getMessage());
    }
    if (exception instanceof HttpRequestMethodNotSupportedException) {
      return CommonResponse.fail("请求方式不支持");
    }
    log.error(exception.getMessage(), exception);
    return CommonResponse.fail("系统繁忙,请稍后再试");
  }
}
