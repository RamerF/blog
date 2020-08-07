package io.github.ramerf.blog.controller.manage;

import io.github.ramerf.blog.entity.pojo.TagPoJo;
import io.github.ramerf.blog.entity.request.TagRequest;
import io.github.ramerf.blog.entity.response.TagResponse;
import io.github.ramerf.blog.service.TagService;
import io.github.ramerf.blog.system.entity.Constant.AccessPath;
import io.github.ramerf.blog.system.exception.CommonException;
import io.github.ramerf.blog.system.service.common.CommonService;
import io.github.ramerf.wind.core.entity.response.Rs;
import io.github.ramerf.wind.core.entity.response.Rs.JsonInstance;
import io.github.ramerf.wind.core.helper.ControllerHelper;
import io.github.ramerf.wind.core.util.CollectionUtils;
import io.github.ramerf.wind.core.util.StringUtils;
import io.swagger.annotations.*;
import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.*;
import javax.annotation.Nonnull;
import javax.annotation.Resource;
import javax.validation.*;
import lombok.Getter;
import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import springfox.documentation.annotations.ApiIgnore;

import static java.util.stream.Collectors.toList;

/**
 * The type Tag controller.
 *
 * @author Tang xiaofeng
 */
@Slf4j
@Controller("manage_tag_controller")
@PreAuthorize("hasAnyAuthority('global:read','tag:read')")
@RequestMapping(AccessPath.MANAGE + "/tag")
@Api(tags = "文章标签接口")
@SuppressWarnings({"UnusedDeclaration", "unchecked"})
public class TagController {
  @Resource private Validator commonValidator;
  @Resource private TagService service;
  @Resource private CommonService commonService;

  /**
   * Index string.
   *
   * @return the string
   */
  @GetMapping("/index")
  @ApiOperation("文章标签页面")
  public String index() {
    return "tag/index";
  }

  /**
   * Page response entity.
   *
   * @param page the page
   * @param size the size
   * @param criteria the criteria
   * @return the response entity
   */
  @GetMapping("/page")
  @ResponseBody
  @ApiOperation("获取文章标签列表")
  public ResponseEntity<Rs<Page<TagResponse>>> page(
      @ApiParam("页号,从1开始,当page=size=-1时,表示不分页")
          @RequestParam(value = "page", required = false, defaultValue = "1")
          int page,
      @RequestParam(value = "size", required = false, defaultValue = "10") int size,
      @ApiParam("查询条件") @RequestParam(value = "criteria", required = false) String criteria) {
    return ControllerHelper.page(
        service.page(
            condition ->
                condition.like(StringUtils.nonEmpty(criteria), TagPoJo::setName, criteria + "%"),
            page,
            size,
            null),
        TagResponse::of);
  }

  /**
   * Create string.
   *
   * @return the string
   */
  @GetMapping
  @ApiOperation("添加文章标签页面")
  public String create() {
    return "/manage/tag/edit";
  }

  /**
   * Create response entity.
   *
   * @param tagRequest the tag request
   * @param bindingResult the binding result
   * @return the response entity
   */
  @PostMapping
  @ResponseBody
  @PreAuthorize("hasAnyAuthority('global:create','tag:create')")
  @ApiOperation("添加文章标签")
  public ResponseEntity<Rs<Object>> create(
      @Valid TagRequest tagRequest, @ApiIgnore BindingResult bindingResult) {
    log.info("create:[{}]", tagRequest);
    return ControllerHelper.create(service, tagRequest, bindingResult);
  }

  /**
   * Create response entity.
   *
   * @param tagRequest the tag request
   * @param bindingResult the binding result
   * @return the response entity
   */
  @PostMapping("/batch")
  @ResponseBody
  @PreAuthorize("hasAnyAuthority('global:create','tag:create')")
  @ApiOperation("批量添加文章标签")
  public ResponseEntity<Rs<Object>> createBatch(
      @RequestBody List<TagRequest> tagRequest, @ApiIgnore BindingResult bindingResult) {
    if (CollectionUtils.isEmpty(tagRequest)) {
      return Rs.canNotBlank("标签");
    }
    final ViolationResult violationResult = validate(tagRequest);
    if (violationResult.hasErrors()) {
      return Rs.fail(collectViolationResult(violationResult));
    }
    return Rs.ok(
        tagRequest.stream()
            .map(
                o -> {
                  final ResponseEntity<Rs<Object>> entity =
                      ControllerHelper.create(service, o, bindingResult);
                  return ((JsonInstance) entity.getBody().getData()).get("id");
                })
            .map(Long.class::cast)
            .collect(toList()));
  }

