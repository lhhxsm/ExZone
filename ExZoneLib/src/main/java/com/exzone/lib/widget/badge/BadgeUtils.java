package com.exzone.lib.widget.badge;

import android.util.DisplayMetrics;
import android.view.View;
import android.view.ViewGroup;

import com.exzone.lib.R;

/**
 * 作者:李鸿浩
 * 描述:
 * 未读消息提示View,显示小红点或者带有数字的红点:
 * 数字一位,圆
 * 数字两位,圆角矩形,圆角是高度的一半
 * 数字超过两位,显示99+
 * 时间:2017/3/26.
 */
public class BadgeUtils {

    public static void show(BadgeView badgeView, int num) {
        if (badgeView == null) {
            return;
        }
        ViewGroup.LayoutParams lp = badgeView.getLayoutParams();
        DisplayMetrics dm = badgeView.getResources().getDisplayMetrics();
        badgeView.setVisibility(View.VISIBLE);
        if (num <= 0) {//圆点,设置默认宽高
            badgeView.setStrokeWidth(0);
            badgeView.setText("");

            lp.width = (int) (5 * dm.density);
            lp.height = (int) (5 * dm.density);
            badgeView.setLayoutParams(lp);
        } else {
            lp.height = (int) (18 * dm.density);
            if (num > 0 && num < 10) {//圆
                lp.width = (int) (18 * dm.density);
                badgeView.setText(String.valueOf(num));
            } else if (num > 9 && num < 100) {//圆角矩形,圆角是高度的一半,设置默认padding
                lp.width = ViewGroup.LayoutParams.WRAP_CONTENT;
                badgeView.setPadding((int) (6 * dm.density), 0, (int) (6 * dm.density), 0);
                badgeView.setText(String.valueOf(num));
            } else {//数字超过两位,显示99+
                lp.width = ViewGroup.LayoutParams.WRAP_CONTENT;
                badgeView.setPadding((int) (6 * dm.density), 0, (int) (6 * dm.density), 0);
                badgeView.setText(R.string.more_than_99);
            }
            badgeView.setLayoutParams(lp);
        }
    }

    public static void setSize(BadgeView badgeView, int size) {
        if (badgeView == null) {
            return;
        }
        ViewGroup.LayoutParams lp = badgeView.getLayoutParams();
        lp.width = size;
        lp.height = size;
        badgeView.setLayoutParams(lp);
    }
}
