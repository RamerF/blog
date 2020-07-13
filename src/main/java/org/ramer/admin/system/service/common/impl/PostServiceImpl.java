package org.ramer.admin.system.service.common.impl;

import java.util.List;
import java.util.Optional;
import javax.annotation.Resource;
import javax.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;
import org.ramer.admin.system.entity.Constant.State;
import org.ramer.admin.system.entity.domain.common.Post;
import org.ramer.admin.system.exception.CommonException;
import org.ramer.admin.system.repository.BaseRepository;
import org.ramer.admin.system.repository.common.PostRepository;
import org.ramer.admin.system.service.common.ManagerService;
import org.ramer.admin.system.service.common.PostService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

/** @author ramer */
@Slf4j
@Service
public class PostServiceImpl implements PostService {
  @Resource private ManagerService managerService;
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

  /** 如果岗位上有人员,不允许删除岗位 */
  @Transactional(rollbackOn = Exception.class)
  @Override
  public void delete(final long id) throws RuntimeException {
    if (!CollectionUtils.isEmpty(managerService.listByPost(id))) {
      throw new CommonException("无法删除岗位,人员不为空");
    }
    getRepository().deleteById(id);
  }

  /** 如果岗位上有人员,不允许删除岗位 */
  @Transactional(rollbackOn = Exception.class)
  @Override
  public void deleteBatch(final List<Long> ids) throws RuntimeException {
    if (!CollectionUtils.isEmpty(ids)) {
      ids.forEach(
          id -> {
            if (!CollectionUtils.isEmpty(managerService.listByPost(id))) {
              throw new CommonException(
                  String.format(
                      "无法删除岗位[%s],人员不为空",
                      Optional.ofNullable(getById(id)).map(Post::getName).orElse("")));
            }
            getRepository().deleteById(id);
          });
    }
  }

  @SuppressWarnings({"unchecked"})
  @Override
  public <U extends BaseRepository<Post, Long>> U getRepository() throws CommonException {
    return (U) repository;
  }
}
