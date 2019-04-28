package org.ramer.admin.system.entity.pojo.common;

import org.ramer.admin.system.entity.domain.common.CommonMediaCategory;
import org.ramer.admin.system.entity.pojo.AbstractEntityPoJo;
import lombok.*;

/**
 * 通用多媒体文件存储分类.
 *
 * @author Ramer @Date 1/10/2019
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class CommonMediaCategoryPoJo extends AbstractEntityPoJo {
  private String code;
  /** 名称 */
  private String name;
  /** 备注 */
  private String remark;

  public static CommonMediaCategoryPoJo of(CommonMediaCategory category) {
    return new CommonMediaCategoryPoJo().of(category, CommonMediaCategoryPoJo.class);
  }
}
