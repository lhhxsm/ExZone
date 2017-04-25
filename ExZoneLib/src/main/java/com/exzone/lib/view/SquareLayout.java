package com.exzone.lib.view;

import android.annotation.TargetApi;
import android.content.Context;
import android.os.Build;
import android.util.AttributeSet;
import android.widget.RelativeLayout;

/**
 * 作者:李鸿浩
 * 描述:RelativeLayout将始终是正方形 - 相同的宽度和高度，其中高度基于宽度。
 * 时间: 2016/10/11.
 */
public class SquareLayout extends RelativeLayout {

  public SquareLayout(Context context) {
    super(context);
  }

  public SquareLayout(Context context, AttributeSet attrs) {
    super(context, attrs);
  }

  public SquareLayout(Context context, AttributeSet attrs, int defStyleAttr) {
    super(context, attrs, defStyleAttr);
  }

  @TargetApi(Build.VERSION_CODES.LOLLIPOP)
  public SquareLayout(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
    super(context, attrs, defStyleAttr, defStyleRes);
  }

  @Override protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
    setMeasuredDimension(getDefaultSize(0, widthMeasureSpec), getDefaultSize(0, heightMeasureSpec));
    int childWidthSize = getMeasuredWidth();
    //高度和宽度一样
    heightMeasureSpec =
        widthMeasureSpec = MeasureSpec.makeMeasureSpec(childWidthSize, MeasureSpec.EXACTLY);
    super.onMeasure(widthMeasureSpec, heightMeasureSpec);
  }
}
