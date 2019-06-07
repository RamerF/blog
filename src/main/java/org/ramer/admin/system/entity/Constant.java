package org.ramer.admin.system.entity;

import lombok.AllArgsConstructor;

import java.util.*;
import java.util.stream.Collectors;

/**
 * 系统常量定义.
 *
 * @author ramer
 */
@SuppressWarnings({"unused"})
public class Constant {

  public static final String DEFAULT_CSRF_TOKEN = "X-CSRF-TOKEN";
  // 分页每页默认条数
  public static final int DEFAULT_PAGE_SIZE = 10;
  // 默认list字符串分隔符
  public static final String DEFAULT_STRING_SPLIT = ",";
  public static final String DEFAULT_CHARSET_ENCODE = "UTF-8";
  public static final String DEFAULT_DATE_PATTERN = "yyyy-MM-dd";

  public enum Config {
    /** APP版本号 */
    APP_VERSION("APP_VERSION", "1", "APP版本号"),
    /** 电表检查一致性的属性 */
    DIAN_BIAO_CHECK_PROP("DIAN_BIAO_CHECK_PROP", "biaohao,huhao,huming", "电表检查一致性的属性");
    public String code;
    public String value;
    public String name;

    Config(String code, String value, String name) {
      this.code = code;
      this.value = value;
      this.name = name;
    }

    public List<Config> list() {
      return Arrays.asList(Config.values());
    }
  }
  /**
   * <pre>
   * @Author ruihao.zhai@gmail.com
   * @Description 系统操做存储的ID 不真实存在
   * @Date 10:08 2019/2/19
   * @Param
   * @return
   * </pre>
   **/
  public static final Long SYSTEM_ID = 0L;

  /** 接口路径 */
  public class AccessPath {
    public static final String USER = "/user";
    public static final String MANAGE = "/manage";
  }

  // 静态资源路径
  public class ResourcePath {
    // 管理端
    public static final String MANAGER = "/manager";
    // 用户端
    public static final String USER = "/user";
    // 公共资源
    public static final String PUBLIC = "/public";
    // 默认
    public static final String DEFAULT = "/default";
  }

  // 返回结果码
  public enum ResultCode {
    E0000("成功"),
    E0001("参数错误[code,signed,data不能为空]"),
    E0002("接口验证失败、加密方式错误或secret错误"),
    E0003("其他原因");
    private String desc;

    ResultCode(String desc) {
      this.desc = desc;
    }

    @Override
    public String toString() {
      return desc;
    }
  }

  /** 通用文本 */
  public static class Txt {
    // 操作成功提示文本
    public static final String SUCCESS_EXEC = "操作成功";
    public static final String SUCCESS_EXEC_ADD = "添加成功";
    public static final String SUCCESS_EXEC_UPDATE = "更新成功";
    public static final String SUCCESS_EXEC_DELETE = "删除成功";
    // 操作失败提示文本
    public static final String FAIL_EXEC = "操作失败,数据格式有误";
    public static final String FAIL_EXEC_ADD = "添加失败,数据格式有误";
    public static final String FAIL_EXEC_ADD_EXIST = "添加失败,数据已存在";
    public static final String FAIL_EXEC_UPDATE = "更新失败,数据格式有误";
    public static final String FAIL_EXEC_UPDATE_NOT_EXIST = "更新失败,记录不存在";
    public static final String FAIL_EXEC_DELETE_NOT_EXIST = "删除失败";
    // 系统异常提示文本
    public static final String ERROR_PARAM = "参数错误";
    public static final String ERROR_SYSTEM = "系统繁忙，请稍后再试 ！";
    public static final String NOT_IMPLEMENT = "方法未实现";
  }

  public static class State {
    /** 可用状态 */
    public static final int STATE_ON = 1;
    /** 不可用状态 */
    public static final int STATE_OFF = -1;
  }

  /** 可登录 */
  public static final int ACTIVE_TRUE = 1;
  /** 不可登录 */
  public static final int ACTIVE_FALSE = 0;

  /** 数据字典CODE */
  public class DataDictCode {
    /** 电表状态 */
    public static final String DIAN_BIAO_STATUS_CODE = "DIAN_BIAO_STATUS_CODE";
  }

