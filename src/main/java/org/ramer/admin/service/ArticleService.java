package org.ramer.admin.service;

import org.ramer.admin.entity.domain.Article;
import org.ramer.admin.entity.pojo.ArticlePoJo;
import org.ramer.admin.system.service.BaseService;

/** @author ramer */
public interface ArticleService extends BaseService<Article, ArticlePoJo> {
  String generateNumber();
}
