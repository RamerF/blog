package org.ramer.admin.system.entity.domain.common;

import org.ramer.admin.system.entity.Constant;
import org.ramer.admin.system.entity.domain.AbstractEntity;
import com.fasterxml.jackson.annotation.JsonBackReference;
import java.util.List;
import javax.persistence.*;
import lombok.*;
import org.hibernate.annotations.Where;
import org.hibernate.annotations.Table;

/** 系统操作员角色. */
@Entity(name = Roles.TABLE_NAME)
@Table(appliesTo = Roles.TABLE_NAME, comment = "角色")
@Data
@NoArgsConstructor
@ToString(exclude = {"menus", "privileges"})
@EqualsAndHashCode(callSuper = true)
public class Roles extends AbstractEntity {
  public static final String TABLE_NAME = "roles";

  @Column(nullable = false, length = 20, columnDefinition = "VARCHAR(20) NOT NULL")
  private String name;

  @Column(length = 100, columnDefinition = "VARCHAR(100)")
  private String remark;

  @Where(clause = "state = " + Constant.STATE_ON)
  @ManyToMany
  @JoinTable(
      name = "roles_menu",
      joinColumns = {@JoinColumn(name = "roles_id")},
      inverseJoinColumns = {@JoinColumn(name = "menu_id")})
  @OrderBy(value = "id")
  @JsonBackReference
  private List<Menu> menus;

  @Column
  @ManyToMany
  @JoinTable(
      name = "roles_privileges",
      joinColumns = @JoinColumn(name = "roles_id", referencedColumnName = "id"),
      inverseJoinColumns = @JoinColumn(name = "privilege_id", referencedColumnName = "id"))
  private List<Privilege> privileges;

  private Roles(long id) {
    setId(id);
  }

  public Roles(String name) {
    this.name = name;
  }

  public static Roles of(long id) {
    return new Roles(id);
  }
}
