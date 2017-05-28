package com.android.lib;

import java.nio.charset.Charset;

/**
 * 常量
 */

public class Constants {
  public static final Charset US_ASCII = Charset.forName("US-ASCII");
  public static final Charset UTF_8 = Charset.forName("UTF-8");
  public static final String TAG = "tag";//debug默认标志
  public static final boolean DEBUG = BuildConfig.DEBUG;//debug默认开启
}
