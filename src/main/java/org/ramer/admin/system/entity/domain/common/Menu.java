package org.ramer.admin.system.entity.domain.common;

import com.fasterxml.jackson.annotation.JsonBackReference;
import java.util.Objects;
import javax.persistence.*;
import lombok.*;
import org.hibernate.annotations.Table;
import org.hibernate.annotations.Where;
import org.ramer.admin.system.entity.Constant.State;
import org.ramer.admin.system.entity.domain.AbstractEntity;

@Entity(name = Menu.TABLE_NAME)
@Table(appliesTo = Menu.TABLE_NAME, comment = "菜单")
@Data
@AllArgsConstructor
@Builder
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class Menu extends AbstractEntity {
  public static final String TABLE_NAME = "menu";

  /** 名称 */
  @Column(columnDefinition = "VARCHAR(25) COMMENT '名称'")
  private String name;
  /** 菜单别名,用于权限表达式 */
  @Column(columnDefinition = "VARCHAR(100) NOT NULL COMMENT '菜单别名,用于权限表达式'")
  private String alia;
  /** 地址 */
  @Column(columnDefinition = "VARCHAR(100) COMMENT '地址'")
  private String url;
  /** 排序权重 */
  @Column(columnDefinition = "TINYINT(4) COMMENT '排序权重'")
  @Builder.Default
  private Integer sortWeight = 0;
  /** ICON FONT 图标 */
  @Column(columnDefinition = "VARCHAR(25) DEFAULT 'people' COMMENT 'ICON FONT图标'")
  @Builder.Default
  private String icon = "people";

  @Column(columnDefinition = "VARCHAR(100) COMMENT '备注'")
  private String remark;

  @Column(name = "parent_id")
  private Long parentId;

  /** 是否最终节点 */
  @Column(columnDefinition = "BIT DEFAULT 0 COMMENT '是否有子节点'")
  @Builder.Default
  private Boolean hasChild = false;

  @ManyToOne
  @JoinColumn(name = "parent_id", insertable = false, updatable = false)
  @JsonBackReference
  @Where(clause = "state = " + State.STATE_ON)
  private Menu parent;

  public static Menu of(Long id) {
    if (Objects.isNull(id)) {
      return null;
    }
    final Menu menu = new Menu();
    menu.setId(id);
    return menu;
  }
}
