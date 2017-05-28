package com.android.frame.db;

import android.text.TextUtils;
import java.util.Locale;

/**
 * 作者:李鸿浩
 * 描述:
 * 时间:2017/5/23.
 */
public class DaoUtil {

  //  Filed莫名出现这两个东西  $change serialVersionUID
  private static String[] otherFieldName = new String[]{"serialVersionUID"};
  private static String[] otherFieldNameContainsKey = new String[]{"$"};

  private DaoUtil() {
    throw new UnsupportedOperationException("禁止实例化");
  }

  public static String getTableName(Class<?> clazz) {
    return clazz.getSimpleName();
  }

  public static String getColumnType(String type) {
    if (type.contains("String")) {
      return "text";
    } else if (type.contains("int")) {
      return "integer";
    } else if (type.contains("boolean")) {
      return " boolean";
    } else if (type.contains("float")) {
      return "float";
    } else if (type.contains("double")) {
      return "double";
    } else if (type.contains("long")) {
      return "long";
    } else if (type.contains("char")) {
      return "varchar";
    } else {
      return "text";
    }
  }

  public static String capitalize(String string) {
    if (!TextUtils.isEmpty(string)) {
      return string.substring(0, 1).toUpperCase(Locale.US) + string.substring(1);
    }
    return string == null ? null : "";
  }


  /**
   * 排除不必要的字段
   *
   * @param name
   * @return true表示此字段正常  false表示该字段是包含的在排除的范围内
   */
  public static boolean checkFiled(String name) {
    boolean check = true;

    for (String otherFiled : otherFieldName) {
      if (otherFiled.equals(name)) {
        check = false;
        break;
      }
    }

    if (check) {
      for (String containsFiledKey : otherFieldNameContainsKey) {
        if (name.contains(containsFiledKey)) {
          check = false;
          break;
        }
      }

    }

    return check;

  }
}
