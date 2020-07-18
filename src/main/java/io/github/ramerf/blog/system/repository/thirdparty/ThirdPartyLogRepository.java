package io.github.ramerf.blog.system.repository.thirdparty;

import io.github.ramerf.blog.system.entity.domain.thirdparty.ThirdPartyLog;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * @author ramer
 * @version 7/1/2018
 * @see
 */
@Repository
public interface ThirdPartyLogRepository extends JpaRepository<ThirdPartyLog, Long> {}
