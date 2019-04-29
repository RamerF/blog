package org.ramer.admin.system.entity.domain.thirdparty;

import javax.persistence.*;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.hibernate.annotations.Table;
import org.ramer.admin.system.entity.domain.AbstractEntity;

/**
 * 三方系统请求日志.
 *
 * @author ramer
 */
@Entity(name = ThirdPartyLog.TABLE_NAME)
@Table(appliesTo = ThirdPartyLog.TABLE_NAME, comment = "三方系统请求日志")
@Data
@EqualsAndHashCode(callSuper = false)
public class ThirdPartyLog extends AbstractEntity {
  public static final String TABLE_NAME = "third_party_log";

  @ManyToOne
  @JoinColumn(referencedColumnName = "code")
  private ThirdPartyCertificate thirdPartyCertificate;
  /** 请求地址 */
  @Column(columnDefinition = "varchar(255)")
  private String url;
  /** 请求IP */
  @Column(columnDefinition = "varchar(255)")
  private String ip;
  /** 请求结果 */
  @Column(columnDefinition = "text")
  private String result;
}
