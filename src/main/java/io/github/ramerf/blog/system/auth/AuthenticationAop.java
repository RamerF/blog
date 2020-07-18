package io.github.ramerf.blog.system.auth;

import java.util.Objects;
import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import io.github.ramerf.blog.system.entity.Constant.ResultCode;
import io.github.ramerf.blog.system.entity.domain.thirdparty.ThirdPartyCertificate;
import io.github.ramerf.blog.system.entity.domain.thirdparty.ThirdPartyLog;
import io.github.ramerf.blog.system.entity.response.thirdparty.ThirdPartyResponse;
import io.github.ramerf.blog.system.service.thirdparty.ThirdPartyCertService;
import io.github.ramerf.blog.system.util.IpUtils;
import io.github.ramerf.blog.system.util.RMIUtil;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

/** @author ramer */
@Component
@Aspect
@Slf4j
public class AuthenticationAop {
  @Resource private ThirdPartyCertService thirdPartyCertService;

  @Around("execution(@io.github.ramerf.blog.system.auth.annotation.RequiredAuthentication * *(..))")
  public Object handlerAuthentication(ProceedingJoinPoint joinPoint) throws Throwable {
    HttpServletRequest request =
        ((ServletRequestAttributes)
                Objects.requireNonNull(RequestContextHolder.getRequestAttributes()))
            .getRequest();
    String code = request.getParameter("code");
    String signed = request.getParameter("signed");
    String data = request.getParameter("data");
    log.info(
        " AuthenticationAop.handlerAuthentication : [url={},data={},code={},signed={}]",
        request.getRequestURL().toString(),
        data,
        code,
        signed);
    Object result = new ThirdPartyResponse(false, ResultCode.E0001);
    if (code != null && data != null && signed != null) {
      ThirdPartyCertificate certificate = thirdPartyCertService.findByCode(code);
      if (certificate == null) {
        return new ThirdPartyResponse(false, ResultCode.E0001);
      }
      if (certificate.getIsDelete().equals(Boolean.FALSE)) {
        return new ThirdPartyResponse(false, ResultCode.E0002);
      }
      if (RMIUtil.md5Encode(certificate.getSecret().concat(data)).equalsIgnoreCase(signed)) {
        log.debug(" AuthenticationAop.handlerAuthentication outer request: success");
        result = joinPoint.proceed();
      } else {
        log.debug(" AuthenticationAop.handlerAuthentication outer request: authentication fail");
        return new ThirdPartyResponse(false, ResultCode.E0003);
      }
      ThirdPartyLog log = new ThirdPartyLog();
      String realIP = IpUtils.getRealIP(request);
      log.setIp(realIP);
      log.setUrl(request.getRequestURL().toString());
      log.setThirdPartyCertificate(certificate);
      if (result instanceof ThirdPartyResponse) {
        ThirdPartyResponse response = (ThirdPartyResponse) result;
        log.setResult(response.isSuccess() ? "调用成功" : response.getMessage());
      }
      thirdPartyCertService.saveLog(log);
    }
    return result;
  }
}
