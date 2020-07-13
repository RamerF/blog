package org.ramer.admin.repository;

import org.ramer.admin.entity.domain.Article;
import org.ramer.admin.system.repository.BaseRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ArticleRepository extends BaseRepository<Article, Long> {}
