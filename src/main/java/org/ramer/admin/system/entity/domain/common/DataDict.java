package org.ramer.admin.system.entity.domain.common;

import com.fasterxml.jackson.annotation.JsonBackReference;
import javax.persistence.*;
import lombok.*;
import org.hibernate.annotations.Table;
import org.hibernate.annotations.Where;
import org.ramer.admin.system.entity.Constant.State;
import org.ramer.admin.system.entity.domain.AbstractEntity;

@Entity(name = DataDict.TABLE_NAME)
@Table(appliesTo = DataDict.TABLE_NAME, comment = "数据字典")
@Data
@ToString(exclude = {"dataDictType"})
@EqualsAndHashCode(callSuper = true)
public class DataDict extends AbstractEntity {
  public static final String TABLE_NAME = "data_dict";

  /** CODE */
  @Column(columnDefinition = "VARCHAR(25) NOT NULL COMMENT 'CODE'")
  private String code;

  /** 名称 */
  @Column(columnDefinition = "VARCHAR(25) NOT NULL COMMENT '名称'")
  private String name;

  /** 备注 */
  @Column(columnDefinition = "VARCHAR(100) COMMENT '备注'")
  private String remark;

  /** 字典类型 */
  @Column(name = "data_dict_type_id", columnDefinition = "BIGINT(20) COMMENT '字典类型'")
  private Long dataDictTypeId;

  @Where(clause = "state = " + State.STATE_ON)
  @ManyToOne
  @JoinColumn(
      name = "data_dict_type_id",
      insertable = false,
      updatable = false,
      foreignKey = @ForeignKey(value = ConstraintMode.NO_CONSTRAINT))
  @JsonBackReference
  private DataDictType dataDictType;
}
