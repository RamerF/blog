package org.ramer.admin.system.entity.response;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import lombok.*;
import org.ramer.admin.system.entity.Constant.Txt;
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
@SuppressWarnings({"unchecked"})
@ApiModel("通用响应")
public class CommonResponse<T> {
  /** 请求处理结果: 成功/失败 */
  @ApiModelProperty(value = "处理结果: true: 成功,false: 失败")
  private boolean result;
  /** 请求成功时,该值是具体返回内容,否则为空 */
  @ApiModelProperty(value = "数据内容: 请求成功时,该值是具体返回内容,否则为空")
  private T data;
  /** 请求结果描述 */
  @ApiModelProperty(value = "描述信息: 请求结果描述")
  private String msg;
  /** 响应时间戳 */
  @ApiModelProperty(value = "响应时间戳")
  private LocalDateTime timestamp = LocalDateTime.now();

  private CommonResponse(final boolean result, final T data, final String msg) {
    this.result = result;
    this.data = data;
    this.msg = msg;
  }

  public static <T> ResponseEntity<CommonResponse<T>> ok() {
    return ResponseEntity.ok(new CommonResponse<>(true, null, Txt.SUCCESS_EXEC));
  }
  /**
   * 默认执行成功构造器.
   *
   * @param data 返回给页面数据
   */
  public static <T> ResponseEntity<CommonResponse<T>> ok(T data) {
    return ResponseEntity.ok(new CommonResponse<>(true, data, Txt.SUCCESS_EXEC));
  }

  public static <T> ResponseEntity<CommonResponse<T>> ok(T data, String msg) {
    return ResponseEntity.ok(new CommonResponse<>(true, data, msg));
  }

  public static <T> ResponseEntity<CommonResponse<T>> fail() {
    return ResponseEntity.ok(new CommonResponse<>(false, null, Txt.ERROR_SYSTEM));
  }

  /** 返回空的page对象 */
  public static <T> ResponseEntity<CommonResponse<PageImpl<T>>> emptyPage() {
    return ResponseEntity.ok(
        new CommonResponse<>(true, new PageImpl<>(Collections.emptyList()), Txt.SUCCESS_EXEC));
  }

  /** 返回空的list对象 */
  public static <T> ResponseEntity<CommonResponse<List<T>>> emptyList() {
    return ResponseEntity.ok(new CommonResponse<>(true, Collections.emptyList(), Txt.SUCCESS_EXEC));
  }

  /**
   * 默认执行失败构造器.
   *
   * @param msg 失败提示
   */
  public static <T> ResponseEntity<CommonResponse<T>> fail(String msg) {
    return ResponseEntity.ok(new CommonResponse<>(false, null, msg));
  }

  public static <T> ResponseEntity<CommonResponse<T>> fail(String msg, T data) {
    return ResponseEntity.ok(new CommonResponse<>(false, data, msg));
  }

  public static <T> ResponseEntity<CommonResponse<T>> wrongFormat(String arg) {
    return ResponseEntity.ok(
        new CommonResponse<>(false, null, String.format("参数格式不正确: [%s]", arg)));
  }

  public static <T> ResponseEntity<CommonResponse<T>> wrongValue(String arg) {
    return ResponseEntity.ok(new CommonResponse<>(false, null, String.format("参数值不正确: [%s]", arg)));
  }

  public static <T> ResponseEntity<CommonResponse<T>> canNotBlank(String arg) {
    return ResponseEntity.ok(
        new CommonResponse<>(false, null, String.format("参数值不能为空: [%s]", arg)));
  }

  public static <T> ResponseEntity<CommonResponse<T>> invalid(String arg) {
    return ResponseEntity.ok(new CommonResponse<>(false, null, String.format("参数值无效: [%s]", arg)));
  }

  public static <T> ResponseEntity<CommonResponse<T>> exists(String arg) {
    return ResponseEntity.ok(new CommonResponse<>(false, null, String.format("数据已存在: [%s]", arg)));
  }

  public static <T> ResponseEntity<CommonResponse<T>> notExists(String arg) {
    return ResponseEntity.ok(
        new CommonResponse<>(false, (T) null, String.format("数据不存在: [%s]", arg)));
  }

  public static <T> ResponseEntity<CommonResponse<T>> notPresent(String arg) {
    return ResponseEntity.ok(
        new CommonResponse<>(false, (T) null, String.format("参数未传递: [%s]", arg)));
  }
}
