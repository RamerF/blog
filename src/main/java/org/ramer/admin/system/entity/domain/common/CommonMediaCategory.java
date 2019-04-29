package org.ramer.admin.system.entity.domain.common;

import org.ramer.admin.system.entity.Constant;
import org.ramer.admin.system.entity.domain.AbstractEntity;
import javax.persistence.Column;
import javax.persistence.Entity;
import lombok.*;
import org.hibernate.annotations.Table;

@Entity(name = CommonMediaCategory.TABLE_NAME)
@Table(appliesTo = CommonMediaCategory.TABLE_NAME, comment = "通用多媒体文件存储类别")
@Data
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class CommonMediaCategory extends AbstractEntity {
  public static final String TABLE_NAME = "common_media_category";

  /** {@link Constant.CommonMediaCode} */
  @Column(columnDefinition = "VARCHAR(255) NOT NULL COMMENT '该字段确定来源'")
  private String code;

  @Column(columnDefinition = "VARCHAR(255) NOT NULL COMMENT '名称'")
  private String name;

  @Column(columnDefinition = "VARCHAR(255) COMMENT '备注'")
  private String remark;

  public static CommonMediaCategory of(Long id) {
    CommonMediaCategory category = new CommonMediaCategory();
    category.setId(id);
    return category;
  }
}
