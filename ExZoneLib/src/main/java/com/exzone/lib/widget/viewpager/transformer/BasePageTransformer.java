package com.exzone.lib.widget.viewpager.transformer;

import android.annotation.TargetApi;
import android.os.Build;
import android.support.v4.view.ViewPager;
import android.view.View;

/**
 * 作者:李鸿浩
 * 描述:使用的时候必须设置  mViewPager.setPageMargin(40);  android:clipChildren="false"
 * 时间: 2016/10/10.
 */
public abstract class BasePageTransformer implements ViewPager.PageTransformer {
    protected ViewPager.PageTransformer mPageTransformer = NonPageTransformer.INSTANCE;
    public static final float DEFAULT_CENTER = 0.5f;

    @TargetApi(Build.VERSION_CODES.HONEYCOMB)
    @Override
    public void transformPage(View page, float position) {
        if (mPageTransformer != null) {
            mPageTransformer.transformPage(page, position);
        }
        pageTransformer(page, position);
    }

    protected abstract void pageTransformer(View page, float position);
}
