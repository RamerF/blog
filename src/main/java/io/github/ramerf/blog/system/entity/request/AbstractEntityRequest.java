package io.github.ramerf.blog.system.entity.request;

import java.io.Serializable;
import javax.persistence.MappedSuperclass;
import lombok.Data;
import lombok.NoArgsConstructor;
import io.github.ramerf.blog.system.entity.domain.AbstractEntity;

@Data
@MappedSuperclass
@NoArgsConstructor
public abstract class AbstractEntityRequest implements Serializable {
  private Long id;

  /**
   * Request实体转换为Domain实体的额外处理.
   *
   * @param entity 业务请求实体 {@link AbstractEntityRequest}.
   * @param domain Domain实体 {@link AbstractEntity}.
   */
  @SuppressWarnings({"unused"})
  public <T extends AbstractEntity, E extends AbstractEntityRequest> void of(E entity, T domain) {}
}
