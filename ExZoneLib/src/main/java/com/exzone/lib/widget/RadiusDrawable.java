package com.exzone.lib.widget;

import android.graphics.Canvas;
import android.graphics.ColorFilter;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PixelFormat;
import android.graphics.RectF;
import android.graphics.drawable.Drawable;
import android.support.annotation.IntRange;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

/**
 * 作者:lhh
 * 描述:圆角 Drawable 每个角的圆半径可以不同 （使用不同半径圆角图形的时候 使用这个类 以防止 系统提供的用 xml 实现的圆角 Drawable 在2.3 上 出现各个角位置错乱）
 * 时间:2017/3/26.
 */
public class RadiusDrawable extends Drawable {
  private final Paint paint;
  private final boolean isStroke;
  private int topLeftRadius;
  private int topRightRadius;
  private int bottomLeftRadius;
  private int bottomRightRadius;
  private int left;
  private int top;
  private int right;
  private int bottom;
  private int color;
  private int strokeWidth = 0;
  private int strokeColor;

  private Path path;

  public RadiusDrawable(int topLeftRadius, int topRightRadius, int bottomLeftRadius,
      int bottomRightRadius, boolean isStroke, int color) {
    this.topLeftRadius = topLeftRadius;
    this.topRightRadius = topRightRadius;
    this.bottomLeftRadius = bottomLeftRadius;
    this.bottomRightRadius = bottomRightRadius;

    paint = new Paint(Paint.ANTI_ALIAS_FLAG);
    this.isStroke = isStroke;
    this.color = color;
  }

  public RadiusDrawable(int radius, boolean isStroke, int color) {
    this.topLeftRadius =
        this.topRightRadius = this.bottomLeftRadius = this.bottomRightRadius = radius;
    paint = new Paint(Paint.ANTI_ALIAS_FLAG);
    this.isStroke = isStroke;
    this.color = color;
  }

  public void setStrokeWidth(int width) {
    strokeWidth = width;
    setBounds(left, top, right, bottom);
  }

  public void setStrokeColor(int strokeColor) {
    this.strokeColor = strokeColor;
  }

  public void setColor(int color) {
    this.color = color;
  }

  public void setRadius(int radius) {
    this.topLeftRadius =
        this.topRightRadius = this.bottomLeftRadius = this.bottomRightRadius = radius;
  }

  public void setRadius(int topLeftRadius, int topRightRadius, int bottomLeftRadius,
      int bottomRightRadius) {
    this.topLeftRadius = topLeftRadius;
    this.topRightRadius = topRightRadius;
    this.bottomLeftRadius = bottomLeftRadius;
    this.bottomRightRadius = bottomRightRadius;
  }

  @Override public void setBounds(int left, int top, int right, int bottom) {
    super.setBounds(left, top, right, bottom);
    this.left = left;
    this.top = top;
    this.right = right;
    this.bottom = bottom;

    if (isStroke) {
      int halfStrokeWidth = strokeWidth / 2;
      left += halfStrokeWidth;
      top = top + halfStrokeWidth;
      right -= halfStrokeWidth;
      bottom = bottom - halfStrokeWidth;
    }

    path = new Path();
    path.moveTo(left + topLeftRadius, top);
    path.lineTo(right - topRightRadius, top);
    path.arcTo(new RectF(right - topRightRadius * 2, top, right, top + topRightRadius * 2), -90,
        90);
    // path.quadTo(right, top, right, top + topRightRadius);
    path.lineTo(right, bottom - bottomRightRadius);
    path.arcTo(
        new RectF(right - bottomRightRadius * 2, bottom - bottomRightRadius * 2, right, bottom), 0,
        90);
    // path.quadTo(right, bottom, right - bottomRightRadius, bottom);
    path.lineTo(left + bottomLeftRadius, bottom);
    path.arcTo(new RectF(left, bottom - bottomLeftRadius * 2, left + bottomLeftRadius * 2, bottom),
        90, 90);
    // path.quadTo(left, bottom, left, bottom - bottomLeftRadius);
    path.lineTo(left, top + topLeftRadius);
    path.arcTo(new RectF(left, top, left + topLeftRadius * 2, top + topLeftRadius * 2), 180, 90);
    // path.quadTo(left, top, left + topLeftRadius, top);
    path.close();
  }

  @Override public void draw(@NonNull Canvas canvas) {
    if (color != 0) {
      paint.setColor(color);
      paint.setStyle(Paint.Style.FILL);
      canvas.drawPath(path, paint);
    }

    if (strokeWidth > 0) {
      paint.setColor(strokeColor);
      paint.setStyle(Paint.Style.STROKE);
      paint.setStrokeJoin(Paint.Join.MITER);
      paint.setStrokeWidth(strokeWidth);
      canvas.drawPath(path, paint);
    }
  }

  @Override public void setAlpha(@IntRange(from = 0, to = 255) int alpha) {

  }

  @Override public void setColorFilter(@Nullable ColorFilter colorFilter) {

  }

  @Override public int getOpacity() {
    return PixelFormat.TRANSLUCENT;
  }
}
