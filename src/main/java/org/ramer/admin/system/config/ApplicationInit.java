package org.ramer.admin.system.config;

import java.time.*;
import java.util.*;
import javax.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.ramer.admin.system.entity.Constant.*;
import org.ramer.admin.system.entity.domain.common.Config;
import org.ramer.admin.system.entity.domain.common.*;
import org.ramer.admin.system.service.common.*;
import org.ramer.admin.system.util.EncryptUtil;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

/**
 * Run once after the project start.
 *
 * @author Ramer
 */
@Slf4j
@Component
public class ApplicationInit implements ApplicationRunner {
  @Resource private MenuService menuService;
  @Resource private RoleService rolesService;
  @Resource private ConfigService configService;
  @Resource private ManagerService managerService;
  @Resource private DataDictService dataDictService;
  @Resource private PrivilegeService privilegeService;
  @Resource private DataDictTypeService dataDictTypeService;

  @Override
  public void run(ApplicationArguments args) {
    if (rolesService.count() < 1) {
      log.debug("onApplicationEvent ：init security");
      // the access privileges for manage
      Privilege managePrivilege = new Privilege();
      managePrivilege.setExp(PrivilegeEnum.MANAGE.name.concat(":").concat(PrivilegeEnum.READ.name));
      managePrivilege.setRemark(
          PrivilegeEnum.MANAGE.remark.concat(":").concat(PrivilegeEnum.MANAGE.remark));
      privilegeService.create(managePrivilege);
      // the access privileges for super admin default
      Role role = new Role();
      role.setName("超级管理员");
      role.setRemark("超级管理员");
      Privilege globalPrivilege = new Privilege();
      globalPrivilege.setExp(PrivilegeEnum.GLOBAL.name.concat(":").concat(PrivilegeEnum.ALL.name));
      globalPrivilege.setRemark(
          PrivilegeEnum.GLOBAL.remark.concat(":").concat(PrivilegeEnum.ALL.remark));
      List<Privilege> privileges = new ArrayList<>();
      privileges.add(globalPrivilege);
      role.setPrivileges(privileges);
      role.setMenus(menuService.list(null));
      privilegeService.createBatch(privileges);
      rolesService.create(role);
      // init super user
      Manager manager = managerService.getByEmpNo("admin");
      if (manager == null) {
        manager = new Manager();
        manager.setEmpNo("admin");
        manager.setIsActive(true);
        manager.setPhone("18990029043");
        manager.setGender(Gender.MALE.ordinal());
        manager.setState(State.STATE_ON);
        manager.setCreateTime(LocalDateTime.now());
        manager.setUpdateTime(LocalDateTime.now());
        manager.setPassword(EncryptUtil.execEncrypt("admin"));
        manager.setName("admin");
        manager.setRoles(Collections.singletonList(role));
        managerService.create(manager);

        // init site info
        configService.create(new Config("SITE_TITLE", "后台管理系统框架", "后台管理系统框架", "网站标题,刷新生效"));
        configService.create(new Config("SITE_NAME", "XXX管理系统", "XXX管理系统", "系统名称,刷新生效"));
        configService.create(
            new Config(
                "SITE_COPYRIGHT",
                "copyright",
                "Copyright ©2018 ramer v1.0 All Rights Reserved",
                "注册信息,刷新生效"));

        // init menu
        Menu systemMenu =
            menuService.create(new Menu(null, false, "系统", "system", null, 2, "fa-cog", "系统"));
        Menu configMenu =
            menuService.create(
                new Menu(
                    systemMenu,
                    true,
                    "参数配置",
                    "config",
                    "/manage/config/index",
                    1,
                    "fa-cog",
                    "参数配置"));
        Menu dataDictMenu =
            menuService.create(
                new Menu(
                    systemMenu,
                    true,
                    "数据字典",
                    "dataDict",
                    "/manage/dataDict/index",
                    2,
                    "fa-cog",
                    "数据字典"));
        Menu managerMenu =
            menuService.create(
                new Menu(
                    systemMenu,
                    true,
                    "管理员管理",
                    "manager",
                    "/manage/manager/index",
                    3,
                    "fa-cog",
                    "管理员管理"));
        Menu rolesMenu =
            menuService.create(
                new Menu(
                    systemMenu, true, "角色管理", "role", "/manage/role/index", 4, "fa-cog", "角色管理"));
        Menu menuMenu =
            menuService.create(
                new Menu(
                    systemMenu, true, "菜单管理", "menu", "/manage/menu/index", 5, "fa-cog", "菜单管理"));

        // init role menu
        ArrayList<Menu> menus = new ArrayList<>();
        menus.add(systemMenu);
        menus.add(configMenu);
        menus.add(dataDictMenu);
        menus.add(managerMenu);
        menus.add(rolesMenu);
        menus.add(menuMenu);
        Role superAdmin = rolesService.getById(1);
        superAdmin.setMenus(menus);
        rolesService.create(superAdmin);
      }
      // user role
      role = new Role();
      role.setName("普通用户");
      role.setRemark("普通用户");
      privileges = new ArrayList<>();
      Privilege userPrivilege = new Privilege();
      // the access privileges for user default
      userPrivilege.setExp(PrivilegeEnum.USER.name.concat(":").concat(PrivilegeEnum.ALL.name));
      userPrivilege.setRemark(
          PrivilegeEnum.USER.remark.concat(":").concat(PrivilegeEnum.ALL.remark));
      privileges.add(userPrivilege);
      role.setPrivileges(privileges);
      privilegeService.createBatch(privileges);
      rolesService.create(role);

      // 添加数据库字典,用于演示
      DataDictType dataDictType = new DataDictType();
      dataDictType.setCode(DataDictTypeCode.DEMO_CODE);
      dataDictType.setName("演示字典类型");
      dataDictType.setRemark("演示字典类型备注");
      dataDictTypeService.create(dataDictType);
      DataDict dataDict = new DataDict();
      dataDict.setCode("演示字典CODE");
      dataDict.setName("演示字典");
      dataDict.setRemark("演示字典备注");
      dataDict.setDataDictType(dataDictType);
      dataDictService.create(dataDict);
    }
  }
}
