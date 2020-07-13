package org.ramer.admin.system.entity.domain.common;

import com.fasterxml.jackson.annotation.JsonBackReference;
import java.util.List;
import javax.persistence.*;
import lombok.*;
import org.hibernate.annotations.Table;
import org.hibernate.annotations.Where;
import org.ramer.admin.system.entity.Constant.State;
import org.ramer.admin.system.entity.domain.AbstractEntity;

/**
 * 组织.
 *
 * @author Ramer
 * @version 4/19/2019
 */
@Entity(name = Organize.TABLE_NAME)
@Table(appliesTo = Organize.TABLE_NAME, comment = "组织")
@Data
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
@ToString(exclude = {"members", "root", "prev"})
public class Organize extends AbstractEntity {
  public static final String TABLE_NAME = "organize";
  /** 名称 */
  @Column(columnDefinition = "VARCHAR(255) COMMENT '名称'")
  private String name;

  /** 直属成员 */
  @OneToMany(mappedBy = "organize")
  private List<Manager> members;

  /** 上级 */
  @Column(name = "prev_id", columnDefinition = "BIGINT(20) NULL COMMENT '上级'")
  private Long prevId;

  @ManyToOne
  @JoinColumn(
      name = "prev_id",
      insertable = false,
      updatable = false,
      foreignKey = @ForeignKey(value = ConstraintMode.NO_CONSTRAINT))
  @JsonBackReference
  private Organize prev;

  /** 顶级 */
  @Column(name = "root_id", columnDefinition = "BIGINT(20) NULL COMMENT '顶级'")
  private Long rootId;

  @ManyToOne
  @JoinColumn(
      name = "root_id",
      insertable = false,
      updatable = false,
      foreignKey = @ForeignKey(value = ConstraintMode.NO_CONSTRAINT))
  @JsonBackReference
  private Organize root;

  @Column(columnDefinition = "BIT DEFAULT 0 COMMENT '是否有子节点'")
  private Boolean hasChild = false;

  /** 备注 */
  @Column(columnDefinition = "VARCHAR(255) COMMENT '备注'")
  private String remark;

  @Column(columnDefinition = "VARCHAR(100) COMMENT '徽标'")
  private String logo;

  //  /** 所有子组织,递归,易用性考虑加入 */
  //  @ManyToMany(fetch = FetchType.LAZY)
  //  private List<Organize> children;

  /** 岗位/职位 */
  @OneToMany(mappedBy = "organizeId")
  @JsonBackReference
  @Where(clause = "state = " + State.STATE_ON)
  private List<Post> posts;

  public static Organize of(Long id) {
    Organize organize = new Organize();
    organize.setId(id);
    return organize;
  }
}
