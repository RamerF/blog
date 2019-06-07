package org.ramer.admin.system.entity.domain.common;

import java.util.*;
import javax.persistence.*;
import lombok.*;
import org.hibernate.annotations.Table;
import org.hibernate.annotations.Where;
import org.ramer.admin.system.entity.Constant;
import org.ramer.admin.system.entity.Constant.State;
import org.ramer.admin.system.entity.domain.AbstractEntity;
import org.springframework.format.annotation.DateTimeFormat;

@Entity(name = Manager.TABLE_NAME)
@Table(appliesTo = Manager.TABLE_NAME, comment = "管理员")
@Data
@NoArgsConstructor
@ToString(exclude = {"roleses"})
@EqualsAndHashCode(callSuper = true)
public class Manager extends AbstractEntity {
  public static final String TABLE_NAME = "manager";

  @Column(nullable = false, length = 25, columnDefinition = "VARCHAR(25) NOT NULL")
  private String empNo;

  @Column(nullable = false, length = 100, columnDefinition = "VARCHAR(100) NOT NULL")
  private String password;

  @Column(nullable = false, length = 25, columnDefinition = "VARCHAR(25) NOT NULL")
  private String name;

  @Column(nullable = false, columnDefinition = "INT NOT NULL")
  private Integer gender;

  @Column(nullable = false, length = 11, columnDefinition = "VARCHAR(11) NOT NULL")
  private String phone;

  @Column(length = 50, columnDefinition = "VARCHAR(50)")
  private String avatar;
  /** 审核状态 */
  @Column(nullable = false, columnDefinition = "TINYINT DEFAULT 1")
  private Integer active;

  public String getActiveDesc() {
    return active == null ? "未知" : active.equals(Constant.ACTIVE_TRUE) ? "已审核" : "未审核";
  }

  @Column(columnDefinition = "DATETIME DEFAULT NULL")
  @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
  private Date validDate;

  @ManyToMany(fetch = FetchType.LAZY)
  @JoinTable(
      name = "manager_roles",
      joinColumns = {@JoinColumn(name = "manager_id")},
      inverseJoinColumns = {@JoinColumn(name = "roles_id")})
  @Where(clause = "state = " + State.STATE_ON)
  //  @JsonBackReference
  // TODO-WARN: 这里注释掉主要是端粒段列表显示,需要修改为手动获取,然后启用该注解
  private List<Role> roleses;

  public static Manager of(Long id) {
    if (Objects.isNull(id)) {
      return null;
    }
    Manager manager = new Manager();
    manager.setId(id);
    return manager;
  }
}