  /**
   *
   *
   * <pre>
   *     性别:
   *     0: 男
   *     1: 女
   * </pre>
   */
  public enum Gender {
    MALE("男"),
    FEMALE("女");
    public String desc;

    Gender(String desc) {
      this.desc = desc;
    }

    @Override
    public String toString() {
      return this.desc;
    }

    public static List<Integer> ordinalList() {
      List<Integer> ordinals = new ArrayList<>();
      for (Gender gender : values()) {
        ordinals.add(gender.ordinal());
      }
      return ordinals;
    }

    public static String desc(int index) {
      for (Gender gender : values()) {
        if (gender.ordinal() == index) {
          return gender.desc;
        }
      }
      return "无";
    }
  }

  /** 权限. */
  public enum PrivilegeEnum {
    READ("read", "读"),
    CREATE("create", "增"),
    WRITE("write", "写"),
    DELETE("delete", "删"),
    ALL("*", "所有");
    public String name;
    public String remark;
    /** 全局 */
    public static PrivilegeEnumEntity GLOBAL = new PrivilegeEnumEntity("global", "全局");
    /** 管理端 */
    public static PrivilegeEnumEntity MANAGE = new PrivilegeEnumEntity("manage", "管理端");
    /** 用户端 */
    public static PrivilegeEnumEntity USER = new PrivilegeEnumEntity("user", "用户端");
    /** 用户管理端 */
    public static PrivilegeEnumEntity USER_MANAGE = new PrivilegeEnumEntity("userManage", "用户端管理");

    /** 认证对象 */
    @AllArgsConstructor
    public static class PrivilegeEnumEntity {
      public String name;
      public String remark;
    }

    PrivilegeEnum(String name, String remark) {
      this.name = name;
      this.remark = remark;
    }

    public static Map<String, String> map() {
      Map<String, String> map = new HashMap<>();
      for (PrivilegeEnum privilegeEnum : values()) {
        map.put(privilegeEnum.name, privilegeEnum.remark);
      }
      return map;
    }
  }

  /** 任务状态 */
  public class CTaskStatus {
    public static final int UNFINISHED = 0;
    public static final int FINISHED = 1;
  }

  /** 电表勘查状态 */
  public static class DianBiaoProspectingStatus {
    /** 未开始 */
    public static final int NOT_START = 0;
    /** 进行中 */
    public static final int EXECUTING = 1;
    /** 已完成 */
    public static final int FINISHED = 2;
    /** 所有 */
    public static final int ALL = -1;

    public static List<Integer> ordinalList() {
      return Arrays.asList(NOT_START, EXECUTING, FINISHED, ALL);
    }

    public static String desc(int status) {
      switch (status) {
        case 0:
          return "未勘查";
        case 1:
          return "勘查中";
        case 2:
          return "已勘查";
        default:
          return "所有";
      }
    }
  }

  /** 存储在session中的key */
  public class SessionKey {
    // 前端登录对象
    public static final String LOGIN_USER = "user";
    // 管理端
    public static final String LOGIN_MANAGER = "manager";
  }

  /** 数据库字典类型CODE */
  public static class DataDictTypeCode {
    // 用于演示CODE
    public static final String DEMO_CODE = "DEMO_CODE";
  }

  /** 资源文件CODE. */
  public class CommonMediaCode {}

  /** 前端绑定操作: 0: 删除,1: 添加 */
  public enum BindingOperate {
    DELETE,
    ADD;

    public static Map<Integer, BindingOperate> map() {
      return Arrays.stream(BindingOperate.values())
          .collect(Collectors.toMap(Enum::ordinal, val -> val));
    }

    public static List<Integer> list() {
      List<Integer> list = new ArrayList<>();
      Arrays.asList(BindingOperate.values()).forEach(val -> list.add(val.ordinal()));
      return list;
    }
  }

  /** 系统配置CODE */
  public static class ConfigCode {
    public static final String SITE_TITLE = "SITE_TITLE";
    public static final String SITE_NAME = "SITE_NAME";
    public static final String SITE_COPYRIGHT = "SITE_COPYRIGHT";
  }
}
