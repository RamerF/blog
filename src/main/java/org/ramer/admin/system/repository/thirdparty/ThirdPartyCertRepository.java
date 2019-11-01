package org.ramer.admin.system.repository.thirdparty;

import org.ramer.admin.system.entity.domain.thirdparty.ThirdPartyCertificate;
import org.ramer.admin.system.repository.BaseRepository;
import org.springframework.stereotype.Repository;

/**
 * @author ramer
 * @version 6/29/2018
 * @see
 */
@Repository
public interface ThirdPartyCertRepository
    extends BaseRepository<ThirdPartyCertificate, Long> {
  ThirdPartyCertificate findByCode(String code);
}
