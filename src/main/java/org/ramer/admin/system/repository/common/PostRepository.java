package org.ramer.admin.system.repository.common;

import org.ramer.admin.system.entity.domain.common.Post;
import org.ramer.admin.system.repository.BaseRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

@Repository
public interface PostRepository extends BaseRepository<Post, Long> {
  Page<Post> findByOrganizeIdAndState(
      final long organizeId, final int state, final Pageable pageable);

  Page<Post> findByOrganizeIdAndNameLikeAndState(
      final long organizeId, final String criteria, final int state, final Pageable pageable);
}
