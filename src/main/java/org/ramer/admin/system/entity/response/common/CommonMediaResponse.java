package org.ramer.admin.system.entity.response.common;

import org.ramer.admin.system.entity.domain.common.CommonMedia;
import java.util.*;
import java.util.stream.Collectors;
import lombok.*;
import org.springframework.beans.BeanUtils;
import org.springframework.util.CollectionUtils;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CommonMediaResponse {

  private Long id;
  private Integer state;
  private Date createTime;
  private Date updateTime;

  private String code;

  private String url;

  private String remark;

  private Long categoryId;

  public static CommonMediaResponse of(CommonMedia commonMedia) {
    CommonMediaResponse response = new CommonMediaResponse();
    BeanUtils.copyProperties(commonMedia, response);
    return response;
  }

  public static List<CommonMediaResponse> of(List<CommonMedia> commonMedias) {
    if (CollectionUtils.isEmpty(commonMedias)) {
      return new ArrayList<>();
    }
    return commonMedias.stream().map(CommonMediaResponse::of).collect(Collectors.toList());
  }
}
