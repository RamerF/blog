package org.ramer.admin.system.config;

import java.util.*;
import javax.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.ramer.admin.system.entity.Constant.*;
import org.ramer.admin.system.entity.Constant.PrivilegeEnum.PrivilegeEnumEntity;
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
      log.info("run:[init superAdmin]");
      // 所有菜单
      ArrayList<Menu> menus = initMenus();
      // 数据字典
      initDataDict();
      // 参数配置
      initConfig();
      // 超级管理员
      initSuperAdmin(menus);
      // 管理端观察员
      initVisitorAdmin(menus);
      // 普通用户
      initUser();
    }
  }

  /** 初始化超级管理员. */
  private void initSuperAdmin(final List<Menu> menus) {
    final Manager superAdmin = new Manager();
    superAdmin.setEmpNo("superAdmin");
    superAdmin.setIsActive(true);
    superAdmin.setPhone("18990029043");
    superAdmin.setGender(Gender.MALE.ordinal());
    superAdmin.setState(State.STATE_ON);
    superAdmin.setCreateTime(new Date());
    superAdmin.setUpdateTime(new Date());
    superAdmin.setPassword(EncryptUtil.execEncrypt("superAdmin"));
    superAdmin.setName("superAdmin");
    superAdmin.setRoles(Collections.singletonList(initSuperAdminRole(menus)));
    managerService.create(superAdmin);
  }

  /** 初始化管理端观察员. */
  private void initVisitorAdmin(final List<Menu> menus) {
    final Manager visitor = new Manager();
    visitor.setEmpNo("visitor");
    visitor.setIsActive(true);
    visitor.setPhone("18990000000");
    visitor.setGender(Gender.MALE.ordinal());
    visitor.setState(State.STATE_ON);
    visitor.setCreateTime(new Date());
    visitor.setUpdateTime(new Date());
    visitor.setPassword(EncryptUtil.execEncrypt("visitor"));
    visitor.setName("visitor");
    visitor.setRoles(Collections.singletonList(initVisitorAdminRole(menus)));
    managerService.create(visitor);
  }

  private Privilege initPrivilege(
      final PrivilegeEnumEntity privilegeEnumEntity, final PrivilegeEnum privilegeEnum) {
    Privilege privilege = new Privilege();
    privilege.setExp(privilegeEnumEntity.name.concat(":").concat(privilegeEnum.name));
    privilege.setName(privilegeEnumEntity.remark.concat(":").concat(privilegeEnum.remark));
    privilegeService.create(privilege);
    return privilege;
  }

  private Role initVisitorAdminRole(final List<Menu> menus) {
    Role visitorAdminRole = new Role();
    visitorAdminRole.setName("观察员");
    visitorAdminRole.setRemark("观察员");
    // 管理端读权限
    visitorAdminRole.setPrivileges(
        Collections.singletonList(initPrivilege(PrivilegeEnum.MANAGE, PrivilegeEnum.READ)));
    visitorAdminRole.setMenus(menus);
    rolesService.create(visitorAdminRole);
    return visitorAdminRole;
  }

  private Role initSuperAdminRole(final List<Menu> menus) {
    Role superAdminRole = new Role();
    superAdminRole.setName("超级管理员");
    superAdminRole.setRemark("超级管理员");
    // 全局所有权限
    superAdminRole.setPrivileges(
        Collections.singletonList(initPrivilege(PrivilegeEnum.GLOBAL, PrivilegeEnum.ALL)));
    superAdminRole.setMenus(menus);
    rolesService.create(superAdminRole);
    return superAdminRole;
  }

  /** 初始化普通用户. */
  private void initUser() {
    Role userRole = new Role();
    userRole.setName("普通用户");
    userRole.setRemark("普通用户");
    Privilege userPrivilege = new Privilege();
    userPrivilege.setExp(PrivilegeEnum.USER.name.concat(":").concat(PrivilegeEnum.ALL.name));
    userPrivilege.setName(PrivilegeEnum.USER.remark.concat(":").concat(PrivilegeEnum.ALL.remark));
    final List<Privilege> userPrivileges = Collections.singletonList(userPrivilege);
    privilegeService.createBatch(userPrivileges);
    userRole.setPrivileges(userPrivileges);
    rolesService.create(userRole);
  }

  /** 初始化菜单. */
  private ArrayList<Menu> initMenus() {
    Menu systemMenu =
        menuService.create(Menu.builder().hasChild(true).name("系统").sortWeight(100).build());
    Menu articleMenu =
        menuService.create(
            Menu.builder()
                .parentId(systemMenu.getId())
                .hasChild(false)
                .name("文章管理")
                .url("/manage/article/index")
                .sortWeight(1)
                .build());
    Menu configMenu =
        menuService.create(
            Menu.builder()
                .parentId(systemMenu.getId())
                .hasChild(false)
                .name("参数配置")
                .url("/manage/config/index")
                .sortWeight(5)
                .build());
    Menu dataDictMenu =
        menuService.create(
            Menu.builder()
                .parentId(systemMenu.getId())
                .hasChild(false)
                .name("数据字典")
                .url("/manage/dataDict/index")
                .sortWeight(10)
                .build());
    Menu managerMenu =
        menuService.create(
            Menu.builder()
                .parentId(systemMenu.getId())
                .hasChild(false)
                .name("管理员管理")
                .url("/manage/manager/index")
                .sortWeight(15)
                .build());
    Menu roleMenu =
        menuService.create(
            Menu.builder()
                .parentId(systemMenu.getId())
                .hasChild(false)
                .name("角色管理")
                .url("/manage/role/index")
                .sortWeight(20)
                .build());
    Menu privilegeMenu =
        menuService.create(
            Menu.builder()
                .parentId(systemMenu.getId())
                .hasChild(false)
                .name("权限管理")
                .url("/manage/privilege/index")
                .sortWeight(25)
                .build());
    Menu menuMenu =
        menuService.create(
            Menu.builder()
                .parentId(systemMenu.getId())
                .hasChild(false)
                .name("菜单管理")
                .url("/manage/menu/index")
                .sortWeight(30)
                .build());
    ArrayList<Menu> menus = new ArrayList<>();
    menus.add(articleMenu);
    menus.add(systemMenu);
    menus.add(configMenu);
    menus.add(dataDictMenu);
    menus.add(managerMenu);
    menus.add(roleMenu);
    menus.add(privilegeMenu);
    menus.add(menuMenu);
    return menus;
  }

  /** init site info */
  private void initConfig() {
    configService.create(new Config("SITE_TITLE", "后台管理系统框架", "后台管理系统框架", "网站标题,刷新生效"));
    configService.create(new Config("SITE_NAME", "XXX管理系统", "XXX管理系统", "系统名称,刷新生效"));
    configService.create(
        new Config(
            "SITE_COPYRIGHT",
            "copyright",
            "Copyright ©2018 ramer v1.0 All Rights Reserved",
            "注册信息,刷新生效"));
  }

  /** 添加数据库字典,用于演示 */
  private void initDataDict() {
    DataDictType dataDictType = new DataDictType();
    dataDictType.setCode(DataDictTypeCode.DEMO_CODE);
    dataDictType.setName("演示字典类型");
    dataDictType.setRemark("演示字典类型备注");
    dataDictTypeService.create(dataDictType);
    DataDict dataDict = new DataDict();
    dataDict.setCode("演示字典CODE");
    dataDict.setName("演示字典");
    dataDict.setRemark("演示字典备注");
    dataDict.setDataDictTypeId(dataDictType.getId());
    dataDictService.create(dataDict);
  }
}
