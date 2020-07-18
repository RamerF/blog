package io.github.ramerf.blog.system.service.common;

import io.github.ramerf.blog.system.entity.domain.common.Post;
import io.github.ramerf.blog.system.entity.pojo.common.PostPoJo;
import io.github.ramerf.blog.system.service.BaseService;
import org.springframework.data.domain.Page;

/** @author ramer */
public interface PostService extends BaseService<Post, PostPoJo> {
  Page<Post> page(long organizeId, String criteria, int page, int size);
}
