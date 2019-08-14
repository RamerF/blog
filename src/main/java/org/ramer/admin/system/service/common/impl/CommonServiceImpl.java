package org.ramer.admin.system.service.common.impl;

import com.alibaba.fastjson.JSONObject;
import java.lang.reflect.InvocationTargetException;
import java.util.*;
import java.util.function.*;
import java.util.stream.Collectors;
import javax.annotation.Resource;
import javax.servlet.http.HttpSession;
import lombok.extern.slf4j.Slf4j;
import org.ramer.admin.system.entity.Constant.*;
import org.ramer.admin.system.entity.domain.AbstractEntity;
import org.ramer.admin.system.entity.domain.common.Manager;
import org.ramer.admin.system.entity.pojo.AbstractEntityPoJo;
import org.ramer.admin.system.entity.request.AbstractEntityRequest;
import org.ramer.admin.system.entity.response.CommonResponse;
import org.ramer.admin.system.entity.response.common.MenuResponse;
import org.ramer.admin.system.exception.CommonException;
import org.ramer.admin.system.service.BaseService;
import org.ramer.admin.system.service.common.*;
import org.ramer.admin.system.util.CollectionUtils;
import org.ramer.admin.system.util.TextUtil;
import org.springframework.beans.BeanUtils;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import springfox.documentation.annotations.ApiIgnore;

/** @author ramer */
@Component("manageCommon")
@Slf4j
public class CommonServiceImpl implements CommonService {
  @Resource private MenuService menuService;
  @Resource private ConfigService configService;

  @Override
  public void writeMenuAndSiteInfo(@ApiIgnore HttpSession session, Map<String, Object> map) {
    final Manager manager = (Manager) session.getAttribute(SessionKey.LOGIN_MANAGER);
    final Long managerId = manager.getId();
    // 所有可用菜单
    final List<MenuResponse> menuList =
        CollectionUtils.list(menuService.listByManager(managerId), MenuResponse::of, null, null);
    // 可用菜单的树形结构
    final List<MenuResponse> menus = Objects.requireNonNull(menuList, "菜单为空");
    final List<MenuResponse> responses =
        menus.stream()
            .filter(response -> Objects.isNull(response.getParentId()))
            .collect(Collectors.toList());
    menus.removeAll(responses);
    Stack<MenuResponse> retain = new Stack<>();
    responses.forEach(retain::push);
    while (retain.size() > 0 && menus.size() > 0) {
      MenuResponse menu = retain.pop();
      // 当前节点的子节点
      List<MenuResponse> children =
          menus.stream()
              .filter(menuResponse -> menuResponse.getParentId().equals(menu.getId()))
              .collect(Collectors.toList());
      // 子节点具有叶子节点,入栈
      children.stream().filter(menuResponse -> !menuResponse.getHasChild()).forEach(retain::push);
      menu.setChildren(children);
      menus.removeAll(children);
    }
    map.put("menus", responses);
    JSONObject siteJson = new JSONObject();
    siteJson.put("title", configService.getSiteInfo(ConfigCode.SITE_TITLE));
    siteJson.put("name", configService.getSiteInfo(ConfigCode.SITE_NAME));
    siteJson.put("copyright", configService.getSiteInfo(ConfigCode.SITE_COPYRIGHT));
    map.put("site", siteJson);
  }

  @Override
  public <S extends BaseService<T, E>, T extends AbstractEntity, E extends AbstractEntityPoJo>
      ResponseEntity create(S invoke, T entity, BindingResult bindingResult) {
    if (bindingResult.hasErrors()) {
      return CommonResponse.fail(collectBindingResult(bindingResult));
    }
    try {
      entity = invoke.create(entity);
      return entity == null
          ? CommonResponse.fail(Txt.FAIL_EXEC_ADD_EXIST)
          : entity.getId() > 0
              ? CommonResponse.ok(entity.getId())
              : CommonResponse.fail(Txt.FAIL_EXEC_ADD);
    } catch (Exception e) {
      log.warn(" CommonServiceImpl.create : [{}]", e.getMessage());
      return CommonResponse.fail(
          !StringUtils.isEmpty(e.getMessage())
                  && (e instanceof CommonException || e instanceof NullPointerException)
              ? e.getMessage()
              : Txt.FAIL_EXEC);
    }
  }

