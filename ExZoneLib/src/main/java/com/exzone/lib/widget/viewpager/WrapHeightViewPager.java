package com.exzone.lib.widget.viewpager;

import android.content.Context;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.view.View;

/**
 * Created by lhh on 2015/10/19.
 */
public class WrapHeightViewPager extends ViewPager {
  public WrapHeightViewPager(Context context) {
    super(context);
  }

  public WrapHeightViewPager(Context context, AttributeSet attrs) {
    super(context, attrs);
  }

  @Override protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
    int height = 0;
    for (int i = 0; i < getChildCount(); i++) {
      View child = getChildAt(i);
      child.measure(widthMeasureSpec, MeasureSpec.makeMeasureSpec(0, MeasureSpec.UNSPECIFIED));
      int h = child.getMeasuredHeight();
      if (h > height) {
        height = h;
      }
    }

    heightMeasureSpec = MeasureSpec.makeMeasureSpec(height, MeasureSpec.EXACTLY);
    super.onMeasure(widthMeasureSpec, heightMeasureSpec);
  }

  private int measureHeight(int measureSpec, View view) {
    int result = 0;
    int specMode = MeasureSpec.getMode(measureSpec);
    int specSize = MeasureSpec.getSize(measureSpec);

    if (specMode == MeasureSpec.EXACTLY) {
      result = specSize;
    } else {
      // set the height from the base view if available
      if (view != null) {
        result = view.getMeasuredHeight();
      }
      if (specMode == MeasureSpec.AT_MOST) {
        result = Math.min(result, specSize);
      }
    }
    return result;
  }
}
