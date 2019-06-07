package org.ramer.admin.system.entity.response;

import java.io.Serializable;
import java.util.Date;
import javax.persistence.MappedSuperclass;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@MappedSuperclass
@NoArgsConstructor
public abstract class AbstractEntityResponse implements Serializable {
  private Long id;
  private Date createTime;
  private Date updateTime;
}
