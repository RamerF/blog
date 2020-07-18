package io.github.ramerf.blog.system.service.common.impl;

import java.util.List;
import java.util.Optional;
import javax.annotation.Resource;
import javax.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;
import io.github.ramerf.blog.system.entity.domain.common.Post;
import io.github.ramerf.blog.system.exception.CommonException;
import io.github.ramerf.blog.system.repository.BaseRepository;
import io.github.ramerf.blog.system.repository.common.PostRepository;
import io.github.ramerf.blog.system.service.common.ManagerService;
import io.github.ramerf.blog.system.service.common.PostService;
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
        ? repository.findByOrganizeIdAndIsDelete(organizeId, Boolean.FALSE, pageable)
        : repository.findByOrganizeIdAndNameLikeAndIsDelete(
            organizeId, "%" + criteria + "%", Boolean.FALSE, pageable);
  }

  /** 如果岗位上有人员,不允许删除岗位 */
  @Transactional(rollbackOn = Exception.class)
  @Override
  public void delete(final long id) throws RuntimeException {
    if (!CollectionUtils.isEmpty(managerService.listByPost(id))) {
      throw CommonException.of("无法删除岗位,人员不为空");
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
              throw CommonException.of(
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
