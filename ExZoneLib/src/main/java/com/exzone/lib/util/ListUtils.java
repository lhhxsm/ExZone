package com.exzone.lib.util;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import java.util.ArrayList;
import java.util.List;

/**
 * 作者:李鸿浩
 * 描述:集合工具类
 * 时间:2016/7/9.
 */
public class ListUtils {

  private ListUtils() {
    throw new AssertionError();
  }

  /**
   * 集合的大小
   */
  public static <T> int getSize(List<T> sourceList) {
    return sourceList == null ? 0 : sourceList.size();
  }

  /**
   * 集合是否为空,或者大小为0
   */
  public static <T> boolean isEmpty(List<T> sourceList) {
    return (sourceList == null || sourceList.size() == 0);
  }

  /**
   * 集合是否相等
   */
  public static <T> boolean isEquals(ArrayList<T> actual, ArrayList<T> expected) {
    if (actual == null) {
      return expected == null;
    }
    if (expected == null) {
      return false;
    }
    if (actual.size() != expected.size()) {
      return false;
    }

    for (int i = 0; i < actual.size(); i++) {
      if (actual.get(i) != expected.get(i)) {
        return false;
      }
    }
    return true;
  }

  /**
   * 将`x`添加到列表xs的开头。
   */
  public static <T> List<T> prepend(final @NonNull List<T> xs, final @NonNull T x) {
    final List<T> ys = new ArrayList<>(xs);
    ys.add(0, x);
    return ys;
  }

  /**
   * 根据索引`idx`替换元素为`x`。这样做通过返回一个新的列表，而不改变原来的列表。
   */
  public static @NonNull <T> List<T> replaced(final @NonNull List<T> xs, final int idx,
      final @Nullable T x) {
    final List<T> ys = new ArrayList<>(xs);
    ys.set(idx, x);
    return ys;
  }

  /**
   * 使用Fisher-Yates算法重新排列数组而不改变数组内容。
   * http://www.dotnetperls.com/shuffle-java
   */
  public static <T> List<T> shuffle(final @NonNull List<T> xs) {
    final List<T> ys = new ArrayList<>(xs);
    final int length = ys.size();

    for (int i = 0; i < length; i++) {
      final int j = i + (int) (Math.random() * (length - i));
      final T temp = ys.get(i);
      ys.set(i, ys.get(j));
      ys.set(j, temp);
    }

    return ys;
  }

  /**
   * 将`x`添加到列表xs的末尾。列表xs不发生改变,返回一个新的列表ys
   */
  public static @NonNull <T> List<T> append(final @NonNull List<T> xs, final @NonNull T x) {
    final List<T> ys = new ArrayList<>(xs);
    ys.add(x);
    return ys;
  }

  /**
   * 返回一个新的列表，其中`xs`中的所有元素都等于`x`替换为`news`。
   */
  public static @NonNull <T> List<T> allReplaced(final @NonNull List<T> xs, final @NonNull T x,
      final @NonNull T news) {
    final List<T> ys = new ArrayList<>(xs);
    for (int idx = 0; idx < xs.size(); idx++) {
      if (x.equals(xs.get(idx))) {
        ys.set(idx, news);
      }
    }
    return ys;
  }

  /**
   * 添加不同的条目到列表
   *
   * @return 如果entry在sourceList中已存在，则返回false，否则添加它并返回true。
   */
  public static <T> boolean addDistinctEntry(List<T> sourceList, T entry) {
    return (sourceList != null && !sourceList.contains(entry)) && sourceList.add(entry);
  }

  /**
   * 从entryList向sourceList添加所有不同的条目
   *
   * @return 添加条目的个数
   */
  public static <T> int addDistinctList(List<T> sourceList, List<T> entryList) {
    if (sourceList == null || isEmpty(entryList)) {
      return 0;
    }

    int sourceCount = sourceList.size();
    for (T entry : entryList) {
      if (!sourceList.contains(entry)) {
        sourceList.add(entry);
      }
    }
    return sourceList.size() - sourceCount;
  }

  /**
   * 从ys向xs添加所有的条目,xs和ys列表都不会发生改变,返回新的列表zs
   */
  public static <T> List<T> concat(final @NonNull List<T> xs, final @NonNull List<T> ys) {
    final List<T> zs = new ArrayList<>(xs);
    ListUtils.mutatingConcat(zs, ys);
    return zs;
  }

  /**
   * 从ys向xs添加所有不同的条目,xs和ys列表都不会发生改变,返回新的列表zs
   */
  public static <T> List<T> concatDistinct(final @NonNull List<T> xs, final @NonNull List<T> ys) {
    final List<T> zs = new ArrayList<>(xs);
    ListUtils.mutatingConcatDistinct(zs, ys);
    return zs;
  }

  /**
   * 从ys向xs添加所有的条目,xs列表会发生改变
   */
  public static <T> List<T> mutatingConcat(final @NonNull List<T> xs, final @NonNull List<T> ys) {
    xs.addAll(ys);
    return xs;
  }

  /**
   * 从ys向xs添加所有不同的条目,xs列表会发生改变
   */
  public static <T> List<T> mutatingConcatDistinct(final @NonNull List<T> xs,
      final @NonNull List<T> ys) {
    for (final T y : ys) {
      if (!xs.contains(y)) {
        xs.add(y);
      }
    }
    return xs;
  }

  /**
   * 删除列表中的重复条目
   *
   * @return 删除条目的个数
   */
  public static <T> int distinctList(List<T> sourceList) {
    if (isEmpty(sourceList)) {
      return 0;
    }

    int sourceCount = sourceList.size();
    int sourceListSize = sourceList.size();
    for (int i = 0; i < sourceListSize; i++) {
      for (int j = (i + 1); j < sourceListSize; j++) {
        if (sourceList.get(i).equals(sourceList.get(j))) {
          sourceList.remove(j);
          sourceListSize = sourceList.size();
          j--;
        }
      }
    }
    return sourceCount - sourceList.size();
  }

  /**
   * 向列表中添加不为null的条目
   */
  public static <T> boolean addListNotNullValue(List<T> sourceList, T value) {
    return (sourceList != null && value != null) && sourceList.add(value);
  }

  /**
   * 反转列表
   */
  public static <T> List<T> invertList(List<T> sourceList) {
    if (isEmpty(sourceList)) {
      return sourceList;
    }

    List<T> invertList = new ArrayList<T>(sourceList.size());
    for (int i = sourceList.size() - 1; i >= 0; i--) {
      invertList.add(sourceList.get(i));
    }
    return invertList;
  }

  /**
   * Combines a list of lists into a new single, flat list.
   */
  public static @NonNull <T> List<T> flatten(final @NonNull List<List<T>> xss) {
    final List<T> result = new ArrayList<>();
    for (final List<T> xs : xss) {
      result.addAll(xs);
    }
    return result;
  }
}
