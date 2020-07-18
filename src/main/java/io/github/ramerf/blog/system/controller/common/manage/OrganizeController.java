package io.github.ramerf.blog.system.controller.common.manage;

import com.alibaba.fastjson.JSON;
import io.github.ramerf.wind.core.entity.response.Rs;
import io.swagger.annotations.*;
import java.util.*;
import java.util.stream.Collectors;
import javax.annotation.Resource;
import javax.servlet.http.HttpSession;
import javax.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import io.github.ramerf.blog.system.entity.Constant.AccessPath;
import io.github.ramerf.blog.system.entity.domain.common.Organize;
import io.github.ramerf.blog.system.entity.pojo.common.OrganizePoJo;
import io.github.ramerf.blog.system.entity.request.common.OrganizeRequest;
import io.github.ramerf.blog.system.entity.response.common.OrganizeJsonTreeResponse;
import io.github.ramerf.blog.system.entity.response.common.OrganizeResponse;
import io.github.ramerf.blog.system.service.common.*;
import io.github.ramerf.blog.system.util.TextUtil;
import io.github.ramerf.blog.system.validator.common.OrganizeValidator;
import org.springframework.data.domain.PageImpl;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.util.CollectionUtils;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.*;
import springfox.documentation.annotations.ApiIgnore;

@Slf4j
@Controller("organizecm")
@PreAuthorize("hasAnyAuthority('global:read','manage:read','organize:read')")
@RequestMapping(AccessPath.MANAGE + "/organize")
@Api(tags = "管理端: 组织接口")
@SuppressWarnings("UnusedDeclaration")
public class OrganizeController {
  @Resource private PostService postService;
  @Resource private OrganizeService service;
  @Resource private ManagerService managerService;
  @Resource private CommonService commonService;
  @Resource private OrganizeValidator validator;

  @InitBinder("organizeRequest")
  public void initBinder(WebDataBinder binder) {
    binder.addValidators(validator);
  }

  @GetMapping("/index")
  @ApiOperation("组织页面")
  public String index(@ApiIgnore HttpSession session, @ApiIgnore Map<String, Object> map) {
    map.put("organizes", service.list(null));
    commonService.writeMenuAndSiteInfo(session, map);
    return "manage/organize/index";
  }

  @GetMapping("/page")
  @ResponseBody
  @ApiOperation("获取组织列表")
  public ResponseEntity<Rs<PageImpl<OrganizeResponse>>> page(
      @ApiParam("页号,从1开始,当page=size=-1时,表示不分页")
          @RequestParam(value = "page", required = false, defaultValue = "1")
          String pageStr,
      @RequestParam(value = "size", required = false, defaultValue = "10") String sizeStr,
      @ApiParam("查询条件") @RequestParam(value = "criteria", required = false) String criteria) {
    final int[] pageAndSize = TextUtil.validFixPageAndSize(pageStr, sizeStr);
    return commonService.page(
        service.page(criteria, pageAndSize[0], pageAndSize[1]), OrganizeResponse::of);
  }

  @GetMapping
  @ApiOperation("添加组织页面")
  public String create(
      @RequestParam(value = "prevId", required = false) String prevIdStr,
      @ApiIgnore HttpSession session,
      @ApiIgnore Map<String, Object> map) {
    final long prevId = TextUtil.validLong(prevIdStr, -1);
    if (TextUtil.isValidId(prevId)) {
      map.put("prev", service.getById(prevId));
    }
    commonService.writeMenuAndSiteInfo(session, map);
    //    map.put("organizes", service.list(null));
    //    map.put("managers", managerService.list(null));
    return "manage/organize/edit_pure::main-container";
  }

  @PostMapping
  @ResponseBody
  @PreAuthorize("hasAnyAuthority('global:create','organize:create')")
  @ApiOperation("添加组织")
  public ResponseEntity create(
      @Valid OrganizeRequest organizeRequest, BindingResult bindingResult) {
    log.info(" OrganizeController.create : [{}]", organizeRequest);
    return commonService.create(service, Organize.class, organizeRequest, bindingResult);
  }

  @GetMapping("/{id}")
  @ApiOperation("更新组织页面")
  public String update(
      @PathVariable("id") String idStr,
      @ApiIgnore HttpSession session,
      @ApiIgnore Map<String, Object> map) {
    return commonService.update(
        service,
        OrganizePoJo.class,
        idStr,
        "manage/organize/edit_pure::main-container",
        map,
        "organize",
        id -> {
          map.put(
              "organizes",
              service.list(null).stream()
                  .filter(o -> !Objects.equals(o.getId(), id))
                  .collect(Collectors.toList()));
          commonService.writeMenuAndSiteInfo(session, map);
          final Organize organize = service.getById(id);
          map.put("organize", organize);
          map.put("prev", Optional.ofNullable(organize).map(Organize::getPrev).orElse(null));
        },
        false);
  }

