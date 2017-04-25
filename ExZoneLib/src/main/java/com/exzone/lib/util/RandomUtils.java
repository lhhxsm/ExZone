package com.exzone.lib.util;

import android.text.TextUtils;
import java.util.Random;

/**
 * 作者:李鸿浩
 * 描述:随机工具类
 * 时间:2016/7/9.
 */
public class RandomUtils {
  public static final String NUMBERS_AND_LETTERS =
      "0123456789abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ";
  public static final String NUMBERS = "0123456789";
  public static final String LETTERS = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ";
  public static final String CAPITAL_LETTERS = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";
  public static final String LOWER_CASE_LETTERS = "abcdefghijklmnopqrstuvwxyz";

  private RandomUtils() {
    throw new AssertionError();
  }

  /**
   * 获取随机字符串,字符串由数字和大小写字母组成
   *
   * @param length 字符串长度
   */
  public static String getRandomNumbersAndLetters(int length) {
    return getRandom(NUMBERS_AND_LETTERS, length);
  }

  /**
   * 获取随机字符串,字符串由数字组成
   *
   * @param length 字符串长度
   */
  public static String getRandomNumbers(int length) {
    return getRandom(NUMBERS, length);
  }

  /**
   * 获取随机字符串,字符串由大小写字母组成
   *
   * @param length 字符串长度
   */
  public static String getRandomLetters(int length) {
    return getRandom(LETTERS, length);
  }

  /**
   * 获取随机字符串,字符串由大写字母组成
   *
   * @param length 字符串长度
   */
  public static String getRandomCapitalLetters(int length) {
    return getRandom(CAPITAL_LETTERS, length);
  }

  /**
   * 获取随机字符串,字符串由小写字母组成
   *
   * @param length 字符串长度
   */
  public static String getRandomLowerCaseLetters(int length) {
    return getRandom(LOWER_CASE_LETTERS, length);
  }

  /**
   * 获取随机字符串
   *
   * @param source 数据源
   * @param length 字符串长度
   * @return 如果数据源为空则返回空
   */
  public static String getRandom(String source, int length) {
    return TextUtils.isEmpty(source) ? null : getRandom(source.toCharArray(), length);
  }

  /**
   * 获取随机字符串
   *
   * @param sourceChar 数据源
   * @param length 字符串长度
   * @return 如果数据源为空则返回空
   */
  public static String getRandom(char[] sourceChar, int length) {
    if (sourceChar == null || sourceChar.length == 0 || length < 0) {
      return null;
    }

    StringBuilder str = new StringBuilder(length);
    Random random = new Random();
    for (int i = 0; i < length; i++) {
      str.append(sourceChar[random.nextInt(sourceChar.length)]);
    }
    return str.toString();
  }

  /**
   * 获取0到最大值之间的随机值
   *
   * @param max 最大值
   * @return 如果max<=0, 则返回0.
   */
  public static int getRandom(int max) {
    return getRandom(0, max);
  }

  /**
   * 获取min到max之间的随机值
   *
   * @param min 最小值
   * @param max 最大值
   * @return 如果min>max, 则返回0.如果min=max, 则返回min
   */
  public static int getRandom(int min, int max) {
    if (min > max) {
      return 0;
    }
    if (min == max) {
      return min;
    }
    return min + new Random().nextInt(max - min);
  }

  /**
   * 随机置换使用随机的默认源指定的数组
   */
  public static boolean shuffle(Object[] objArray) {
    if (objArray == null) {
      return false;
    }
    return shuffle(objArray, getRandom(objArray.length));
  }

  /**
   * 随机置换指定数组
   */
  public static boolean shuffle(Object[] objArray, int shuffleCount) {
    int length;
    if (objArray == null || shuffleCount < 0 || (length = objArray.length) < shuffleCount) {
      return false;
    }

    for (int i = 1; i <= shuffleCount; i++) {
      int random = getRandom(length - i);
      Object temp = objArray[length - i];
      objArray[length - i] = objArray[random];
      objArray[random] = temp;
    }
    return true;
  }

  /**
   * 随机置换使用随机的默认源指定的int数组
   */
  public static int[] shuffle(int[] intArray) {
    if (intArray == null) {
      return null;
    }
    return shuffle(intArray, getRandom(intArray.length));
  }

  /**
   * 随机置换指定的int数组
   */
  public static int[] shuffle(int[] intArray, int shuffleCount) {
    int length;
    if (intArray == null || shuffleCount < 0 || (length = intArray.length) < shuffleCount) {
      return null;
    }

    int[] out = new int[shuffleCount];
    for (int i = 1; i <= shuffleCount; i++) {
      int random = getRandom(length - i);
      out[i - 1] = intArray[random];
      int temp = intArray[length - i];
      intArray[length - i] = intArray[random];
      intArray[random] = temp;
    }
    return out;
  }
}
