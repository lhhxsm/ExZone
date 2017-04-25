package com.exzone.lib.util;

import java.text.DecimalFormat;

/**
 * 作者:李鸿浩
 * 描述:数学相关的工具类
 * 时间: 2016/10/10.
 */
public class MathUtils {

  /**
   * 勾股定理
   */
  public static double pythagoras(double value1, double value2) {
    return Math.sqrt((value1 * value1) + (value2 * value2));
  }

  /**
   * 计算百分比
   *
   * @param dividend 被除数
   * @param divisor 除数
   * @param pointLength 小数点位数
   * @param zero 当小数点位数不足时,是否使用0代替
   * @param percent 是否显示字符串默认的百分号(%) true显示 false不显示
   */
  public static String percent(double dividend, double divisor, int pointLength, boolean zero,
      boolean percent) {
    StringBuilder sb = new StringBuilder();
    sb.append("#");
    if (pointLength > 0) {
      sb.append(".");
    }
    for (int i = 0; i < pointLength; i++) {
      sb.append(zero ? "0" : "#");
    }
    sb.append("%");
    if (sb.length() > 0) {
      String result = new DecimalFormat(sb.toString()).format(dividend / divisor);
      if (!percent && result.length() > 0) {
        result = result.substring(0, result.length() - 1);
      }
      return result;
    } else {
      return null;
    }
  }

  /**
   * 计算百分比,默认显示的百分号(%)
   *
   * @param dividend 被除数
   * @param divisor 除数
   * @param pointLength 小数点位数
   * @param zero 当小数点位数不足时,是否使用0代替
   */
  public static String percent(double dividend, double divisor, int pointLength, boolean zero) {
    return percent(dividend, divisor, pointLength, zero, true);
  }

  /**
   * 计算百分比,默认显示的百分号(%)、小数点位数不足的时候默认不使用0代替
   *
   * @param dividend 被除数
   * @param divisor 除数
   * @param pointLength 小数点位数
   * @return 25.22%
   */
  public static String percent(double dividend, double divisor, int pointLength) {
    return percent(dividend, divisor, pointLength, false, true);
  }

  /**
   * 计算百分比,默认显示的百分号(%)、没有小数
   *
   * @param dividend 被除数
   * @param divisor 除数
   * @return 25%
   */
  public static String percent(double dividend, double divisor) {
    return percent(dividend, divisor, 0, false, true);
  }
}
