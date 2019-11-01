package org.ramer.admin.service.impl;

import com.querydsl.jpa.impl.JPAQueryFactory;
import java.text.SimpleDateFormat;
import java.util.*;
import javax.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.ramer.admin.entity.domain.Article;
import org.ramer.admin.entity.domain.QArticle;
import org.ramer.admin.repository.ArticleRepository;
import org.ramer.admin.service.ArticleService;
import org.ramer.admin.system.entity.Constant.State;
import org.ramer.admin.system.exception.CommonException;
import org.ramer.admin.system.repository.BaseRepository;
import org.springframework.stereotype.Service;

/** @author ramer */
@Slf4j
@Service
public class ArticleServiceImpl implements ArticleService {
  @Resource private JPAQueryFactory jpaQueryFactory;
  @Resource private ArticleRepository repository;

  @Override
  public String generateNumber() {
    String maxNumber =
        Optional.ofNullable(
                jpaQueryFactory
                    .select(QArticle.article.number.max())
                    .from(QArticle.article)
                    .where(QArticle.article.state.eq(State.STATE_ON))
                    .fetchOne())
            .map(num -> num + "")
            .orElse("0000000000000");
    final int seq = Integer.parseInt(maxNumber.substring(7)) + 1;
    StringBuilder seqStr = new StringBuilder("" + seq);
    while (seqStr.length() < 3) {
      seqStr.insert(0, "0");
    }
    return new SimpleDateFormat("yyMMdd").format(new Date()).concat(seqStr.toString());
  }

  @SuppressWarnings({"unchecked"})
  @Override
  public <U extends BaseRepository<Article, Long>> U getRepository() throws CommonException {
    return (U) repository;
  }
}
