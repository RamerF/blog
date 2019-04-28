package org.ramer.admin.system.entity.domain.common;

import org.ramer.admin.system.entity.domain.AbstractEntity;
import org.ramer.admin.system.entity.Constant;
import com.fasterxml.jackson.annotation.JsonBackReference;
import javax.persistence.*;
import lombok.*;
import org.hibernate.annotations.Where;
import org.hibernate.annotations.Table;

@Entity(name = Menu.TABLE_NAME)
@Table(appliesTo = Menu.TABLE_NAME, comment = "菜单")
@Data
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class Menu extends AbstractEntity {
  public static final String TABLE_NAME = "menu";

  @ManyToOne
  @JoinColumn(name = "pid")
  @JsonBackReference
  @Where(clause = "state = " + Constant.STATE_ON)
  private Menu parent;
  /** 是否最终节点 */
  @Column(columnDefinition = "bit  comment '是否最终节点'")
  private Boolean leaf;

  @Column(length = 25, columnDefinition = "varchar(25)")
  private String name;
  /** 菜单别名,用于权限表达式 */
  @Column(
      nullable = false,
      length = 100,
      columnDefinition = "varchar(100) not null comment '菜单别名,用于权限表达式'")
  private String alia;

  @Column(length = 100, columnDefinition = "varchar(100)")
  private String url;
  /** 显示顺序 */
  @Column(length = 11, columnDefinition = "int(11)")
  private Integer sort;
  /** ICON FONT 图标 */
  @Column(length = 25, columnDefinition = "varchar(25) default null comment 'ICON FONT图标'")
  private String icon;

  @Column(length = 100, columnDefinition = "varchar(100)")
  private String remark;
  //    @JoinColumn(name = "menu_id")
  //    @OneToMany(fetch = FetchType.EAGER)
  //    private List<Privilege> privileges;

  public static Menu of(long id) {
    final Menu menu = new Menu();
    menu.setId(id);
    return menu;
  }
}
