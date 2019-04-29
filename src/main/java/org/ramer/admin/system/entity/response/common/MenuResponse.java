package org.ramer.admin.system.entity.response.common;

import java.util.List;
import lombok.*;
import org.ramer.admin.system.entity.domain.common.Menu;
import org.ramer.admin.system.entity.pojo.common.MenuPoJo;
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
  private Long id;

  private Boolean leaf;

  private String name;

  private String alia;

  private String url;

  private Integer sort;

  private String icon;

  private String remark;

  private Long parentId;

  private List<MenuResponse> children;

  public static MenuResponse of(MenuPoJo menuPoJo) {
    MenuResponse response = new MenuResponse();
    BeanUtils.copyProperties(menuPoJo, response);
    return response;
  }

  public static MenuResponse of(Menu menu) {
    MenuResponse response = new MenuResponse();
    BeanUtils.copyProperties(menu, response);
    return response;
  }
}
