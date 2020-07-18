package io.github.ramerf.blog.repository;

import io.github.ramerf.blog.entity.domain.Tag;
import io.github.ramerf.blog.system.repository.BaseRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TagRepository extends BaseRepository<Tag, Long> {}
