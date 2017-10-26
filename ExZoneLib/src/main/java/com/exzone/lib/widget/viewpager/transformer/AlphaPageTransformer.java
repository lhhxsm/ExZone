package com.exzone.lib.widget.viewpager.transformer;

import android.annotation.TargetApi;
import android.os.Build;
import android.support.v4.view.ViewPager;
import android.view.View;

/**
 * 作者:lhh
 * 描述:
 * 时间: 2016/10/10.
 */
public class AlphaPageTransformer extends BasePageTransformer {
  private static final float DEFAULT_MIN_ALPHA = 0.5f;
  private float mMinAlpha = DEFAULT_MIN_ALPHA;

  public AlphaPageTransformer() {
  }

  public AlphaPageTransformer(float minAlpha) {
    this(minAlpha, NonPageTransformer.INSTANCE);
  }

  public AlphaPageTransformer(ViewPager.PageTransformer pageTransformer) {
    this(DEFAULT_MIN_ALPHA, pageTransformer);
  }

  public AlphaPageTransformer(float minAlpha, ViewPager.PageTransformer pageTransformer) {
    mMinAlpha = minAlpha;
    mPageTransformer = pageTransformer;
  }

  @TargetApi(Build.VERSION_CODES.HONEYCOMB) @Override
  protected void pageTransformer(View page, float position) {
    page.setScaleX(0.999f);//hack

    if (position < -1) { // [-Infinity,-1)
      page.setAlpha(mMinAlpha);
    } else if (position <= 1) { // [-1,1]

      if (position < 0) //[0，-1]
      {           //[1,min]
        float factor = mMinAlpha + (1 - mMinAlpha) * (1 + position);
        page.setAlpha(factor);
      } else//[1，0]
      {
        //[min,1]
        float factor = mMinAlpha + (1 - mMinAlpha) * (1 - position);
        page.setAlpha(factor);
      }
    } else { // (1,+Infinity]
      page.setAlpha(mMinAlpha);
    }
  }
}
