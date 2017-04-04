package com.exzone.lib.cache;


import java.lang.ref.ReferenceQueue;
import java.lang.ref.SoftReference;
import java.util.HashMap;

/**
 * 软引用的集合
 *
 * @param <K>
 * @param <V>
 * @author Administrator
 */
@SuppressWarnings("ALL")
public class SoftMap<K, V> extends HashMap<K, V> {
    // private HashMap<K, SoftReference<V>> temp;// 占用内存较多的都被放到袋子中——引用级别降低了
    private HashMap<K, SoftValue<K, V>> temp;// 占用内存较多的都被放到袋子中——引用级别降低了

    private ReferenceQueue<V> queue;// GC记录曾经偷过的手机的容器

    // 降低引用级别
    public SoftMap() {
        // ①将所有的手机（BaseView的子类对象）装到袋子里面（SoftReference）
        // ②将空袋子做回收的处理
        temp = new HashMap<K, SoftValue<K, V>>();
        queue = new ReferenceQueue<V>();
    }

    @Override
    public V put(K key, V value) {
        // SoftReference<V> sr = new SoftReference<V>(value);
        SoftValue<K, V> sr = new SoftValue<K, V>(key, value, queue);
        temp.put(key, sr);
        return null;
    }

    @Override
    public V get(Object key) {
        clearSr();
        SoftReference<V> sr = temp.get(key);
        if (sr != null) {
            // 垃圾回收器清除，则此方法将返回 null
            return sr.get();
        }

        return null;
    }

    @Override
    public boolean containsKey(Object key) {
        clearSr();
        // 什么才叫真正的contain
        SoftReference<V> sr = temp.get(key);
        if (sr != null) {
            V v = sr.get();
            return v != null;
        }
        return false;
    }

    /**
     * 清除空袋子
     */
    private void clearSr() {
        // 方式一：循环HashMap<K, SoftReference<V>> temp，如果是空袋子回收
        // 如果内存充足
        // 方式二：不循环temp，让GC记录一下曾经偷过的手机，如果能够获取到记录的结果
        // 如何控制记录结果的容器
        // 则从该队列中"移除"此对象并返回。否则此方法立即返回 null。
        SoftValue<K, V> sr = (SoftValue<K, V>) queue.poll();
        while (sr != null) {
            // HashMap<K, SoftReference<V>> temp,删除sr
            temp.remove(sr.key);

            sr = (SoftValue<K, V>) queue.poll();
        }
    }

    /**
     * 加强版的袋子
     *
     * @param <K>
     * @param <V>
     * @author Administrator
     */
    private class SoftValue<K, V> extends SoftReference<V> {
        private Object key;

        public SoftValue(Object key, V value, ReferenceQueue<V> queue) {
            super(value, queue);
            this.key = key;
        }

    }
}
