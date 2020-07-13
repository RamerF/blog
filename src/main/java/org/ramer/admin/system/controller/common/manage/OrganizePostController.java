package org.ramer.admin.system.controller.common.manage;

import io.swagger.annotations.*;
import java.util.List;
import java.util.Map;
import javax.annotation.Resource;
import javax.servlet.http.HttpSession;
import javax.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.ramer.admin.system.entity.Constant.AccessPath;
import org.ramer.admin.system.entity.domain.common.Post;
import org.ramer.admin.system.entity.domain.common.Post.DataAccess;
import org.ramer.admin.system.entity.pojo.common.PostPoJo;
import org.ramer.admin.system.entity.request.common.PostRequest;
import org.ramer.admin.system.entity.response.Rs;
import org.ramer.admin.system.entity.response.common.PostResponse;
import org.ramer.admin.system.exception.CommonException;
import org.ramer.admin.system.service.common.*;
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
@PreAuthorize("hasAnyAuthority('global:read','manage:read','post:read')")
@RequestMapping(AccessPath.MANAGE + "/post")
@Api(tags = "管理端: 岗位接口")
@SuppressWarnings("UnusedDeclaration")
public class OrganizePostController {
  @Resource private OrganizeService organizeService;
  @Resource private PostService service;
  @Resource private CommonService commonService;
  @Resource private PostValidator validator;

  @InitBinder("postRequest")
  void initBinder(WebDataBinder binder) {
    binder.addValidators(validator);
  }

  @GetMapping("/index")
  @ApiOperation("岗位页面")
  public String index(@ApiIgnore HttpSession session, @ApiIgnore Map<String, Object> map) {
    map.put("organizes", organizeService.list(null));
    commonService.writeMenuAndSiteInfo(session, map);
    return "manage/organize/post/index";
  }

  @GetMapping("/page")
  @ResponseBody
  @ApiOperation("获取岗位列表")
  public ResponseEntity<Rs<PageImpl<PostResponse>>> page(
      @ApiParam("组织id") @RequestParam(value = "organizeId", required = false) String organizeIdStr,
      @ApiParam("页号,从1开始,当page=size=-1时,表示不分页")
          @RequestParam(value = "page", required = false, defaultValue = "1")
          String pageStr,
      @RequestParam(value = "size", required = false, defaultValue = "10") String sizeStr,
      @ApiParam("查询条件") @RequestParam(value = "criteria", required = false) String criteria) {
    final long organizeId = TextUtil.validLong(organizeIdStr, -1);
    if (TextUtil.nonValidId(organizeId)) {
      return Rs.canNotBlank("组织");
    }
    final int[] pageAndSize = TextUtil.validFixPageAndSize(pageStr, sizeStr);
    return commonService.page(
        service.page(organizeId, criteria, pageAndSize[0], pageAndSize[1]), PostResponse::of);
  }

  @GetMapping
  @ApiOperation("添加岗位页面")
  public String create(
      @ApiParam("组织id") @RequestParam(value = "organizeId", required = false) String organizeIdStr,
      @ApiIgnore HttpSession session,
      @ApiIgnore Map<String, Object> map) {
    final long organizeId = TextUtil.validLong(organizeIdStr, -1);
    if (TextUtil.nonValidId(organizeId)) {
      throw CommonException.of("参数值无效 [组织]");
    }
    map.put("organize", organizeService.getById(organizeId));
    map.put("dataAccesses", DataAccess.map());
    //    return "manage/organize/post/edit";
    return "manage/organize/post/edit_pure::main-container";
  }

  @PostMapping
  @ResponseBody
  @PreAuthorize("hasAnyAuthority('global:create','post:create')")
  @ApiOperation("添加岗位")
  public ResponseEntity<Rs<Object>> create(
      @Valid PostRequest postRequest, @ApiIgnore BindingResult bindingResult) {
    log.info(" PostController.create : [{}]", postRequest);
    return commonService.create(service, Post.class, postRequest, bindingResult);
  }

  @GetMapping("/{id}")
  @ApiOperation("更新岗位页面")
  public String update(
      @PathVariable("id") String idStr,
      @ApiIgnore HttpSession session,
      @ApiIgnore Map<String, Object> map) {
    return commonService.update(
        service,
        PostPoJo.class,
        idStr,
        "manage/organize/post/edit_pure::main-container",
        map,
        "post",
        id -> {
          final Post post = service.getById(id);
          map.put("post", post);
          map.put("organize", post.getOrganize());
          map.put("dataAccesses", DataAccess.map());
          commonService.writeMenuAndSiteInfo(session, map);
        },
        false);
  }

  @PutMapping("/{id}")
  @ResponseBody
  @PreAuthorize("hasAnyAuthority('global:write','post:write')")
  @ApiOperation("更新岗位")
  public ResponseEntity<Rs<Object>> update(
      @PathVariable("id") String idStr,
      @Valid PostRequest postRequest,
      @ApiIgnore BindingResult bindingResult) {
    log.info(" PostController.update : [{}]", postRequest);
    final long id = TextUtil.validLong(idStr, -1);
    if (id < 1) {
      return Rs.wrongFormat("id");
    }
    postRequest.setId(id);
    return commonService.update(service, Post.class, postRequest, idStr, bindingResult);
  }

  @DeleteMapping("/{id}")
  @ResponseBody
  @PreAuthorize("hasAnyAuthority('global:delete','post:delete')")
  @ApiOperation("删除岗位")
  public ResponseEntity<Rs<Object>> delete(@PathVariable("id") String idStr) {
    log.info(" PostController.delete : [{}]", idStr);
    return commonService.delete(service, idStr);
  }

  @DeleteMapping("/deleteBatch")
  @ResponseBody
  @PreAuthorize("hasAnyAuthority('global:delete','post:delete')")
  @ApiOperation("删除岗位批量")
  public ResponseEntity<Rs<Object>> deleteBatch(@RequestParam("ids") List<Long> ids) {
    log.info(" PostController.deleteBatch : [{}]", ids);
    return commonService.deleteBatch(service, ids);
  }
}
