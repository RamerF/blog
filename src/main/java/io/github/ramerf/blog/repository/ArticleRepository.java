package io.github.ramerf.blog.repository;

import io.github.ramerf.blog.entity.domain.Article;
import io.github.ramerf.blog.system.repository.BaseRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ArticleRepository extends BaseRepository<Article, Long> {}
