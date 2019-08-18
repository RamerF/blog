package org.ramer.admin.system.repository.common;

import org.ramer.admin.system.entity.domain.common.Post;
import org.ramer.admin.system.repository.BaseRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PostRepository extends BaseRepository<Post, Long> {}
