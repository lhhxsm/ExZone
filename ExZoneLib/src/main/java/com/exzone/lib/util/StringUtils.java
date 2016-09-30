package com.exzone.lib.util;

import android.text.TextUtils;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 作者:李鸿浩
 * 描述:
 * 时间:2016/7/9.
 */
public class StringUtils {

    private StringUtils() {
        throw new AssertionError();
    }

    /**
     * 如果字符串为空或它的大小为0,或者是有空格,返回true,否则返回false.
     */
    public static boolean isBlank(String str) {
        return (str == null || str.trim().length() == 0);
    }


    /**
     * 两个字符串比较
     */
    public static boolean isEquals(String actual, String expected) {
        return actual.equals(expected);
    }

    /**
     * 字符串长度
     */
    public static int length(CharSequence str) {
        return str == null ? 0 : str.length();
    }

    /**
     * 字符串链接转换为字符串HTML
     */
    public static String getHtml(String href) {
        if (TextUtils.isEmpty(href)) {
            return "";
        }

        String hrefReg = ".*<[\\s]*a[\\s]*.*>(.+?)<[\\s]*/a[\\s]*>.*";
        Pattern hrefPattern = Pattern.compile(hrefReg, Pattern.CASE_INSENSITIVE);
        Matcher hrefMatcher = hrefPattern.matcher(href);
        if (hrefMatcher.matches()) {
            return hrefMatcher.group(1);
        }
        return href;
    }
}
