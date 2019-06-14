package org.ramer.admin.system.interceptor;

import java.util.stream.Stream;
import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.ramer.admin.system.entity.domain.common.ManageLog;
import org.ramer.admin.system.service.common.ManageLogService;
import org.ramer.admin.system.service.common.ManagerService;
import org.ramer.admin.system.util.IpUtils;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.context.support.WebApplicationContextUtils;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

/**
 * 管理端操作日志记录.
 *
 * @author Administrator
 */
@Slf4j
@Component
public class OperateLogInterceptor implements HandlerInterceptor {
  @Resource private ManageLogService logService;
  @Resource private ManagerService managerService;

  @Override
  public void postHandle(
      HttpServletRequest request,
      HttpServletResponse response,
      Object handler,
      ModelAndView modelAndView) {
    Stream<String> interceptMethods =
        Stream.of(RequestMethod.POST.name(), RequestMethod.PUT.name(), RequestMethod.DELETE.name());
    if (interceptMethods.anyMatch(method -> request.getMethod().equals(method))) {
      ManageLog manageLog = new ManageLog();
      manageLog.setUrl(request.getRequestURL().toString());
      manageLog.setIp(IpUtils.getRealIP(request));
      final Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
      if (managerService == null) {
        BeanFactory factory =
            WebApplicationContextUtils.getRequiredWebApplicationContext(
                request.getServletContext());
        managerService = (ManagerService) factory.getBean("managerServiceImpl");
      }
      if (authentication != null) {
        manageLog.setManager(managerService.getByEmpNo(authentication.getName()));
      }
      if (logService == null) {
        BeanFactory factory =
            WebApplicationContextUtils.getRequiredWebApplicationContext(
                request.getServletContext());
        logService = (ManageLogService) factory.getBean("manageLogServiceImpl");
      }
      logService.create(manageLog);
      log.info(
          " OperateLogInterceptor.postHandle : [{}]:{},{}",
          request.getMethod(),
          request.getRequestURL().toString(),
          manageLog);
    }
  }
}
