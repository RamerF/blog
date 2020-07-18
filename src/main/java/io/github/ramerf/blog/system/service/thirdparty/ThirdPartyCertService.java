package io.github.ramerf.blog.system.service.thirdparty;

import io.github.ramerf.blog.system.entity.domain.thirdparty.ThirdPartyCertificate;
import io.github.ramerf.blog.system.entity.domain.thirdparty.ThirdPartyLog;
import org.springframework.data.domain.Page;

/** @author ramer */
public interface ThirdPartyCertService {
  ThirdPartyLog saveLog(ThirdPartyLog log);

  ThirdPartyCertificate findByCode(String code);

  ThirdPartyCertificate save(String code, String secret, String name, String remark);

  Page<ThirdPartyCertificate> page(String name, Integer page, Integer size);

  ThirdPartyCertificate update(Long id, String code, String secret, String name, String remark);

  void delete(Long id);
}