  @Override
  public <S extends BaseService<T, E>, T extends AbstractEntity, E extends AbstractEntityPoJo>
      String update(
          S invoke,
          Class<E> clazz,
          final String idStr,
          final String page,
          Map<String, Object> map,
          String propName) {
    return update(invoke, clazz, idStr, page, map, propName, null, true);
  }

  @Override
  public <S extends BaseService<T, E>, T extends AbstractEntity, E extends AbstractEntityPoJo>
      String update(
          S invoke,
          Class<E> clazz,
          final String idStr,
          final String page,
          Map<String, Object> map,
          String propName,
          Consumer<Long> consumer) {
    return update(invoke, clazz, idStr, page, map, propName, consumer, true);
  }

  @Override
  public <S extends BaseService<T, E>, T extends AbstractEntity, E extends AbstractEntityPoJo>
      String update(
          S invoke,
          Class<E> clazz,
          final String idStr,
          final String page,
          Map<String, Object> map,
          String propName,
          Consumer<Long> consumer,
          boolean mapPoJo) {
    final long id = TextUtil.validLong(idStr, 0);
    if (id < 1) {
      throw new CommonException("id 格式不正确");
    }
    if (Objects.nonNull(consumer)) {
      consumer.accept(id);
    }
    if (mapPoJo) {
      map.put(propName, invoke.getPoJoById(id, clazz));
    }
    return page;
  }

  @Override
  public <S extends BaseService<T, E>, T extends AbstractEntity, E extends AbstractEntityPoJo>
      ResponseEntity<CommonResponse<Object>> update(
          S invoke, T entity, String idStr, BindingResult bindingResult) {
    final long id = TextUtil.validLong(idStr, 0);
    if (id < 1) {
      return CommonResponse.wrongFormat("id");
    }
    if (bindingResult.hasErrors()) {
      return CommonResponse.fail(collectBindingResult(bindingResult));
    }
    entity.setId(id);
    try {
      entity = invoke.update(entity);
      return entity == null
          ? CommonResponse.fail(Txt.FAIL_EXEC_UPDATE_NOT_EXIST)
          : entity.getId() > 0
              ? CommonResponse.ok(entity.getId(), Txt.SUCCESS_EXEC_UPDATE)
              : CommonResponse.fail(Txt.FAIL_EXEC_UPDATE);
    } catch (Exception e) {
      log.warn(" CommonServiceImpl.update : [{}]", e.getMessage());
      return CommonResponse.fail(
          !StringUtils.isEmpty(e.getMessage())
                  && (e instanceof CommonException || e instanceof NullPointerException)
              ? e.getMessage()
              : Txt.FAIL_EXEC);
    }
  }

  @Override
  public <
          S extends BaseService<T, E>,
          T extends AbstractEntity,
          E extends AbstractEntityPoJo,
          R extends AbstractEntityRequest>
      ResponseEntity<CommonResponse<Object>> create(
          final S invoke,
          Class<T> clazz,
          final R entity,
          final BindingResult bindingResult,
          String... includeNullProperties) {
    return createOrUpdate(invoke, clazz, entity, bindingResult, true);
  }