  @PutMapping("/{id}")
  @ResponseBody
  @PreAuthorize("hasAnyAuthority('global:write','organize:write')")
  @ApiOperation("更新组织")
  public ResponseEntity update(
      @PathVariable("id") String idStr,
      @Validated OrganizeRequest organizeRequest,
      BindingResult bindingResult) {
    log.info(" OrganizeController.update : [{}]", organizeRequest);
    return commonService.update(service, Organize.class, organizeRequest, idStr, bindingResult);
  }

  //  @PutMapping("/updateMemberOrLeader/{id}")
  //  @ResponseBody
  //  @PreAuthorize("hasAnyAuthority('global:write','organize:write')")
  //  @ApiOperation("更新组织成员/负责人")
  //  public ResponseEntity updateMemberOrLeader(
  //      @PathVariable("id") String idStr, @ApiParam OrganizeMemberRequest memberRequest) {
  //    log.info(" OrganizeController.updateMemberOrLeader : [{}]", memberRequest);
  //    final long id = TextUtil.validLong(idStr, -1);
  //    if (id < 1) {
  //      return CommonResponse.wrongFormat("id");
  //    }
  //    // 成员操作
  //    boolean memberFlag = !Objects.isNull(memberRequest.getRole()) && memberRequest.getRole() ==
  // 0;
  //    // 成员/负责人 id
  //    final List<Long> mlIds = memberRequest.getIds();
  //    if (CollectionUtils.isEmpty(mlIds) || mlIds.stream().anyMatch(mlId -> mlId < 1)) {
  //      return CommonResponse.wrongValue(memberFlag ? "成员" : "负责人");
  //    }
  //    final Integer operate = memberRequest.getBindingOperate();
  //    if (!BindingOperate.list().contains(operate)) {
  //      return CommonResponse.wrongValue("操作类型");
  //    }
  //    final Organize organize = service.getById(id);
  //    final List<Manager> members = organize.getMembers();
  //    final List<Long> memberIds =
  //        Optional.ofNullable(members).orElse(new ArrayList<>()).stream()
  //            .map(AbstractEntity::getId)
  //            .collect(Collectors.toList());
  //    List<Long> mls =
  //        Optional.ofNullable(memberFlag ? members : organize.getLeaders()).orElse(new
  // ArrayList<>())
  //            .stream()
  //            .map(AbstractEntity::getId)
  //            .collect(Collectors.toList());
  //    final int size = mls.size();
  //    switch (BindingOperate.map().get(operate)) {
  //      case DELETE:
  //        mls = mls.stream().filter(mId -> !mlIds.contains(mId)).collect(Collectors.toList());
  //        break;
  //      case ADD:
  //        Set<Long> set = new HashSet<>(mls);
  //        set.addAll(mlIds);
  //        mls = new ArrayList<>(set);
  //        break;
  //    }
  //    if (size == mls.size()) {
  //      return CommonResponse.exists(memberFlag ? "成员" : "负责人");
  //    }
  //    List<Manager> mlObjs = mls.stream().map(Manager::of).collect(Collectors.toList());
  //    if (memberFlag) {
  //      organize.setMembers(mlObjs);
  //    } else {
  //      if (!validMemberAndLeader(memberIds, mls)) {
  //        return CommonResponse.invalid("负责人必须是组织成员");
  //      }
  //      organize.setLeaders(mlObjs);
  //    }
  //    try {
  //      service.update(organize);
  //      return organize.getId() > 0
  //          ? CommonResponse.ok(organize.getId(), Txt.SUCCESS_EXEC_UPDATE)
  //          : CommonResponse.fail(Txt.FAIL_EXEC_UPDATE);
  //    } catch (Exception e) {
  //      log.warn(" CommonServiceImpl.update : [{}]", e.getMessage());
  //      return CommonResponse.fail(
  //          !StringUtils.isEmpty(e.getMessage())
  //                  && (e instanceof CommonException || e instanceof NullPointerException)
  //              ? e.getMessage()
  //              : Txt.FAIL_EXEC);
  //    }
  //  }

  @DeleteMapping("/{id}")
  @ResponseBody
  @PreAuthorize("hasAnyAuthority('global:delete','organize:delete')")
  @ApiOperation("删除组织")
  public ResponseEntity delete(@PathVariable("id") String idStr) {
    log.info(" OrganizeController.delete : [{}]", idStr);
    return commonService.delete(service, idStr);
  }

