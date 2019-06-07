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
@EqualsAndHashCode(callSuper = true)
@NoArgsConstructor
public class DataDict extends AbstractEntity {
  public static final String TABLE_NAME = "data_dict";

  @Where(clause = "state = " + State.STATE_ON)
  @ManyToOne
  @JoinColumn(name = "data_dict_type_id")
  @JsonBackReference
  private DataDictType dataDictType;

  @Column(nullable = false, length = 25, columnDefinition = "VARCHAR(25) NOT NULL")
  private String name;

  @Column(nullable = false, length = 25, columnDefinition = "VARCHAR(25) NOT NULL")
  private String code;

  @Column(length = 100, columnDefinition = "VARCHAR(100)")
  private String remark;
}
