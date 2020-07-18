package io.github.ramerf.blog.system.util;

import java.util.*;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Collectors;
import javax.annotation.Nonnull;

public class CollectionUtils {

  /**
   * 将List对象转换成其它List对象.
   *
   * @param lists 对象集合
   * @param function 转换函数
   * @param filterFunction 过滤函数
   * @param <T> 待转换对象
   * @param <E> 目标对象
   */
  public static <T, E> List<E> list(
      @Nonnull final List<T> lists,
      final Function<T, E> function,
      final Predicate<E> filterFunction,
      Comparator<? super E> comparator) {
    return Objects.isNull(filterFunction)
        ? Objects.isNull(comparator)
            ? lists.stream().map(function).collect(Collectors.toList())
            : lists.stream().map(function).sorted(comparator).collect(Collectors.toList())
        : Objects.isNull(comparator)
            ? lists.stream().map(function).filter(filterFunction).collect(Collectors.toList())
            : lists.stream()
                .map(function)
                .filter(filterFunction)
                .sorted(comparator)
                .collect(Collectors.toList());
  }
}
