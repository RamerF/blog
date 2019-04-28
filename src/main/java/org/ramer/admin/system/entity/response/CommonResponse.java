package org.ramer.admin.system.entity.response;

import org.ramer.admin.system.entity.Constant.Txt;
import java.util.Collections;
import lombok.*;
import org.springframework.data.domain.PageImpl;
import org.springframework.http.ResponseEntity;

/**
 * 通用JSON响应.
 *
 * @author ramer
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class CommonResponse {
  /** 请求处理结果: 成功/失败 */
  private boolean result;
  /** 请求成功时,该值是具体返回内容,否则为空 */
  private Object data;
  /** 请求结果描述 */
  private String msg;

  public static ResponseEntity ok() {
    return ResponseEntity.ok(new CommonResponse(true, null, Txt.SUCCESS_EXEC));
  }
  /**
   * 默认执行成功构造器.
   *
   * @param data 返回给页面数据
   */
  public static ResponseEntity ok(Object data) {
    return ResponseEntity.ok(new CommonResponse(true, data, Txt.SUCCESS_EXEC));
  }

  public static ResponseEntity ok(Object data, String msg) {
    return ResponseEntity.ok(new CommonResponse(true, data, msg));
  }

  public static ResponseEntity fail() {
    return ResponseEntity.ok(new CommonResponse(false, null, Txt.ERROR_SYSTEM));
  }

  /** 返回空的page对象 */
  public static ResponseEntity emptyPage() {
    return ResponseEntity.ok(new PageImpl<>(Collections.emptyList()));
  }

  /** 返回空的list对象 */
  public static ResponseEntity emptyList() {
    return ResponseEntity.ok(Collections.emptyList());
  }

  /**
   * 默认执行失败构造器.
   *
   * @param msg 失败提示
   */
  public static ResponseEntity fail(String msg) {
    return ResponseEntity.ok(new CommonResponse(false, null, msg));
  }

  public static ResponseEntity fail(String msg, Object data) {
    return ResponseEntity.ok(new CommonResponse(false, data, msg));
  }

  public static ResponseEntity wrongFormat(String arg) {
    return ResponseEntity.ok(new CommonResponse(false, null, String.format("参数格式不正确: [%s]", arg)));
  }

  public static ResponseEntity wrongValue(String arg) {
    return ResponseEntity.ok(new CommonResponse(false, null, String.format("参数值不正确: [%s]", arg)));
  }

  public static ResponseEntity canNotBlank(String arg) {
    return ResponseEntity.ok(new CommonResponse(false, null, String.format("参数值不能为空: [%s]", arg)));
  }

  public static ResponseEntity invalid(String arg) {
    return ResponseEntity.ok(new CommonResponse(false, null, String.format("参数值无效: [%s]", arg)));
  }

  public static ResponseEntity exists(String arg) {
    return ResponseEntity.ok(new CommonResponse(false, null, String.format("数据已存在: [%s]", arg)));
  }

  public static ResponseEntity notExists(String arg) {
    return ResponseEntity.ok(new CommonResponse(false, null, String.format("数据不存在: [%s]", arg)));
  }

  /** 2019-04-24 11:33:42 添加时间戳字段 */
  public String getTimestamp() {
    return String.valueOf(System.currentTimeMillis());
  }
}
