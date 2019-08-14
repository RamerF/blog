package org.ramer.admin.system.service.common;

import java.util.List;
import java.util.Map;
import java.util.function.*;
import javax.servlet.http.HttpSession;
import org.ramer.admin.system.entity.domain.AbstractEntity;
import org.ramer.admin.system.entity.pojo.AbstractEntityPoJo;
import org.ramer.admin.system.entity.request.AbstractEntityRequest;
import org.ramer.admin.system.entity.response.CommonResponse;
import org.ramer.admin.system.service.BaseService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import springfox.documentation.annotations.ApiIgnore;

/**
 * 通用业务方法.
 *
 * @author ramer
 */
public interface CommonService {
  void writeMenuAndSiteInfo(@ApiIgnore HttpSession session, Map<String, Object> map);

  /**
   * @param invoke 服务层实现类.
   * @param entity 要保存的对象.
   * @param bindingResult 校验器校验结果.
   * @param <S> 服务层实现类service {@link BaseService}.
   * @param <T> Domain对象 {@link AbstractEntity}.
   * @param <E> PoJo对象 {@link AbstractEntityPoJo}.
   * @return {@link ResponseEntity}
   */
  <S extends BaseService<T, E>, T extends AbstractEntity, E extends AbstractEntityPoJo>
      ResponseEntity<CommonResponse<Object>> create(
          S invoke, T entity, BindingResult bindingResult);

  /**
   * 跳转到更新页面.校验更新url正确性,写入POJO对象用于回显.
   *
   * @param invoke 服务层实现类.
   * @param clazz 当前写入对象的Class.
   * @param idStr 页面传递的id.
   * @param page 页面路径.
   * @param map 用于写入数据到request.
   * @param propName 写入的属性名
   * @return 返回更新页面
   */
  <S extends BaseService<T, E>, T extends AbstractEntity, E extends AbstractEntityPoJo>
      String update(
          S invoke,
          Class<E> clazz,
          String idStr,
          String page,
          Map<String, Object> map,
          String propName);

  /**
   * 跳转到更新页面.校验更新url正确性,写入POJO对象用于回显.
   *
   * @param invoke 服务层实现类.
   * @param clazz 当前写入对象的Class.
   * @param idStr 页面传递的id.
   * @param page 页面路径.
   * @param map 用于写入数据到request.
   * @param propName 写入的属性名
   * @param consumer 自定义操作,如果该值不为空,将不会写入POJO对象,通常用于写入额外的信息或写入domain对象
   * @return 返回更新页面
   */
  <S extends BaseService<T, E>, T extends AbstractEntity, E extends AbstractEntityPoJo>
      String update(
          S invoke,
          Class<E> clazz,
          String idStr,
          String page,
          Map<String, Object> map,
          String propName,
          Consumer<Long> consumer);
  /**
   * 跳转到更新页面.校验更新url正确性,写入POJO对象用于回显.
   *
   * @param invoke 服务层实现类.
   * @param clazz 当前写入对象的Class.
   * @param idStr 页面传递的id.
   * @param page 页面路径.
   * @param map 用于写入数据到request.
   * @param propName 写入的属性名
   * @param consumer 自定义操作,如果该值不为空,将不会写入POJO对象,通常用于写入额外的信息或写入domain对象
   * @param mapPoJo 是否写入pojo对象到map中
   * @return 返回更新页面
   */
  <S extends BaseService<T, E>, T extends AbstractEntity, E extends AbstractEntityPoJo>
      String update(
          S invoke,
          Class<E> clazz,
          String idStr,
          String page,
          Map<String, Object> map,
          String propName,
          Consumer<Long> consumer,
          boolean mapPoJo);

