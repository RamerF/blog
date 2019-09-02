package org.ramer.admin.system.entity.domain.common;

import com.fasterxml.jackson.annotation.JsonBackReference;
import java.util.List;
import java.util.Objects;
import javax.persistence.*;
import lombok.*;
import org.hibernate.annotations.Table;
import org.hibernate.annotations.Where;
import org.ramer.admin.system.entity.Constant;
import org.ramer.admin.system.entity.Constant.State;
import org.ramer.admin.system.entity.domain.AbstractEntity;

@Entity(name = Manager.TABLE_NAME)
@Table(appliesTo = Manager.TABLE_NAME, comment = "管理员")
@Data
@NoArgsConstructor
@ToString(exclude = {"roles", "post", "organize"})
@EqualsAndHashCode(callSuper = true)
public class Manager extends AbstractEntity {
  public static final String TABLE_NAME = "manager";

  /** 工号 */
  @Column(columnDefinition = "VARCHAR(25) NOT NULL COMMENT '工号'")
  private String empNo;

  /** 密码 */
  @Column(columnDefinition = "VARCHAR(100) NOT NULL COMMENT '密码'")
  private String password;

  /** 姓名 */
  @Column(columnDefinition = "VARCHAR(10) NOT NULL COMMENT '姓名'")
  private String name;

  /** 性别{@link Constant.Gender} */
  @Column(columnDefinition = "TINYINT(4) DEFAULT NULL COMMENT '性别'")
  private Integer gender;

  /** 联系电话 */
  @Column(columnDefinition = "VARCHAR(11) NOT NULL COMMENT '联系电话'")
  private String phone;

  /** 头图: 暂未启用 */
  @Column(length = 50, columnDefinition = "VARCHAR(50) COMMENT '头像'")
  private String avatar;

  /** 审核状态: 暂未启用 */
  @Column(columnDefinition = "BIT DEFAULT 0 COMMENT '审核状态'")
  private Boolean isActive = false;

  @ManyToMany
  @JoinTable
  @JsonBackReference
  @Where(clause = "state = " + State.STATE_ON)
  private List<Role> roles;

  @Column(name = "post_id")
  private Long postId;

  @ManyToOne
  @JoinColumn(name = "post_id", insertable = false, updatable = false)
  @Where(clause = "state = " + State.STATE_ON)
  @JsonBackReference
  private Post post;

  @Column(name = "organize_id")
  private Long organizeId;

  @ManyToOne
  @JoinColumn(name = "organize_id", insertable = false, updatable = false)
  @Where(clause = "state = " + State.STATE_ON)
  @JsonBackReference
  private Organize organize;

  public static Manager of(Long id) {
    if (Objects.isNull(id)) {
      return null;
    }
    Manager manager = new Manager();
    manager.setId(id);
    return manager;
  }
}