  @DeleteMapping("/deleteBatch")
  @ResponseBody
  @PreAuthorize("hasAnyAuthority('global:delete','organize:delete')")
  @ApiOperation("删除岗位批量")
  public ResponseEntity<Rs<Object>> deleteBatch(@RequestParam("ids") List<Long> ids) {
    log.info(" OrganizeController.deleteBatch : [{}]", ids);
    return commonService.deleteBatch(service, ids);
  }

  @GetMapping("/getJsonTreeAll")
  @ResponseBody
  @ApiOperation("获取所有组织json树")
  public ResponseEntity getJsonTreeAll() {
    final List<Organize> organizes = service.list(null);
    log.info(" OrganizeController.getJsonTreeAll : [{}]", JSON.toJSONString(organizes));
    // 所有可用菜单
    final List<OrganizeJsonTreeResponse> organizesAll = new ArrayList<>();
    // 可用菜单的树形结构
    organizes.forEach(organize -> organizesAll.add(OrganizeJsonTreeResponse.of(organize)));
    final List<OrganizeJsonTreeResponse> organizesRes =
        organizes.stream()
            .filter(organizePoJo -> Objects.isNull(organizePoJo.getPrevId()))
            .map(OrganizeJsonTreeResponse::of)
            .collect(Collectors.toList());
    organizesAll.removeAll(organizesRes);
    Stack<OrganizeJsonTreeResponse> retain = new Stack<>();
    organizesRes.forEach(retain::push);
    while (retain.size() > 0 && organizesAll.size() > 0) {
      OrganizeJsonTreeResponse organize = retain.pop();
      // 当前节点的子节点
      List<OrganizeJsonTreeResponse> child =
          organizesAll.stream()
              .filter(menuResponse -> menuResponse.getPrevId().equals(organize.getId()))
              .collect(Collectors.toList());
      // 子节点具有叶子节点,入栈
      child.stream().filter(OrganizeJsonTreeResponse::getHasChild).forEach(retain::push);
      organize.setChildren(child);
      organizesAll.removeAll(child);
    }
    return Rs.ok(organizesRes);
  }

  //  @GetMapping("/listMembers/{id}")
  //  @ApiOperation("获取组织直属成员列表")
  //  public ResponseEntity listMembers(@PathVariable("id") String idStr) {
  //    final long id = TextUtil.validLong(idStr, -1);
  //    if (id < 1) {
  //      return CommonResponse.wrongFormat(idStr);
  //    }
  //    final Organize organize = service.getById(id);
  //    if (Objects.isNull(organize)) {
  //      return CommonResponse.notExists("组织");
  //    }
  //    final List<OrganizeMemberResponse> responses =
  //        service.listMembers(id).stream()
  //            .map(
  //                manager -> {
  //                  final OrganizeMemberResponse response = OrganizeMemberResponse.of(manager);
  //                  response.setOrganizes(
  //                      service.listRelation(response.getId()).stream()
  //                          .map(OrganizeMemberRelationResponse::getOrganizeName)
  //                          .collect(Collectors.toList()));
  //                  response.setLeader(
  //                      organize.getLeaders().stream()
  //                          .anyMatch(m -> m.getId().equals(response.getId())));
  //                  return response;
  //                })
  //            .collect(Collectors.toList());
  //
  //    return CommonResponse.ok(responses);
  //  }

  //  @GetMapping("/listLeaders/{id}")
  //  @ApiOperation("获取组织负责人列表")
  //  public ResponseEntity listLeaders(@PathVariable("id") String idStr) {
  //    final long id = TextUtil.validLong(idStr, -1);
  //    if (id < 1) {
  //      return CommonResponse.wrongFormat(idStr);
  //    }
  //    return CommonResponse.ok(
  //        service.listLeaders(id).stream()
  //            .map(OrganizeMemberResponse::of)
  //            .collect(Collectors.toList()));
  //  }

  @GetMapping("/listByMember")
  @ResponseBody
  @ApiOperation("获取成员所在组织列表")
  public ResponseEntity listByMember(@RequestParam("memberId") String memberIdStr) {
    final long memberId = TextUtil.validLong(memberIdStr, -1);
    if (memberId < 1) {
      return Rs.wrongValue("成员");
    }
    return Rs.ok(service.listRelation(memberId));
  }

  /** 校验成员和负责人的正确性: 1. 负责人为空 2. 都为空. 3. 成员不为空,且负责人全都属于成员 */
  private boolean validMemberAndLeader(List<Long> memberIds, List<Long> leaderIds) {
    return CollectionUtils.isEmpty(leaderIds)
        || (!CollectionUtils.isEmpty(memberIds) && memberIds.containsAll(leaderIds));
  }
}
