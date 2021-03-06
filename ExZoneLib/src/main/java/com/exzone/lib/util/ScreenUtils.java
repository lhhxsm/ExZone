package com.exzone.lib.util;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Rect;
import android.util.DisplayMetrics;
import android.util.TypedValue;
import android.view.View;
import android.view.WindowManager;

/**
 * 作者:lhh
 * 描述:屏幕工具类:屏幕宽高,截屏,单位转换等
 * 时间：2016/10/10.
 */
public class ScreenUtils {

  private ScreenUtils() {
    throw new AssertionError();
  }

  /**
   * 获得屏幕高度
   */
  public static int getScreenWidth(Context context) {
    WindowManager wm = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
    DisplayMetrics outMetrics = new DisplayMetrics();
    wm.getDefaultDisplay().getMetrics(outMetrics);
    int width = outMetrics.widthPixels;
    Logger.e("当前屏幕宽度：" + width);
    return width;
  }

  /**
   * 获得屏幕宽度
   */
  public static int getScreenHeight(Context context) {
    WindowManager wm = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
    DisplayMetrics outMetrics = new DisplayMetrics();
    wm.getDefaultDisplay().getMetrics(outMetrics);
    int height = outMetrics.heightPixels;
    Logger.e("当前屏幕高度：" + height);
    return height;
  }

  /**
   * 获得状态栏的高度
   */
  public static int getStatusHeight(Context context) {
    int statusHeight = -1;
    try {
      Class<?> clazz = Class.forName("com.android.internal.R$dimen");
      Object object = clazz.newInstance();
      int height = Integer.parseInt(clazz.getField("status_bar_height").get(object).toString());
      statusHeight = context.getResources().getDimensionPixelSize(height);
    } catch (Exception e) {
      e.printStackTrace();
    }
    Logger.e("当前状态栏高度：" + statusHeight);
    return statusHeight;
  }

  /**
   * 获取当前屏幕截图，包含状态栏
   */
  public static Bitmap snapShotWithStatusBar(Activity activity) {
    View view = activity.getWindow().getDecorView();
    view.setDrawingCacheEnabled(true);
    view.buildDrawingCache();
    Bitmap bmp = view.getDrawingCache();
    int width = getScreenWidth(activity);
    int height = getScreenHeight(activity);
    Bitmap bp = null;
    bp = Bitmap.createBitmap(bmp, 0, 0, width, height);
    view.destroyDrawingCache();
    return bp;
  }

  /**
   * 获取当前屏幕截图，不包含状态栏
   */
  public static Bitmap snapShotWithoutStatusBar(Activity activity) {
    View view = activity.getWindow().getDecorView();
    view.setDrawingCacheEnabled(true);
    view.buildDrawingCache();
    Bitmap bmp = view.getDrawingCache();
    Rect frame = new Rect();
    activity.getWindow().getDecorView().getWindowVisibleDisplayFrame(frame);
    int statusBarHeight = frame.top;

    int width = getScreenWidth(activity);
    int height = getScreenHeight(activity);
    Bitmap bp = null;
    bp = Bitmap.createBitmap(bmp, 0, statusBarHeight, width, height - statusBarHeight);
    view.destroyDrawingCache();
    return bp;
  }

  /**
   * dp转px
   */
  public static int dp2px(Context context, float dp) {
    float scale = context.getResources().getDisplayMetrics().density;
    return (int) (dp * scale + 0.5f);
  }

  /**
   * dp转px
   */
  public static int dp2Px(Context context, float dp) {
    DisplayMetrics metrics = context.getResources().getDisplayMetrics();
    return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp, metrics);
  }

  /**
   * px转dp
   */
  public static int px2dp(Context context, float px) {
    float scale = context.getResources().getDisplayMetrics().density;
    return (int) (px / scale + 0.5f);
  }

  /**
   * sp转px
   */
  public static int sp2px(Context context, float sp) {
    float scale = context.getResources().getDisplayMetrics().scaledDensity;
    return (int) (sp * scale + 0.5f);
  }

  /**
   * sp转px
   */
  public static int sp2Px(Context context, float sp) {
    DisplayMetrics metrics = context.getResources().getDisplayMetrics();
    return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP, sp, metrics);
  }

  /**
   * px转sp
   */
  public static int px2sp(Context context, float px) {
    float scale = context.getResources().getDisplayMetrics().scaledDensity;
    return (int) (px / scale + 0.5f);
  }

  /**
   * 获取屏幕的比例
   */
  public static float getScaledDensity(Context context) {
    DisplayMetrics dm = context.getResources().getDisplayMetrics();
    return dm.scaledDensity;
  }

  /**
   * 获取控件的高度，如果获取的高度为0，则重新计算尺寸后再返回高度
   */
  public static int getViewMeasuredHeight(View view) {
    calcViewMeasure(view);
    return view.getMeasuredHeight();
  }

  /**
   * 获取控件的宽度，如果获取的宽度为0，则重新计算尺寸后再返回宽度
   */
  public static int getViewMeasuredWidth(View view) {
    calcViewMeasure(view);
    return view.getMeasuredWidth();
  }

  /**
   * 测量控件的尺寸
   */
  public static void calcViewMeasure(View view) {
    int width = View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED);
    int expandSpec =
        View.MeasureSpec.makeMeasureSpec(Integer.MAX_VALUE >> 2, View.MeasureSpec.AT_MOST);
    view.measure(width, expandSpec);
  }
}
