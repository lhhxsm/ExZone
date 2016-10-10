package com.exzone.lib.widget.viewpager.transformer;

import android.annotation.TargetApi;
import android.os.Build;
import android.support.v4.view.ViewPager;
import android.view.View;

/**
 * 作者:李鸿浩
 * 描述:
 * 时间: 2016/10/10.
 */
public class RotateDownPageTransformer extends BasePageTransformer {
    private static final float DEFAULT_MAX_ROTATE = 15.0f;
    private float mMaxRotate = DEFAULT_MAX_ROTATE;

    public RotateDownPageTransformer() {
    }

    public RotateDownPageTransformer(float maxRotate) {
        this(maxRotate, NonPageTransformer.INSTANCE);
    }

    public RotateDownPageTransformer(ViewPager.PageTransformer pageTransformer) {
        this(DEFAULT_MAX_ROTATE, pageTransformer);
    }

    public RotateDownPageTransformer(float maxRotate, ViewPager.PageTransformer pageTransformer) {
        mPageTransformer = pageTransformer;
        mMaxRotate = maxRotate;
    }


    @TargetApi(Build.VERSION_CODES.HONEYCOMB)
    @Override
    protected void pageTransformer(View page, float position) {
        if (position < -1) { // [-Infinity,-1)
            // This page is way off-screen to the left.
            page.setRotation(mMaxRotate * -1);
            page.setPivotX(page.getWidth());
            page.setPivotY(page.getHeight());

        } else if (position <= 1) { // [-1,1]

            if (position < 0)//[0，-1]
            {
                page.setPivotX(page.getWidth() * (DEFAULT_CENTER + DEFAULT_CENTER * (-position)));
                page.setPivotY(page.getHeight());
                page.setRotation(mMaxRotate * position);
            } else//[1,0]
            {
                page.setPivotX(page.getWidth() * DEFAULT_CENTER * (1 - position));
                page.setPivotY(page.getHeight());
                page.setRotation(mMaxRotate * position);
            }
        } else { // (1,+Infinity]
            // This page is way off-screen to the right.
            page.setRotation(mMaxRotate);
            page.setPivotX(page.getWidth() * 0);
            page.setPivotY(page.getHeight());
        }
    }
}
