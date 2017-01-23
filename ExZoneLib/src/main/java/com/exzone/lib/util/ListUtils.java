package com.exzone.lib.util;

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
    public static <V> int getSize(List<V> sourceList) {
        return sourceList == null ? 0 : sourceList.size();
    }

    /**
     * 集合是否为空,或者大小为0
     */
    public static <V> boolean isEmpty(List<V> sourceList) {
        return (sourceList == null || sourceList.size() == 0);
    }

    /**
     * 集合是否相等
     */
    public static <V> boolean isEquals(ArrayList<V> actual, ArrayList<V> expected) {
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
     * 添加不同的条目到列表
     *
     * @return 如果entry在sourceList中已存在，则返回false，否则添加它并返回true。
     */
    public static <V> boolean addDistinctEntry(List<V> sourceList, V entry) {
        return (sourceList != null && !sourceList.contains(entry)) && sourceList.add(entry);
    }

    /**
     * 从entryList向sourceList添加所有不同的条目
     *
     * @return 添加条目的个数
     */
    public static <V> int addDistinctList(List<V> sourceList, List<V> entryList) {
        if (sourceList == null || isEmpty(entryList)) {
            return 0;
        }

        int sourceCount = sourceList.size();
        for (V entry : entryList) {
            if (!sourceList.contains(entry)) {
                sourceList.add(entry);
            }
        }
        return sourceList.size() - sourceCount;
    }

    /**
     * 删除列表中的重复条目
     *
     * @return 删除条目的个数
     */
    public static <V> int distinctList(List<V> sourceList) {
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
    public static <V> boolean addListNotNullValue(List<V> sourceList, V value) {
        return (sourceList != null && value != null) && sourceList.add(value);
    }


    /**
     * 反转列表
     */
    public static <V> List<V> invertList(List<V> sourceList) {
        if (isEmpty(sourceList)) {
            return sourceList;
        }

        List<V> invertList = new ArrayList<V>(sourceList.size());
        for (int i = sourceList.size() - 1; i >= 0; i--) {
            invertList.add(sourceList.get(i));
        }
        return invertList;
    }
}
