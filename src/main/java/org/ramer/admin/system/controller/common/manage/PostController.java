package org.ramer.admin.system.controller.common.manage;

import io.swagger.annotations.*;
import java.util.List;
import java.util.Map;
import javax.annotation.Resource;
import javax.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.ramer.admin.system.entity.Constant.AccessPath;
import org.ramer.admin.system.entity.domain.common.Post;
import org.ramer.admin.system.entity.pojo.common.PostPoJo;
import org.ramer.admin.system.entity.request.common.PostRequest;
import org.ramer.admin.system.entity.response.CommonResponse;
import org.ramer.admin.system.entity.response.common.PostResponse;
import org.ramer.admin.system.service.common.CommonService;
import org.ramer.admin.system.service.common.PostService;
import org.ramer.admin.system.util.TextUtil;
import org.ramer.admin.system.validator.common.PostValidator;
import org.springframework.data.domain.PageImpl;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.*;
import springfox.documentation.annotations.ApiIgnore;

@Slf4j
@Controller("postcm")
@PreAuthorize("hasAnyAuthority('global:read','post:read')")
@RequestMapping(AccessPath.MANAGE + "/post")
@Api(tags = "岗位接口")
@SuppressWarnings("UnusedDeclaration")
public class PostController {
  @Resource private PostService service;
  @Resource private CommonService commonService;
  @Resource private PostValidator validator;

  @InitBinder
  void initBinder(WebDataBinder binder) {
    binder.addValidators(validator);
  }

  @GetMapping("/index")
  @ApiOperation("岗位页面")
  public String index() {
    return "post/index";
  }

  @GetMapping("/page")
  @ResponseBody
  @ApiOperation("获取岗位列表")
  public ResponseEntity<CommonResponse<PageImpl<PostResponse>>> page(
      @ApiParam("页号,从1开始,当page=size=-1时,表示不分页")
          @RequestParam(value = "page", required = false, defaultValue = "1")
          String pageStr,
      @RequestParam(value = "size", required = false, defaultValue = "10") String sizeStr,
      @ApiParam("查询条件") @RequestParam(value = "criteria", required = false) String criteria) {
    final int[] pageAndSize = TextUtil.validFixPageAndSize(pageStr, sizeStr);
    return commonService.page(
        service.page(criteria, pageAndSize[0], pageAndSize[1]), PostResponse::of);
  }

  @GetMapping
  @ApiOperation("添加岗位页面")
  public String create() {
    return "post/edit";
  }

  @PostMapping
  @ResponseBody
  @PreAuthorize("hasAnyAuthority('global:create','post:create')")
  @ApiOperation("添加岗位")
  public ResponseEntity<CommonResponse<Object>> create(
      @Valid PostRequest postRequest, @ApiIgnore BindingResult bindingResult) {
    log.info(" PostController.create : [{}]", postRequest);
    return commonService.create(service, Post.class, postRequest, bindingResult);
  }

  @GetMapping("/{id}")
  @ApiOperation("更新岗位页面")
  public String update(@PathVariable("id") String idStr, @ApiIgnore Map<String, Object> map) {
    return commonService.update(service, PostPoJo.class, idStr, "post/edit", map, "post");
  }

  @PutMapping("/{id}")
  @ResponseBody
  @PreAuthorize("hasAnyAuthority('global:write','post:write')")
  @ApiOperation("更新岗位")
  public ResponseEntity<CommonResponse<Object>> update(
      @PathVariable("id") String idStr,
      @Valid PostRequest postRequest,
      @ApiIgnore BindingResult bindingResult) {
    log.info(" PostController.update : [{}]", postRequest);
    final long id = TextUtil.validLong(idStr, -1);
    if (id < 1) {
      return CommonResponse.wrongFormat("id");
    }
    postRequest.setId(id);
    return commonService.update(service, Post.class, postRequest, idStr, bindingResult);
  }

  @DeleteMapping("/{id}")
  @ResponseBody
  @PreAuthorize("hasAnyAuthority('global:delete','post:delete')")
  @ApiOperation("删除岗位")
  public ResponseEntity<CommonResponse<Object>> delete(@PathVariable("id") String idStr) {
    log.info(" PostController.delete : [{}]", idStr);
    return commonService.delete(service, idStr);
  }

  @DeleteMapping("/deleteBatch")
  @ResponseBody
  @PreAuthorize("hasAnyAuthority('global:delete','post:delete')")
  @ApiOperation("删除岗位批量")
  public ResponseEntity<CommonResponse<Object>> deleteBatch(@RequestParam("ids") List<Long> ids) {
    log.info(" PostController.deleteBatch : [{}]", ids);
    return commonService.deleteBatch(service, ids);
  }
}
