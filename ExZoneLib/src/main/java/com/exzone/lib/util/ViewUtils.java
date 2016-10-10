package com.exzone.lib.util;

import android.view.View;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.view.ViewParent;

/**
 * 作者:李鸿浩
 * 描述:视图工具类
 * 时间：2016/10/10.
 */
public class ViewUtils {

    /**
     * 移除自身布局
     *
     * @param v 被移除的View
     */
    public static void removeParent(View v) {
        // 先找到爹 在通过爹去移除孩子
        ViewParent parent = v.getParent();
        // 所有的控件 都有爹 爹一般情况下 就是ViewGroup
        if (parent instanceof ViewGroup) {
            ViewGroup group = (ViewGroup) parent;
            group.removeView(v);
        }
    }

    /**
     * 修改普通View的高
     */
    public static void changeH(View v, int H) {
        LayoutParams params = v.getLayoutParams();
        params.height = H;
        v.setLayoutParams(params);
    }

    /**
     * 修改普通View的宽
     */
    public static void changeW(View v, int W) {
        LayoutParams params = v.getLayoutParams();
        params.width = W;
        v.setLayoutParams(params);
    }

    /**
     * 修改控件的宽高
     */
    public static void changeWH(View v, int W, int H) {
        LayoutParams params = v.getLayoutParams();
        params.width = W;
        params.height = H;
        v.setLayoutParams(params);
    }
}
