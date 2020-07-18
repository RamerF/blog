package io.github.ramerf.blog.system.controller.common.manage;

import io.github.ramerf.wind.core.entity.response.Rs;
import io.swagger.annotations.*;
import java.util.Collections;
import java.util.Map;
import javax.annotation.Resource;
import javax.servlet.http.HttpSession;
import javax.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import io.github.ramerf.blog.system.entity.Constant.AccessPath;
import io.github.ramerf.blog.system.entity.domain.common.Manager;
import io.github.ramerf.blog.system.entity.pojo.common.ManagerPoJo;
import io.github.ramerf.blog.system.entity.request.common.OrganizeMemberRequest;
import io.github.ramerf.blog.system.entity.response.common.ManagerResponse;
import io.github.ramerf.blog.system.entity.response.common.OrganizeMemberResponse;
import io.github.ramerf.blog.system.exception.CommonException;
import io.github.ramerf.blog.system.service.common.*;
import io.github.ramerf.blog.system.util.TextUtil;
import io.github.ramerf.blog.system.validator.common.OrganizeMemberValidator;
import org.springframework.data.domain.PageImpl;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.*;
import springfox.documentation.annotations.ApiIgnore;

@Slf4j
@Controller("organizemcm")
@PreAuthorize("hasAnyAuthority('global:read','manage:read','organize:read')")
@RequestMapping(AccessPath.MANAGE + "/organize/member")
@Api(tags = "管理端: 组织成员接口")
@SuppressWarnings("UnusedDeclaration")
public class OrganizeMemberController {
  @Resource private PostService postService;
  @Resource private ManagerService service;
  @Resource private CommonService commonService;
  @Resource private OrganizeService organizeService;
  @Resource private OrganizeMemberValidator validator;

  @InitBinder("organizeMemberRequest")
  void initBinder(WebDataBinder binder) {
    binder.addValidators(validator);
  }

  @GetMapping("/index")
  @ApiOperation("组织成员页面")
  public String index(@ApiIgnore HttpSession session, @ApiIgnore Map<String, Object> map) {
    map.put("organizes", organizeService.list(null));
    commonService.writeMenuAndSiteInfo(session, map);
    return "manage/organize/member/index";
  }

