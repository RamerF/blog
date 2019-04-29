package org.ramer.admin.system.entity.response.common;

import java.util.Objects;
import lombok.*;
import org.ramer.admin.system.entity.domain.common.Menu;
import org.springframework.beans.BeanUtils;

/**
 * 菜单.
 *
 * @author ramer
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class MenuResponse {

  private Boolean leaf;

  private String name;

  private String alia;

  private String url;

  private Integer sort;

  private String icon;

  private String remark;

  private Long parentId;

  public static MenuResponse of(final Menu menu) {
    if (Objects.isNull(menu)) {
      return null;
    }
    MenuResponse poJo = new MenuResponse();
    // TODO-WARN:  将 Domain 对象转换成 Response 对象
    BeanUtils.copyProperties(menu, poJo);
    return poJo;
  }
}
