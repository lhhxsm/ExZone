package com.exzone.lib.widget.viewpager.transformer;

import android.support.v4.view.ViewPager;
import android.view.View;

/**
 * 作者:lhh
 * 描述:
 * 时间: 2016/10/10.
 */
public class NonPageTransformer implements ViewPager.PageTransformer {
  public static final ViewPager.PageTransformer INSTANCE = new NonPageTransformer();

  @Override public void transformPage(View page, float position) {
    page.setScaleX(0.999f);//hack
  }
}
