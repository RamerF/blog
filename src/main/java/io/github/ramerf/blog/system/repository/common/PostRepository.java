package io.github.ramerf.blog.system.repository.common;

import io.github.ramerf.blog.system.entity.domain.common.Post;
import io.github.ramerf.blog.system.repository.BaseRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

@Repository
public interface PostRepository extends BaseRepository<Post, Long> {
  Page<Post> findByOrganizeIdAndIsDelete(
      final long organizeId, final Boolean isDelete, final Pageable pageable);

  Page<Post> findByOrganizeIdAndNameLikeAndIsDelete(
      final long organizeId, final String criteria, final Boolean isDelete, final Pageable pageable);
}
