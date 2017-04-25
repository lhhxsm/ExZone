package com.exzone.lib.view;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.support.annotation.ColorInt;
import android.support.v7.widget.AppCompatTextView;
import android.util.AttributeSet;
import com.exzone.lib.R;

/**
 * 作者:李鸿浩
 * 描述:
 * 时间:2017/4/25.
 */
public class StrokedTextView extends AppCompatTextView {
  private final Canvas mCanvas = new Canvas();
  private final Paint mPaint = new Paint();
  private Bitmap mCache;
  private boolean mUpdateCachedBitmap;
  private int mStrokeColor;
  private float mStrokeWidth;

  public StrokedTextView(Context context) {
    this(context, null);
  }

  public StrokedTextView(Context context, AttributeSet attrs) {
    super(context, attrs, 0);
  }

  public StrokedTextView(Context context, AttributeSet attrs, int defStyleAttr) {
    super(context, attrs, defStyleAttr);
    init(context, attrs, defStyleAttr);
  }

  private void init(Context context, AttributeSet attrs, int defStyleAttr) {
    TypedArray a =
        context.obtainStyledAttributes(attrs, R.styleable.StrokedTextView, defStyleAttr, 0);
    mStrokeColor = a.getColor(R.styleable.StrokedTextView_strokedColor, 0xFF000000);
    mStrokeWidth = a.getFloat(R.styleable.StrokedTextView_strokedWidth, 0.0f);
    a.recycle();
    mUpdateCachedBitmap = true;
    mPaint.setAntiAlias(true);
    mPaint.setStyle(Paint.Style.FILL_AND_STROKE);
  }

  public int getStrokeColor() {
    return mStrokeColor;
  }

  public void setStrokeColor(@ColorInt int strokeColor) {
    if (mStrokeColor != strokeColor) {
      mStrokeColor = strokeColor;
      invalidate();
    }
  }

  public float getStrokeWidth() {
    return mStrokeWidth;
  }

  public void setStrokeWidth(float strokeWidth) {
    if (mStrokeWidth != strokeWidth) {
      mStrokeWidth = strokeWidth;
      invalidate();
    }
  }

  @Override protected void onTextChanged(CharSequence text, int start, int before, int after) {
    super.onTextChanged(text, start, before, after);
    mUpdateCachedBitmap = true;
  }

  @Override protected void onSizeChanged(int w, int h, int oldw, int oldh) {
    super.onSizeChanged(w, h, oldw, oldh);
    if (w > 0 && h > 0) {
      mUpdateCachedBitmap = true;
      mCache = Bitmap.createBitmap(w, h, Bitmap.Config.ARGB_8888);
    } else {
      mCache = null;
    }
  }

  @Override protected void onDraw(Canvas canvas) {
    if (mCache != null) {
      if (mUpdateCachedBitmap) {
        final int w = getMeasuredWidth();
        final int h = getMeasuredHeight();
        final String text = getText().toString();
        final Rect textBounds = new Rect();
        final Paint textPaint = getPaint();
        final int textWidth = (int) textPaint.measureText(text);
        textPaint.getTextBounds("x", 0, 1, textBounds);
        // Clear the old cached image
        mCanvas.setBitmap(mCache);
        mCanvas.drawColor(0, PorterDuff.Mode.CLEAR);
        // Draw the drawable
        final int drawableLeft = getPaddingLeft();
        final int drawableTop = getPaddingTop();
        final Drawable[] drawables = getCompoundDrawables();
        for (Drawable drawable : drawables) {
          if (drawable != null) {
            drawable.setBounds(drawableLeft, drawableTop,
                drawableLeft + drawable.getIntrinsicWidth(),
                drawableTop + drawable.getIntrinsicHeight());
            drawable.draw(mCanvas);
          }
        }
        final int left = w - getPaddingRight() - textWidth;
        final int bottom = (h + textBounds.height()) / 2;
        // Draw the outline of the text
        mPaint.setStrokeWidth(mStrokeWidth);
        mPaint.setColor(mStrokeColor);
        mPaint.setTextSize(getTextSize());
        mCanvas.drawText(text, left, bottom, mPaint);
        // Draw the text itself
        mPaint.setStrokeWidth(0);
        mPaint.setColor(getCurrentTextColor());
        mCanvas.drawText(text, left, bottom, mPaint);
        mUpdateCachedBitmap = false;
      }
      canvas.drawBitmap(mCache, 0, 0, mPaint);
    } else {
      super.onDraw(canvas);
    }
  }
}
