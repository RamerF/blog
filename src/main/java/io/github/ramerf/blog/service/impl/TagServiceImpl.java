package io.github.ramerf.blog.service.impl;

import javax.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import io.github.ramerf.blog.repository.TagRepository;
import io.github.ramerf.blog.service.TagService;
import org.springframework.stereotype.Service;

/** @author ramer */
@Slf4j
@Service
public class TagServiceImpl implements TagService {
  @Resource private TagRepository repository;

  @Override
  @SuppressWarnings("unchecked")
  public <U> U getRepository() throws RuntimeException {
    return (U) repository;
  }
}