  @GetMapping("/page")
  @ResponseBody
  @ApiOperation("获取组织成员列表")
  public ResponseEntity<Rs<PageImpl<OrganizeMemberResponse>>> page(
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
        service.pageByOrganize(organizeId, criteria, pageAndSize[0], pageAndSize[1]),
        OrganizeMemberResponse::of);
  }

  @GetMapping
  @ApiOperation("添加组织人员页面")
  public String create(
      @ApiParam("组织id") @RequestParam(value = "organizeId", required = false) String organizeIdStr,
      @ApiIgnore HttpSession session,
      @ApiIgnore Map<String, Object> map) {
    final long organizeId = TextUtil.validLong(organizeIdStr, -1);
    if (TextUtil.nonValidId(organizeId)) {
      throw CommonException.of("参数值无效 [组织]");
    }
    map.put("organize", organizeService.getById(organizeId));
    //    map.put("posts", postService.page(organizeId, null, -1, -1));
    //    return "manage/organize/member/add";
    return "manage/organize/member/edit_pure::main-container";
  }

  @PostMapping
  @ResponseBody
  @PreAuthorize("hasAnyAuthority('global:create','organize:create')")
  @ApiOperation("添加组织人员")
  public ResponseEntity<Rs<String>> create(
      @Valid OrganizeMemberRequest organizeMemberRequest, @ApiIgnore BindingResult bindingResult) {
    log.info(" OrganizeMemberController.create : [{}]", organizeMemberRequest);
    return service.updatePost(
            organizeMemberRequest.getMemberIds(),
            organizeMemberRequest.getOrganizeId(),
            organizeMemberRequest.getPostId())
        ? Rs.ok()
        : Rs.fail("成员不存在");
  }

  @GetMapping("/{id}")
  @ApiOperation("更新组织人员页面")
  public String update(
      @PathVariable("id") String idStr,
      @ApiIgnore HttpSession session,
      @ApiIgnore Map<String, Object> map) {
    return commonService.update(
        service,
        ManagerPoJo.class,
        idStr,
        "manage/organize/member/edit_pure::main-container",
        map,
        "member",
        id -> {
          final Manager manager = service.getById(id);
          map.put("member", manager);
          map.put("organize", manager.getOrganize());
        },
        false);
  }

  @GetMapping("/pageDetach")
  @ResponseBody
  @ApiOperation("获取未分配岗位的成员(管理员)列表")
  public ResponseEntity<Rs<PageImpl<ManagerResponse>>> pageDetach(
      @ApiParam("页号,从1开始,当page=size=-1时,表示不分页")
          @RequestParam(value = "page", required = false, defaultValue = "1")
          String pageStr,
      @RequestParam(value = "size", required = false, defaultValue = "10") String sizeStr,
      @ApiParam("查询条件") @RequestParam(value = "criteria", required = false) String criteria) {
    final int[] pageAndSize = TextUtil.validFixPageAndSize(pageStr, sizeStr);
    return commonService.page(
        service.pageDetach(criteria, pageAndSize[0], pageAndSize[1]), ManagerResponse::of);
  }

  @PutMapping("/{memberId}")
  @ResponseBody
  @PreAuthorize("hasAnyAuthority('global:create','organize:write')")
  @ApiOperation("更新岗位成员")
  public ResponseEntity<Rs<String>> update(
      @PathVariable("memberId") final String memberIdStr,
      @Valid OrganizeMemberRequest organizeMemberRequest,
      @ApiIgnore BindingResult bindingResult) {
    log.info(" OrganizeMemberController.create : [{}]", organizeMemberRequest);
    final Long memberId = TextUtil.validLong(memberIdStr, 0);
    if (TextUtil.nonValidId(memberId)) {
      return Rs.fail("成员不存在");
    }
    return service.updatePost(
            Collections.singletonList(memberId),
            organizeMemberRequest.getOrganizeId(),
            organizeMemberRequest.getPostId())
        ? Rs.ok()
        : Rs.fail("成员不存在");
  }

  //
  //  @PutMapping("/{id}")
  //  @ResponseBody
  //  @PreAuthorize("hasAnyAuthority('global:write','post:write')")
  //  @ApiOperation("更新岗位")
  //  public ResponseEntity<CommonResponse<Object>> update(
  //      @PathVariable("id") String idStr,
  //      @Valid PostRequest postRequest,
  //      @ApiIgnore BindingResult bindingResult) {
  //    log.info(" PostController.update : [{}]", postRequest);
  //    final long id = TextUtil.validLong(idStr, -1);
  //    if (id < 1) {
  //      return CommonResponse.wrongFormat("id");
  //    }
  //    postRequest.setId(id);
  //    return commonService.update(service, Post.class, postRequest, idStr, bindingResult);
  //  }
  //
  //  @DeleteMapping("/{id}")
  //  @ResponseBody
  //  @PreAuthorize("hasAnyAuthority('global:delete','post:delete')")
  //  @ApiOperation("删除岗位")
  //  public ResponseEntity<CommonResponse<Object>> delete(@PathVariable("id") String idStr) {
  //    log.info(" PostController.delete : [{}]", idStr);
  //    return commonService.delete(service, idStr);
  //  }
  //
  //  @DeleteMapping("/deleteBatch")
  //  @ResponseBody
  //  @PreAuthorize("hasAnyAuthority('global:delete','post:delete')")
  //  @ApiOperation("删除岗位批量")
  //  public ResponseEntity<CommonResponse<Object>> deleteBatch(@RequestParam("ids") List<Long> ids)
  // {
  //    log.info(" PostController.deleteBatch : [{}]", ids);
  //    return commonService.deleteBatch(service, ids);
  //  }
}
