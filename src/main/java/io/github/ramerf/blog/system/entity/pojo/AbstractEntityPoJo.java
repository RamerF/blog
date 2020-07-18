package io.github.ramerf.blog.system.entity.pojo;

import java.io.Serializable;
import java.util.Date;
import java.util.Objects;
import javax.persistence.MappedSuperclass;
import lombok.Data;
import lombok.NoArgsConstructor;
import io.github.ramerf.blog.system.entity.domain.AbstractEntity;
import org.springframework.beans.BeanUtils;

@Data
@MappedSuperclass
@NoArgsConstructor
public abstract class AbstractEntityPoJo implements Serializable {
  private Long id;
  private Boolean isDelete;
  private Date createTime;
  private Date updateTime;

  public <T extends AbstractEntity, E extends AbstractEntityPoJo> E of(T entity, Class<E> clazz) {
    if (Objects.isNull(entity)) {
      return null;
    }
    E poJo;
    try {
      poJo = clazz.newInstance();
    } catch (Exception e) {
      return null;
    }
    BeanUtils.copyProperties(entity, poJo);
    return poJo;
  }
}
