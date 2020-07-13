package org.ramer.admin.system.entity.domain.common;

import com.fasterxml.jackson.annotation.JsonBackReference;
import java.util.List;
import java.util.Objects;
import javax.persistence.*;
import lombok.*;
import org.hibernate.annotations.Table;
import org.hibernate.annotations.Where;
import org.ramer.admin.system.entity.Constant.State;
import org.ramer.admin.system.entity.domain.AbstractEntity;

/** 系统操作员角色. */
@Entity(name = Role.TABLE_NAME)
@Table(appliesTo = Role.TABLE_NAME, comment = "角色")
@Data
@NoArgsConstructor
@ToString(exclude = {"menus", "privileges"})
@EqualsAndHashCode(callSuper = true)
public class Role extends AbstractEntity {
  public static final String TABLE_NAME = "roles";

  @Column(nullable = false, length = 20, columnDefinition = "VARCHAR(20) NOT NULL")
  private String name;

  @Column(length = 100, columnDefinition = "VARCHAR(100)")
  private String remark;

  @ManyToMany
  @JoinTable(foreignKey = @ForeignKey(value = ConstraintMode.NO_CONSTRAINT))
  @OrderBy(value = "sort_weight DESC")
  @JsonBackReference
  @Where(clause = "state = " + State.STATE_ON)
  private List<Menu> menus;

  @Column
  @ManyToMany
  @JoinTable(foreignKey = @ForeignKey(value = ConstraintMode.NO_CONSTRAINT))
  @JsonBackReference
  @Where(clause = "state = " + State.STATE_ON)
  private List<Privilege> privileges;

  private Role(long id) {
    setId(id);
  }

  public Role(String name) {
    this.name = name;
  }

  public static Role of(Long id) {
    return Objects.isNull(id) ? null : new Role(id);
  }
}
