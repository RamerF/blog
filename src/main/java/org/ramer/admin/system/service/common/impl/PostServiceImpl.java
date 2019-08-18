package org.ramer.admin.system.service.common.impl;

import org.ramer.admin.system.entity.Constant;
import org.ramer.admin.system.entity.domain.common.Post;
import org.ramer.admin.system.exception.CommonException;
import org.ramer.admin.system.repository.BaseRepository;
import org.ramer.admin.system.repository.common.PostRepository;
import org.ramer.admin.system.service.BaseService;
import org.ramer.admin.system.service.common.PostService;
import org.ramer.admin.system.util.TextUtil;
import java.util.*;
import javax.annotation.Resource;
import javax.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.*;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

/** @author ramer */
@Slf4j
@Service
public class PostServiceImpl implements PostService {
  @Resource private PostRepository repository;

  @SuppressWarnings({"unchecked"})
  @Override
  public <U extends BaseRepository<Post, Long>> U getRepository() throws CommonException {
    return (U) repository;
  }
}
