package org.ramer.admin.system.util;

import java.util.*;
import org.springframework.beans.BeanWrapper;
import org.springframework.beans.BeanWrapperImpl;

public class BeanUtils {

  /** 获取对象属性值为空的属性名 */
  public static String[] getNullPropertyNames(Object obj) {
    final BeanWrapper src = new BeanWrapperImpl(obj);
    java.beans.PropertyDescriptor[] pds = src.getPropertyDescriptors();
    Set<String> nullNames = new HashSet<>();
    for (java.beans.PropertyDescriptor pd : pds) {
      if (Objects.isNull(src.getPropertyValue(pd.getName()))) nullNames.add(pd.getName());
    }
    return nullNames.toArray(new String[0]);
  }
}
