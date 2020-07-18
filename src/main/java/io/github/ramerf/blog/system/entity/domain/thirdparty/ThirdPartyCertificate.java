package io.github.ramerf.blog.system.entity.domain.thirdparty;

import javax.persistence.Column;
import javax.persistence.Entity;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.hibernate.annotations.Table;
import io.github.ramerf.blog.system.entity.domain.AbstractEntity;

/**
 * 三方接口调用证书.
 *
 * @author ramer
 */
@Entity(name = ThirdPartyCertificate.TABLE_NAME)
@Table(appliesTo = ThirdPartyCertificate.TABLE_NAME, comment = "三方接口调用证书")
@Data
@EqualsAndHashCode(callSuper = false)
public class ThirdPartyCertificate extends AbstractEntity {
  public static final String TABLE_NAME = "third_party_certificate";

  @Column(nullable = false, length = 50, columnDefinition = "varchar(50) not null")
  private String code;

  @Column(nullable = false, columnDefinition = "varchar(255) not null")
  private String secret;

  @Column(columnDefinition = "varchar(255)")
  private String name;

  @Column(columnDefinition = "varchar(255)")
  private String remark;
}
