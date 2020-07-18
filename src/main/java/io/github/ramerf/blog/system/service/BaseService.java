package io.github.ramerf.blog.system.service;

import java.sql.SQLException;
import java.util.*;
import java.util.stream.Stream;
import javax.transaction.Transactional;
import io.github.ramerf.blog.system.entity.Constant;
import io.github.ramerf.blog.system.entity.Constant.Txt;
import io.github.ramerf.blog.system.entity.domain.AbstractEntity;
import io.github.ramerf.blog.system.entity.pojo.AbstractEntityPoJo;
import io.github.ramerf.blog.system.entity.request.AbstractEntityRequest;
import io.github.ramerf.blog.system.exception.CommonException;
import io.github.ramerf.blog.system.repository.BaseRepository;
import org.springframework.beans.BeanUtils;
import org.springframework.data.domain.*;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

/**
 * 通用业务方法.
 *
 * @param <T> the type parameter
 * @param <E> the type parameter
 * @author ramer
 */
public interface BaseService<T extends AbstractEntity, E extends AbstractEntityPoJo> {

  /**
   * Create t.
   *
   * @param t the t
   * @return the t
   * @throws RuntimeException the runtime exception
   */
  @Transactional
  default T create(T t) throws RuntimeException {
    textFilter(t, t);
    return getRepository().saveAndFlush(t);
  }

  /**
   * Create batch list.
   *
   * @param ts the ts
   * @return the list
   * @throws RuntimeException the runtime exception
   */
  @Transactional
  default List<T> createBatch(List<T> ts) throws RuntimeException {
    if (CollectionUtils.isEmpty(ts)) {
      return Collections.emptyList();
    }
    ts.forEach(t -> textFilter(t, t));
    return getRepository().saveAll(ts);
  }

  /**
   * 条件is_delete=false的总记录数
   *
   * @return long long
   */
  default long count() {
    return getRepository().count();
  }

  /**
   * 获取当前对象的POJO对象. @param id the id
   *
   * @param clazz the clazz
   * @return the po jo by id
   */
  default E getPoJoById(final long id, Class<E> clazz) {
    final E instance;
    try {
      instance = clazz.newInstance();
    } catch (Exception e) {
      return null;
    }
    return Optional.ofNullable(getById(id)).map(e -> instance.of(e, clazz)).orElse(null);
  }

  /**
   * Gets by id.
   *
   * @param id the id
   * @return the by id
   */
  default T getById(final long id) {
    return getRepository().findById(id).orElse(null);
  }

  /**
   * List by ids list.
   *
   * @param ids the ids
   * @return the list
   */
  default List<T> listByIds(final List<Long> ids) {
    return getRepository().findAllById(ids);
  }

  /**
   * 条件查询.
   *
   * @param criteria 查询条件
   * @return {@link List <T>}
   */
  default List<T> list(String criteria) {
    return page(criteria, -1, -1).getContent();
  }

  /**
   * 分页条件查询.
   *
   * @param criteria 查询条件
   * @param page 当前页号 当page和size同时为-1时,将不会分页.
   * @param size 每页条目
   * @return {@link Page<T>}
   */
  default Page<T> page(final String criteria, final int page, final int size) {
    final PageRequest pageable = pageRequest(page, size);
    return Objects.isNull(pageable)
        ? new PageImpl<>(Collections.emptyList())
        : getRepository().findAll(getSpec(criteria), pageable);
  }

