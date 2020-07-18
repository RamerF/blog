package io.github.ramerf.blog.system.repository.thirdparty;

import io.github.ramerf.blog.system.entity.domain.thirdparty.ThirdPartyCertificate;
import io.github.ramerf.blog.system.repository.BaseRepository;
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
