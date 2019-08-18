package org.ramer.admin.system.entity.domain.common;

import com.fasterxml.jackson.annotation.JsonBackReference;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import javax.persistence.*;
import lombok.*;
import org.hibernate.annotations.Table;
import org.ramer.admin.system.entity.domain.AbstractEntity;

/**
 * 岗位/职位.
 *
 * @author Ramer @Date 2019/8/17
 */
@Entity(name = Post.TABLE_NAME)
@Table(appliesTo = Post.TABLE_NAME, comment = "岗位")
@Data
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
@ToString(exclude = {"organize"})
public class Post extends AbstractEntity {
  public static final String TABLE_NAME = "post";
  /** 名称 */
  @Column(columnDefinition = "VARCHAR(255) COMMENT '名称'")
  private String name;

  /** 数据访问权限{@link DataAccess} */
  @Column(columnDefinition = "TINYINT(2) COMMENT '数据访问权限'")
  private Integer dataAccess;

  @Column(name = "organize_id")
  private Long organizeId;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "organize_id", insertable = false, updatable = false)
  @JsonBackReference
  private Organize organize;

  /** 数据访问权限 */
  public enum DataAccess {
    OWN("仅自己"),
    OWN_CHILD_ORGANIZE("自己以及下属岗位"),
    OWN_EQUAL_CHILD_ORGANIZE("相同岗位以及下属岗位"),
    BELONG_ORGANIZE_RETRIEVE("当前组织"),
    ALL_ORGANIZE("所有");

    private static Map<Integer, String> map =
        Stream.of(DataAccess.values()).collect(Collectors.toMap(Enum::ordinal, o -> o.desc));
    private static List<Integer> list =
        Stream.of(DataAccess.values()).map(Enum::ordinal).collect(Collectors.toList());
    private String desc;

    DataAccess(String desc) {
      this.desc = desc;
    }

    @Override
    public String toString() {
      return desc;
    }

    public static Map<Integer, String> map() {
      return map;
    }

    public static List<Integer> list() {
      return list;
    }
  }
}
