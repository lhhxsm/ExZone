package com.exzone.lib.view;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.support.v7.widget.AppCompatImageView;
import android.util.AttributeSet;
import com.bumptech.glide.load.resource.bitmap.GlideBitmapDrawable;
import com.exzone.lib.util.ScreenUtils;

/**
 * 作者:李鸿浩
 * 描述:自定义控件，用于显示宽度和ImageView相同，高度自适应的图片显示模式.
 * 除此之外，还添加了最大高度限制，若图片长度大于等于屏幕长度，则高度显示为屏幕的1/3
 * 时间: 2017/2/7.
 */

public class ShowMaxImageView extends AppCompatImageView {
  private float mHeight = 0;

  public ShowMaxImageView(Context context) {
    this(context, null);
  }

  public ShowMaxImageView(Context context, AttributeSet attrs) {
    this(context, attrs, 0);
  }

  public ShowMaxImageView(Context context, AttributeSet attrs, int defStyleAttr) {
    super(context, attrs, defStyleAttr);
  }

  @Override public void setImageBitmap(Bitmap bm) {

    if (bm != null) {
      getHeight(bm);
    }

    super.setImageBitmap(bm);
    requestLayout();
    invalidate();
  }

  @Override public void setImageDrawable(Drawable drawable) {
    if (drawable != null) {
      getHeight(drawableToBitmap(drawable));
    }
    super.setImageDrawable(drawable);
    requestLayout();
    invalidate();
  }

  @Override protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
    if (mHeight != 0) {
      int sizeWidth = MeasureSpec.getSize(widthMeasureSpec);
      int sizeHeight = MeasureSpec.getSize(heightMeasureSpec);

      int resultHeight = (int) Math.max(mHeight, sizeHeight);

      if (resultHeight >= ScreenUtils.getScreenHeight(getContext())) {
        resultHeight = ScreenUtils.getScreenHeight(getContext()) / 3;
      }

      setMeasuredDimension(sizeWidth, resultHeight);
    } else {
      super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    }
  }

  private void getHeight(Bitmap bm) {
    float bitmapWidth = bm.getWidth();
    float bitmapHeight = bm.getHeight();

    if (bitmapWidth > 0 && bitmapHeight > 0) {
      float scaleWidth = getWidth() / bitmapWidth;
      if (scaleWidth != 0) {
        mHeight = bitmapHeight * scaleWidth;
      }
    }
  }

  private Bitmap drawableToBitmap(Drawable drawable) {
    if (drawable != null) {
      if (drawable instanceof GlideBitmapDrawable) {
        GlideBitmapDrawable bd = (GlideBitmapDrawable) drawable;
        return bd.getBitmap();
      } else if (drawable instanceof BitmapDrawable) {
        BitmapDrawable bd = (BitmapDrawable) drawable;
        return bd.getBitmap();
      } else {
        return null;
      }
    } else {
      return null;
    }
  }
}
