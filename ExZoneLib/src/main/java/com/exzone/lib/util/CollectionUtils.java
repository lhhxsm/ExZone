package com.exzone.lib.util;

import android.text.TextUtils;
import com.android.internal.util.Predicate;
import java.util.ArrayList;
import java.util.Collection;

/**
 * 作者:李鸿浩
 * 描述:集合工具类
 * 时间: 2016/10/10.
 */
public class CollectionUtils {

  /**
   * 集合是否为空,或者大小为0
   */
  public static <V> boolean isEmpty(Collection<V> source) {
    return (source == null || source.size() == 0);
  }

  /**
   * 将给定的集合转换成字符串
   *
   * @param source 给定的集合
   * @param startSymbols 开始符号
   * @param separator 分隔符
   * @param endSymbols 结束符号
   * @return 例如开始符号为"{"，分隔符为", "，结束符号为"}"，那么结果为：{1, 2, 3}
   */
  public static <T> String toString(Collection<T> source, String startSymbols, String separator,
      String endSymbols) {
    boolean addSeparator = false;
    StringBuilder sb = new StringBuilder();
    //如果开始符号不为null且不空
    if (!TextUtils.isEmpty(startSymbols)) {
      sb.append(startSymbols);
    }
    //循环所有的对象
    for (T t : source) {
      //如果需要添加分隔符
      if (addSeparator) {
        sb.append(separator);
        addSeparator = false;
      }
      sb.append(t);
      addSeparator = true;
    }
    //如果结束符号不为null且不空
    if (!TextUtils.isEmpty(endSymbols)) {
      sb.append(endSymbols);
    }
    return sb.toString();
  }

  /**
   * 将给定的集合转换成字符串
   *
   * @param source 给定的集合
   * @param separator 分隔符
   * @return 例如分隔符为", "那么结果为：1, 2, 3
   */
  public static <T> String toString(Collection<T> source, String separator) {
    return toString(source, null, separator, null);
  }

  public static <T> Collection<T> filter(Collection<T> target, Predicate<T> predicate) {
    Collection<T> result = new ArrayList<T>();
    for (T element : target) {
      if (predicate.apply(element)) {
        result.add(element);
      }
    }
    return result;
  }
}
