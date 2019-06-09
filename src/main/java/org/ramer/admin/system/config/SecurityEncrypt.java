package org.ramer.admin.system.config;

import java.time.LocalDateTime;
import javax.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.ramer.admin.system.entity.Constant.State;
import org.ramer.admin.system.entity.domain.common.Manager;
import org.ramer.admin.system.service.common.ManagerService;
import org.ramer.admin.system.util.EncryptUtil;
import org.springframework.security.authentication.*;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Component;

/** Created by RAMER on 5/25/2017. */
@Slf4j
@Component
public class SecurityEncrypt implements AuthenticationProvider {
  @Resource private UserDetailsService userService;
  @Resource private ManagerService managerService;

  @Override
  public Authentication authenticate(Authentication authentication) throws AuthenticationException {
    String empNo = authentication.getName();
    Object credentials = authentication.getCredentials();
    log.debug(" empNo : {}", empNo);
    log.debug(" password : {}", credentials);
    UserDetails user = userService.loadUserByUsername(empNo);
    if (!EncryptUtil.matches(credentials.toString(), user.getPassword())) {
      managerService.setLoginStatus(empNo);
      throw new BadCredentialsException("bad password");
    }
    final Manager manager = managerService.getByEmpNo(empNo);
    if (manager.getState().equals(State.STATE_OFF)
        || manager.getValidDate() == null
        || manager.getValidDate().isBefore(LocalDateTime.now())
        || !manager.getIsActive()) {
      throw new BadCredentialsException("bad password");
    }
    return new UsernamePasswordAuthenticationToken(user, credentials, user.getAuthorities());
  }

  @Override
  public boolean supports(Class<?> authentication) {
    return true;
  }
}
