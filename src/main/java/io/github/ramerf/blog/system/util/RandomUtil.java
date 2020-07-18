package io.github.ramerf.blog.system.util;

import java.util.Random;

/**
 * 随机数工具.
 *
 * @author ramer
 * @since 2019/11/19
 */
public class RandomUtil {
  /**
   * 生成指定长度随机数字
   *
   * @param length 长度
   */
  public static String random(int length) {
    StringBuilder str = new StringBuilder();
    Random random = new Random();
    for (int i = 0; i < length; i++) {
      str.append(random.nextInt(10));
    }
    return str.toString();
  }
}
