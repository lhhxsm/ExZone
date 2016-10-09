package com.exzone.lib.util;

import android.text.TextUtils;

/**
 * 作者:李鸿浩
 * 描述:数组工具类
 * 时间:2016/7/9.
 */
public class ArrayUtils {
    private ArrayUtils() {
        throw new AssertionError();
    }

    /**
     * 数组是否为空,或者大小为0
     */
    public static <V> boolean isEmpty(V[] source) {
        return (source == null || source.length == 0);
    }

    /**
     * 在数组source中搜索元素element
     *
     * @param source  待操作的数组
     * @param element 待匹配的元素
     * @return 索引，如不存在,-1
     */
    public static <V> int search(V[] source, V element) {
        for (int i = 0; i < source.length; i++) {
            if (element.equals(source[i])) {
                return i;
            }
        }
        return -1;
    }

    /**
     * 将给定的数组转换成字符串
     *
     * @param source       给定的数组
     * @param startSymbols 开始符号
     * @param separator    分隔符
     * @param endSymbols   结束符号
     * @return 例如开始符号为"{"，分隔符为", "，结束符号为"}"，那么结果为：{1, 2, 3}
     */
    public static <V> String toString(V[] source, String startSymbols, String separator, String endSymbols) {
        boolean addSeparator = false;
        StringBuilder sb = new StringBuilder();
        //如果开始符号不为null且不空
        if (!TextUtils.isEmpty(startSymbols)) {
            sb.append(startSymbols);
        }
        //循环所有的对象
        for (V v : source) {
            //如果需要添加分隔符
            if (addSeparator) {
                sb.append(separator);
                addSeparator = false;
            }
            sb.append(v);
            addSeparator = true;
        }

        //如果结束符号不为null且不空
        if (!TextUtils.isEmpty(endSymbols)) {
            sb.append(endSymbols);
        }
        return sb.toString();
    }

    /**
     * 将给定的数组转换成字符串
     *
     * @param source    给定的数组
     * @param separator 分隔符
     * @return 例如分隔符为", "那么结果为：1, 2, 3
     */
    public static <V> String toString(V[] source, String separator) {
        return toString(source, null, separator, null);
    }

}
