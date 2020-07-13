package org.ramer.admin.system.entity.domain.common;

import javax.persistence.*;
import lombok.*;
import org.hibernate.annotations.Table;
import org.ramer.admin.system.entity.domain.AbstractEntity;

@Entity(name = CommonMedia.TABLE_NAME)
@Table(appliesTo = CommonMedia.TABLE_NAME, comment = "通用多媒体文件存储")
@Data
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class CommonMedia extends AbstractEntity {
  public static final String TABLE_NAME = "common_media";

  @Column(columnDefinition = "VARCHAR(255) NOT NULL COMMENT '该字段确定来源'")
  private String code;

  @Column(columnDefinition = "VARCHAR(255) NOT NULL COMMENT '地址'")
  private String url;

  @Column(columnDefinition = "VARCHAR(255) COMMENT '备注'")
  private String remark;

  @Column(name = "category_id", insertable = false, updatable = false)
  private Long categoryId;

  @ManyToOne
  @JoinColumn(
      name = "category_id",
      insertable = false,
      updatable = false,
      foreignKey = @ForeignKey(value = ConstraintMode.NO_CONSTRAINT))
  private CommonMediaCategory category;

  public static CommonMedia of(long id) {
    final CommonMedia media = new CommonMedia();
    media.setId(id);
    return media;
  }
}
