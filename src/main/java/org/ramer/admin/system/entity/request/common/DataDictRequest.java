package org.ramer.admin.system.entity.request.common;

import org.ramer.admin.system.entity.domain.common.DataDict;
import org.ramer.admin.system.entity.request.AbstractEntityRequest;
import org.ramer.admin.system.entity.domain.AbstractEntity;
import java.math.BigDecimal;
import java.util.Objects;
import java.util.Date;
import java.util.List;
import lombok.*;

/**
 * 数据字典.
 *
 * @author ramer
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class DataDictRequest extends AbstractEntityRequest {

  private String name;

  private String code;

  private String remark;

  private Long dataDictTypeId;

}
