package org.ramer.admin.system.entity.domain.common;

import com.fasterxml.jackson.annotation.JsonBackReference;
import java.util.List;
import javax.persistence.*;
import lombok.*;
import org.hibernate.annotations.Table;
import org.hibernate.annotations.Where;
import org.ramer.admin.system.entity.Constant.State;
import org.ramer.admin.system.entity.domain.AbstractEntity;

@Entity(name = DataDictType.TABLE_NAME)
@Table(appliesTo = DataDictType.TABLE_NAME, comment = "数据字典类别")
@Data
@AllArgsConstructor
@NoArgsConstructor
@ToString(exclude = {"dataDicts"})
@EqualsAndHashCode(callSuper = true)
public class DataDictType extends AbstractEntity {
  public static final String TABLE_NAME = "data_dict_type";

  @Column(columnDefinition = "VARCHAR(25) NOT NULL UNIQUE")
  private String code;

  @Column(columnDefinition = "VARCHAR(25) NOT NULL")
  private String name;

  @Column(columnDefinition = "VARCHAR(100)")
  private String remark;

  @Where(clause = "state = " + State.STATE_ON)
  @OneToMany(mappedBy = "dataDictType")
  @JsonBackReference
  private List<DataDict> dataDicts;
}