  /**
   * 保存/更新{@link U}对应的Domain对象.默认不会覆盖{@link U}中为null的字段,包含{@code
   * includeNullProperties}*中的属性,即使值为null.
   *
   * @param <U> Request 实体.
   * @param clazz Domain class.
   * @param u 页面请求对象 {@link AbstractEntityRequest}.
   * @param includeNullProperties 覆写这些属性值,即使值为null.
   * @return T <br>
   *     null,如果保存/更新失败,或者更新时记录不存在.
   * @throws RuntimeException the runtime exception
   * @see SQLException
   */
  default <U extends AbstractEntityRequest> T createOrUpdate(
      Class<T> clazz, U u, String... includeNullProperties) throws RuntimeException {
    T domain;
    Long id;
    try {
      id =
          (Long)
              Objects.requireNonNull(
                      BeanUtils.findDeclaredMethod(u.getClass(), "getId"), "getId方法不存在")
                  .invoke(u);
      domain = Objects.isNull(id) ? null : getById(id);
      if (Objects.nonNull(id) && Objects.isNull(domain)) {
        return null;
      }
      domain = Objects.isNull(domain) ? clazz.newInstance() : domain;
    } catch (Exception e) {
      return null;
    }
    BeanUtils.copyProperties(
        u,
        domain,
        Stream.of(io.github.ramerf.blog.system.util.BeanUtils.getNullPropertyNames(u))
            .filter(prop -> !Arrays.asList(includeNullProperties).contains(prop))
            .toArray(String[]::new));
    u.of(u, domain);
    return Objects.isNull(id) ? create(domain) : update(domain);
  }

  /**
   * Update t.
   *
   * @param t the t
   * @return the t
   * @throws RuntimeException the runtime exception
   */
  @Transactional
  default T update(T t) throws RuntimeException {
    return Optional.ofNullable(getById(t.getId()))
        .map(
            o -> {
              textFilter(t, t);
              return getRepository().saveAndFlush(t);
            })
        .orElse(null);
  }

  /**
   * Delete.
   *
   * @param id the id
   * @throws RuntimeException the runtime exception
   */
  @Transactional
  default void delete(final long id) throws RuntimeException {
    getRepository().deleteById(id);
  }

  /**
   * Delete batch.
   *
   * @param ids the ids
   * @throws RuntimeException the runtime exception
   */
  @Transactional
  default void deleteBatch(final List<Long> ids) throws RuntimeException {
    getRepository().deleteByIds(ids);
  }

  /**
   * 过滤某些属性可能包含的特殊字符.
   *
   * @param trans 页面传递过来的对象
   * @param filtered 过滤后的对象
   */
  default void textFilter(T trans, T filtered) {}

  /**
   * 获取模糊查询条件,子类应该根据需要覆写该方法. @param criteria the criteria
   *
   * @return the spec
   */
  default Specification<T> getSpec(String criteria) {
    return StringUtils.isEmpty(criteria)
        ? (root, query, builder) -> builder.and(builder.equal(root.get("isDelete"), Boolean.FALSE))
        : (root, query, builder) ->
            builder.and(
                builder.equal(root.get("isDelete"), Boolean.FALSE),
                builder.or(builder.like(root.get("name"), "%" + criteria + "%")));
  }

  /**
   * 获取分页对象.
   *
   * @param page 当前页,从1开始
   * @param size 每页大小
   * @return the page request
   */
  default PageRequest pageRequest(final int page, final int size) {
    return pageRequest(page, size, Sort.unsorted());
  }
  /**
   * 获取分页对象,支持排序.
   *
   * @param page 当前页,从1开始
   * @param size 每页大小
   * @param sort 排序规则
   * @return the page request
   */
  default PageRequest pageRequest(final int page, final int size, Sort sort) {
    if ((page < 1 || size < 0) && page != size) {
      return null;
    }
    return page == -1
        ? PageRequest.of(0, Integer.MAX_VALUE, sort)
        : PageRequest.of(page - 1, size > 0 ? size : Constant.DEFAULT_PAGE_SIZE, sort);
  }

  /**
   * Gets repository.
   *
   * @param <U> the type parameter
   * @return the repository
   * @throws RuntimeException the runtime exception
   */
  default <U extends BaseRepository<T, Long>> U getRepository() throws RuntimeException {
    throw CommonException.of(Txt.NOT_IMPLEMENT);
  }
}
