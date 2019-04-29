package org.ramer.admin.system.entity.request.common;

import org.ramer.admin.system.entity.domain.common.CommonMediaCategory;
import org.ramer.admin.system.entity.request.AbstractEntityRequest;
import org.ramer.admin.system.entity.domain.AbstractEntity;
import java.math.BigDecimal;
import java.util.Objects;
import java.util.Date;
import java.util.List;
import lombok.*;

/**
 * 通用多媒体类别.
 *
 * @author ramer
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class CommonMediaCategoryRequest extends AbstractEntityRequest {

  private String code;

  private String name;

  private String remark;

}
