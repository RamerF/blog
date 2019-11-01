package org.ramer.admin.system.repository.thirdparty;

import org.ramer.admin.system.entity.domain.thirdparty.ThirdPartyLog;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * @author ramer
 * @version 7/1/2018
 * @see
 */
@Repository
public interface ThirdPartyLogRepository extends JpaRepository<ThirdPartyLog, Long> {}
