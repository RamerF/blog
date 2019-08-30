package org.ramer.admin.system.entity.domain.common;

import com.fasterxml.jackson.annotation.JsonBackReference;
import javax.persistence.*;
import lombok.*;
import org.hibernate.annotations.Table;
import org.ramer.admin.system.entity.domain.AbstractEntity;

/**
 * 组织关系.
 *
 * @author Ramer @Date 6/30/2019
 */
@Entity(name = OrganizeRelation.TABLE_NAME)
@Table(appliesTo = OrganizeRelation.TABLE_NAME, comment = "组织关系")
@Data
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
@ToString
public class OrganizeRelation extends AbstractEntity {
  public static final String TABLE_NAME = "organize_relation";

  @Column(name = "prev_id", columnDefinition = "bigint(20) COMMENT 'left'")
  private Long prevId;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "prev_id", insertable = false, updatable = false)
  @JsonBackReference
  private Organize prev;

  @Column(name = "next_id", columnDefinition = "bigint(20) COMMENT 'right'")
  private Long nextId;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "next_id", insertable = false, updatable = false)
  @JsonBackReference
  private Organize next;

  @Column(columnDefinition = "tinyint(4) COMMENT 'left与right的距离'")
  private Integer distance;

  public static OrganizeRelation of(final long prevId, final long nextId, final Integer distance) {
    OrganizeRelation organizeRelation = new OrganizeRelation();
    organizeRelation.setPrevId(prevId);
    organizeRelation.setNextId(nextId);
    organizeRelation.setDistance(distance);
    return organizeRelation;
  }
}
