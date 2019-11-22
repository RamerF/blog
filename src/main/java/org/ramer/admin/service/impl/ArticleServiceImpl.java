package org.ramer.admin.service.impl;

import com.querydsl.jpa.impl.JPAQueryFactory;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Optional;
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
  public Article create(Article article) throws RuntimeException {
//    article.se
    return repository.saveAndFlush(article);
  }

  @Override
  public Long generateNumber() {
    // 18位编号递增: 191122170150(时间) + 000001(递增)
    long maxNumber =
        Optional.ofNullable(
                jpaQueryFactory
                    .select(QArticle.article.number.max())
                    .from(QArticle.article)
                    .where(QArticle.article.state.eq(State.STATE_ON))
                    .fetchOne())
            .map(num -> Long.parseLong((num + "").substring(12)))
            .orElse(0L);
    // 达到最大后重置为0
    if (maxNumber == 99999) {
      maxNumber = 0;
    }
    final long seq = maxNumber + 1;
    StringBuilder seqStr = new StringBuilder("" + seq);
    while (seqStr.length() < 5) {
      seqStr.insert(0, "0");
    }
    return Long.parseLong(
        new SimpleDateFormat("yyMMddHHmmss").format(new Date()).concat(seqStr.toString()));
    // 编号随机
    //    final String timeStr = new SimpleDateFormat("yyMMddHHmmssS").format(new Date());
    //    return timeStr.concat(RandomUtil.random(16 - timeStr.length()));
  }

  @SuppressWarnings({"unchecked"})
  @Override
  public <U extends BaseRepository<Article, Long>> U getRepository() throws CommonException {
    return (U) repository;
  }
}
