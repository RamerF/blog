package org.ramer.admin.system.controller.common;

import io.swagger.annotations.*;
import java.util.Map;
import javax.annotation.Resource;
import javax.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.ramer.admin.system.entity.domain.common.Manager;
import org.ramer.admin.system.entity.pojo.common.ManagerPoJo;
import org.ramer.admin.system.entity.request.common.ManagerRequest;
import org.ramer.admin.system.entity.response.CommonResponse;
import org.ramer.admin.system.entity.response.common.ManagerResponse;
import org.ramer.admin.system.service.common.CommonService;
import org.ramer.admin.system.service.common.ManagerService;
import org.ramer.admin.system.util.TextUtil;
import org.ramer.admin.system.validator.common.ManagerValidator;
import org.springframework.data.domain.PageImpl;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.*;

@Slf4j
@Controller("managercm")
@PreAuthorize("hasAnyAuthority('global:read','manager:read')")
@RequestMapping("/common/manager")
@Api(tags = "管理员接口")
@SuppressWarnings("UnusedDeclaration")
public class ManagerController {
  @Resource private ManagerService service;
  @Resource private CommonService commonService;
  @Resource private ManagerValidator validator;

  @InitBinder
  void initBinder(WebDataBinder binder) {
    binder.addValidators(validator);
  }

  @GetMapping("/index")
  @ApiOperation("管理员页面")
  public String index() {
    return "manager/index";
  }

  @GetMapping("/page")
  @ResponseBody
  @ApiOperation("获取管理员列表")
  public ResponseEntity<CommonResponse<PageImpl<ManagerResponse>>> page(
      @ApiParam("页号,从1开始,当page=size=-1时,表示不分页")
          @RequestParam(value = "page", required = false, defaultValue = "1")
          String pageStr,
      @RequestParam(value = "size", required = false, defaultValue = "10") String sizeStr,
      @ApiParam("查询条件") @RequestParam(value = "criteria", required = false) String criteria) {
    final int[] pageAndSize = TextUtil.validFixPageAndSize(pageStr, sizeStr);
    return commonService.page(
        service.page(criteria, pageAndSize[0], pageAndSize[1]), ManagerResponse::of);
  }



}