  @Override
  public <
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
          String... includeNullProperties) {
    final long id = TextUtil.validLong(idStr, -1);
    if (id < 1) {
      return CommonResponse.wrongFormat("id");
    }
    try {
      Objects.requireNonNull(BeanUtils.findDeclaredMethod(entity.getClass(), "setId", Long.class))
          .invoke(entity, id);
    } catch (IllegalAccessException | InvocationTargetException e) {
      log.warn(" CommonServiceImpl.update : [{}]", e.getMessage());
      return CommonResponse.fail(Txt.ERROR_SYSTEM);
    }
    return createOrUpdate(invoke, clazz, entity, bindingResult, false, includeNullProperties);
  }

  @Override
  public <S extends BaseService<T, E>, T extends AbstractEntity, E extends AbstractEntityPoJo>
      ResponseEntity<CommonResponse<Object>> delete(final S invoke, final String idStr) {
    long id = TextUtil.validLong(idStr, 0);
    if (id < 1) {
      return CommonResponse.wrongFormat("id");
    }
    try {
      invoke.delete(id);
    } catch (Exception e) {
      log.warn(" CommonServiceImpl.delete : [{}]", e.getMessage());
      return CommonResponse.fail(
          StringUtils.isEmpty(e.getMessage()) ? Txt.FAIL_EXEC : e.getMessage());
    }
    return CommonResponse.ok(null, Txt.SUCCESS_EXEC_DELETE);
  }

  @Override
  public <S extends BaseService<T, E>, T extends AbstractEntity, E extends AbstractEntityPoJo>
      ResponseEntity<CommonResponse<Object>> deleteBatch(final S invoke, final List<Long> ids) {
    try {
      invoke.deleteBatch(ids);
    } catch (Exception e) {
      log.warn(" CommonServiceImpl.deleteBatch : [{}]", e.getMessage());
      return CommonResponse.fail(
          StringUtils.isEmpty(e.getMessage()) ? Txt.FAIL_EXEC : e.getMessage());
    }
    return CommonResponse.ok(null, Txt.SUCCESS_EXEC_DELETE);
  }

  @Override
  public <T extends AbstractEntity, E> ResponseEntity<CommonResponse<List<E>>> list(
      final List<T> lists, final Function<T, E> function, final Predicate<E> filterFunction) {
    return CommonResponse.ok(
        Objects.isNull(filterFunction)
            ? lists.stream().map(function).collect(Collectors.toList())
            : lists.stream().map(function).filter(filterFunction).collect(Collectors.toList()));
  }

  @Override
  public <T extends AbstractEntity, R> ResponseEntity<CommonResponse<PageImpl<R>>> page(
      final Page<T> page, final Function<T, R> function) {
    return CommonResponse.ok(
        new PageImpl<>(
            page.getContent().stream().map(function).collect(Collectors.toList()),
            page.getPageable(),
            page.getTotalElements()));
  }
  //
  //  @Override
  //  public <T extends AbstractEntity, R> ResponseEntity<CommonResponse<R>> page(
  //      final Page<T> page, final Function<T, R> function) {
  //    return null;
  //  }

  @Override
  public String collectBindingResult(BindingResult bindingResult) {
    StringBuilder errorMsg = new StringBuilder("提交信息有误: ");
    bindingResult
        .getAllErrors()
        .forEach(
            error ->
                errorMsg
                    .append("<br/>")
                    .append(
                        Objects.requireNonNull(error.getDefaultMessage())
                                .contains("Failed to convert property")
                            ? ((FieldError) error).getField() + " 格式不正确"
                            : error.getDefaultMessage()));
    return errorMsg.toString();
  }
  /**
   * 创建或更新.
   *
   * @param invoke 服务层实现类.
   * @param entity 要更新的request {@link AbstractEntityRequest} 对象.
   * @param create 是否是创建.
   * @param bindingResult 校验器校验结果.
   * @param <T> 服务层实现类.
   * @param <E> 要更新的对象.
   * @return {@link ResponseEntity}
   */
  private <
          S extends BaseService<T, E>,
          T extends AbstractEntity,
          E extends AbstractEntityPoJo,
          R extends AbstractEntityRequest>
      ResponseEntity<CommonResponse<Object>> createOrUpdate(
          final S invoke,
          Class<T> clazz,
          final R entity,
          final BindingResult bindingResult,
          boolean create,
          String... includeNullProperties) {
    if (bindingResult.hasErrors()) {
      return CommonResponse.fail(collectBindingResult(bindingResult));
    }
    try {
      if (create) {
        Objects.requireNonNull(BeanUtils.findDeclaredMethod(entity.getClass(), "setId", Long.class))
            .invoke(entity, (Long) null);
      }
      final T obj = invoke.createOrUpdate(clazz, entity, includeNullProperties);
      return Objects.isNull(obj)
          ? CommonResponse.fail(create ? Txt.FAIL_EXEC_ADD : Txt.FAIL_EXEC_UPDATE_NOT_EXIST)
          : CommonResponse.ok(obj.getId(), create ? Txt.SUCCESS_EXEC_ADD : Txt.SUCCESS_EXEC_UPDATE);
    } catch (Exception e) {
      log.warn(
          " CommonServiceImpl.update : [{}]",
          e instanceof CommonException || e instanceof NullPointerException
              ? e.getMessage()
              : Txt.ERROR_SYSTEM);
      log.error(e.getMessage(), e);
      return CommonResponse.fail(
          !StringUtils.isEmpty(e.getMessage())
                  && (e instanceof CommonException || e instanceof NullPointerException)
              ? e.getMessage()
              : Txt.FAIL_EXEC);
    }
  }
}
