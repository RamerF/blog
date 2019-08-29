package org.ramer.admin.system.service.common;

import org.ramer.admin.system.entity.domain.common.Post;
import org.ramer.admin.system.entity.pojo.common.PostPoJo;
import org.ramer.admin.system.service.BaseService;
import org.springframework.data.domain.Page;

/** @author ramer */
public interface PostService extends BaseService<Post, PostPoJo> {
  Page<Post> page(long organizeId, String criteria, int page, int size);
}