  private <T> ViolationResult validate(final List<T> ts) {
    if (ts.size() < 1) {
      return null;
    }
    final ViolationResult violationResult = new ViolationResult();
    for (T t : ts) {
      violationResult.addError(validate(t).getViolationErrors());
    }
    return violationResult;
  }

  private String collectViolationResult(ViolationResult result) {
    return "提交信息有误:\n"
        + result.stream().map(ViolationErrors::getMessage).collect(Collectors.joining("\n"));
  }

  private <T> ViolationResult validate(T t) {
    final Set<ConstraintViolation<T>> violations = commonValidator.validate(t);
    ViolationResult result = new ViolationResult();
    violations.forEach(
        violation -> {
          final String path = violation.getPropertyPath().toString();
          final String message = violation.getMessage();
          result.addError(ViolationErrors.of(path, message));
        });
    return result;
  }

  public static class ViolationResult implements Iterable<ViolationErrors> {
    private final List<ViolationErrors> violationErrors = new ArrayList<>();

    public boolean hasErrors() {
      return violationErrors.size() > 0;
    }

    public List<ViolationErrors> getViolationErrors() {
      return violationErrors;
    }

    public void addError(@NonNull ViolationErrors error) {
      violationErrors.add(error);
    }

    public void addError(@NonNull List<ViolationErrors> errors) {
      violationErrors.addAll(errors);
    }

    @Override
    @Nonnull
    public Iterator<ViolationErrors> iterator() {
      return new ViolationResultIterator();
    }

    public Stream<ViolationErrors> stream() {
      return StreamSupport.stream(spliterator(), false);
    }

    class ViolationResultIterator implements Iterator<ViolationErrors> {
      private final AtomicInteger index = new AtomicInteger();

      @Override
      public final boolean hasNext() {
        return index.get() >= violationErrors.size() - 1;
      }

      @Override
      public ViolationErrors next() {
        return violationErrors.get(index.getAndIncrement());
      }
    }
  }

  @Getter
  public static class ViolationErrors {
    private String property;
    private String message;

    private ViolationErrors() {}

    public static ViolationErrors of(String property, String message) {
      ViolationErrors error = new ViolationErrors();
      error.property = property;
      error.message = message;
      return error;
    }
  }

  /**
   * Update string.
   *
   * @param id the id
   * @param map the map
   * @return the string
   */
  @GetMapping("/{id}")
  @ApiOperation("更新文章标签页面")
  public String update(@PathVariable("id") long id, @ApiIgnore Map<String, Object> map) {
    if (id < 1) {
      throw CommonException.of("id 格式不正确");
    }
    map.put("tag", service.getById(id));
    return "/manage/tag/edit";
  }

  /**
   * Update response entity.
   *
   * @param id the id
   * @param tagRequest the tag request
   * @param bindingResult the binding result
   * @return the response entity
   */
  @PutMapping("/{id}")
  @ResponseBody
  @PreAuthorize("hasAnyAuthority('global:write','tag:write')")
  @ApiOperation("更新文章标签")
  public ResponseEntity<Rs<Object>> update(
      @PathVariable("id") long id,
      @Valid TagRequest tagRequest,
      @ApiIgnore BindingResult bindingResult) {
    log.info("update:[{}]", tagRequest);
    return ControllerHelper.update(service, tagRequest, id, bindingResult);
  }

  /**
   * Delete response entity.
   *
   * @param id the id
   * @return the response entity
   */
  @DeleteMapping("/{id}")
  @ResponseBody
  @PreAuthorize("hasAnyAuthority('global:delete','tag:delete')")
  @ApiOperation("删除文章标签")
  public ResponseEntity<Rs<Object>> delete(@PathVariable("id") long id) {
    return ControllerHelper.delete(service, id);
  }

  /**
   * Delete by ids response entity.
   *
   * @param ids the ids
   * @return the response entity
   */
  @DeleteMapping("/deleteBatch")
  @ResponseBody
  @PreAuthorize("hasAnyAuthority('global:delete','tag:delete')")
  @ApiOperation("删除文章标签批量")
  public ResponseEntity<Rs<String>> deleteByIds(@RequestParam("ids") List<Long> ids) {
    return ControllerHelper.deleteByIds(service, ids);
  }
}