  /**
   * 更新.
   *
   * @param invoke 服务层实现类.
   * @param entity 要更新的 {@link AbstractEntity} 对象.
   * @param idStr 页面传递的id.
   * @param bindingResult 校验器校验结果.
   * @param <T> 服务层实现类.
   * @param <E> 要更新的对象.
   * @return {@link ResponseEntity}
   */
  <S extends BaseService<T, E>, T extends AbstractEntity, E extends AbstractEntityPoJo>
      ResponseEntity<CommonResponse<Object>> update(
          S invoke, T entity, String idStr, BindingResult bindingResult);

  /**
   * 创建.
   *
   * @param invoke 服务层实现类.
   * @param entity 要更新的request {@link AbstractEntityRequest} 对象.
   * @param bindingResult 校验器校验结果.
   * @param <T> 服务层实现类.
   * @param <E> 要更新的对象.
   * @return {@link ResponseEntity}
   */
  <
          S extends BaseService<T, E>,
          T extends AbstractEntity,
          E extends AbstractEntityPoJo,
          R extends AbstractEntityRequest>
      ResponseEntity<CommonResponse<Object>> create(
          final S invoke,
          Class<T> clazz,
          final R entity,
          final BindingResult bindingResult,
          String... includeNullProperties);

  /**
   * 更新.
   *
   * @param invoke 服务层实现类.
   * @param entity 要更新的 request {@link AbstractEntityRequest} 对象.
   * @param idStr 路径上的 id
   * @param bindingResult 校验器校验结果.
   * @param <T> 服务层实现类.
   * @param <E> 要更新的对象.
   * @return {@link ResponseEntity}
   */
  <
          S extends BaseService<T, E>,
          T extends AbstractEntity,
          E extends AbstractEntityPoJo,
          R extends AbstractEntityRequest>
      ResponseEntity<CommonResponse<Object>> update(
          final S invoke,
          Class<T> clazz,
          final R entity,
          String idStr,
          final BindingResult bindingResult,
          String... includeNullProperties);

  /**
   * 逻辑删除.
   *
   * @param <T> 服务层实现类.
   * @param <E> 要删除的对象.
   * @param invoke 服务层实现类.
   * @param idStr 待删除对象id.
   * @return {@link ResponseEntity}
   */
  <S extends BaseService<T, E>, T extends AbstractEntity, E extends AbstractEntityPoJo>
      ResponseEntity<CommonResponse<Object>> delete(S invoke, String idStr);

  /**
   * 逻辑删除批量.
   *
   * @param <T> 服务层实现类.
   * @param <E> 要删除的对象.
   * @param invoke 服务层实现类.
   * @param ids 待删除对象id集合.
   * @return {@link ResponseEntity}
   */
  <S extends BaseService<T, E>, T extends AbstractEntity, E extends AbstractEntityPoJo>
      ResponseEntity<CommonResponse<Object>> deleteBatch(S invoke, List<Long> ids);

  /**
   * 转换集合对象.将List domain对象转换为 List 任意对象,并封装为页面响应对象.
   *
   * @param lists List domain对象
   * @param function 转换函数表达式
   * @param filterFunction 过滤函数表达式
   * @param <T> domain对象
   * @param <E> 任意对象(通常是response|poJo对象)
   * @return {@link ResponseEntity}
   */
  <T extends AbstractEntity, E> ResponseEntity<CommonResponse<List<E>>> list(
      List<T> lists, final Function<T, E> function, final Predicate<E> filterFunction);

  /**
   * 转换分页对象.将Page domain对象转换为 Page 任意对象,并封装为页面响应对象.
   *
   * @param page Page domain对象
   * @param function 转换函数表达式
   * @param <T> domain对象
   * @return {@link ResponseEntity}
   */
  <T extends AbstractEntity, R> ResponseEntity<CommonResponse<PageImpl<R>>> page(
      final Page<T> page, final Function<T, R> function);

  /**
   * 拼接表单错误信息.
   *
   * @param bindingResult {@link BindingResult}
   * @return 错误提示
   */
  String collectBindingResult(BindingResult bindingResult);
}
