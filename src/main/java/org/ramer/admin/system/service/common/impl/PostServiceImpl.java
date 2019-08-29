package org.ramer.admin.system.service.common.impl;

import javax.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.ramer.admin.system.entity.Constant.State;
import org.ramer.admin.system.entity.domain.common.Post;
import org.ramer.admin.system.exception.CommonException;
import org.ramer.admin.system.repository.BaseRepository;
import org.ramer.admin.system.repository.common.PostRepository;
import org.ramer.admin.system.service.common.PostService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

/** @author ramer */
@Slf4j
@Service
public class PostServiceImpl implements PostService {
  @Resource private PostRepository repository;

  @Override
  public Page<Post> page(
      final long organizeId, final String criteria, final int page, final int size) {
    final PageRequest pageable = pageRequest(page, size);
    return StringUtils.isEmpty(criteria)
        ? repository.findByOrganizeIdAndState(organizeId, State.STATE_ON, pageable)
        : repository.findByOrganizeIdAndNameLikeAndState(
            organizeId, "%" + criteria + "%", State.STATE_ON, pageable);
  }

  @SuppressWarnings({"unchecked"})
  @Override
  public <U extends BaseRepository<Post, Long>> U getRepository() throws CommonException {
    return (U) repository;
  }
}
