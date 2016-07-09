package com.exzone.lib.util;

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
    public static <V> boolean isEmpty(V[] sourceArray) {
        return (sourceArray == null || sourceArray.length == 0);
    }
}
