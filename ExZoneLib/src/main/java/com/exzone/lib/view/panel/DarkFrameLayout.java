package com.exzone.lib.view.panel;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.support.annotation.AttrRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.widget.FrameLayout;

/**
 * 作者:lhh
 * 描述:
 * 时间:2017/4/4.
 */
public class DarkFrameLayout extends FrameLayout {

  public static final int MAX_ALPHA = 0X9f;

  private int alpha = 0x00;
  private Paint mFadePaint;

  private SlideBottomPanel slideBottomPanel;

  public DarkFrameLayout(@NonNull Context context) {
    this(context, null);
  }

  public DarkFrameLayout(@NonNull Context context, @Nullable AttributeSet attrs) {
    this(context, attrs, 0);
  }

  public DarkFrameLayout(@NonNull Context context, @Nullable AttributeSet attrs,
      @AttrRes int defStyleAttr) {
    super(context, attrs, defStyleAttr);
    mFadePaint = new Paint();
  }

  @Override protected void dispatchDraw(Canvas canvas) {
    super.dispatchDraw(canvas);
    drawFade(canvas);
  }

  private void drawFade(Canvas canvas) {
    mFadePaint.setColor(Color.argb(alpha, 0, 0, 0));
    canvas.drawRect(0, 0, getMeasuredWidth(), getHeight(), mFadePaint);
  }

  public void fade(boolean fade) {
    alpha = fade ? 0x8f : 0x00;
    invalidate();
  }

  public void fade(int alpha) {
    this.alpha = alpha;
    invalidate();
  }

  @Override public boolean onInterceptTouchEvent(MotionEvent ev) {
    return slideBottomPanel.isPanelShowing();
  }

  public int getCurrentAlpha() {
    return alpha;
  }

  public void setSlideBottomPanel(SlideBottomPanel slideBottomPanel) {
    this.slideBottomPanel = slideBottomPanel;
  }
}
