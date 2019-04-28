package org.ramer.admin.system.entity.domain.common;

import org.ramer.admin.system.entity.Constant;
import org.ramer.admin.system.entity.domain.AbstractEntity;
import com.fasterxml.jackson.annotation.JsonBackReference;
import java.util.*;
import javax.persistence.Entity;
import javax.persistence.*;
import lombok.*;
import org.hibernate.annotations.Table;
import org.hibernate.annotations.*;
import org.springframework.format.annotation.*;

@Entity(name = Manager.TABLE_NAME)
@Table(appliesTo = Manager.TABLE_NAME, comment = "管理员")
@Data
@NoArgsConstructor
@ToString(exclude = {"roleses"})
@EqualsAndHashCode(callSuper = true)
public class Manager extends AbstractEntity {
  public static final String TABLE_NAME = "manager";

  @Column(nullable = false, length = 25, columnDefinition = "varchar(25) not null")
  private String empNo;

  @Column(nullable = false, length = 100, columnDefinition = "varchar(100) not null")
  private String password;

  @Column(nullable = false, length = 25, columnDefinition = "varchar(25) not null")
  private String name;

  @Column(nullable = false, columnDefinition = "int not null")
  private Integer gender;

  @Column(nullable = false, length = 11, columnDefinition = "varchar(11) not null")
  private String phone;

  @Column(length = 50, columnDefinition = "varchar(50)")
  private String avatar;
  /** 审核状态 */
  @Column(nullable = false, columnDefinition = "tinyint default 1")
  private Integer active;

  public String getActiveDesc() {
    return active == null ? "未知" : active.equals(Constant.ACTIVE_TRUE) ? "已审核" : "未审核";
  }

  @Column(columnDefinition = "datetime default null")
  @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
  private Date validDate;

  @ManyToMany(fetch = FetchType.LAZY)
  @JoinTable(
      name = "manager_roles",
      joinColumns = {@JoinColumn(name = "manager_id")},
      inverseJoinColumns = {@JoinColumn(name = "roles_id")})
  @Where(clause = "state = " + Constant.STATE_ON)
  @JsonBackReference
  private List<Roles> roleses;

  public static Manager of(Long id) {
    if (Objects.isNull(id)) {
      return null;
    }
    Manager manager = new Manager();
    manager.setId(id);
    return manager;
  }
}
