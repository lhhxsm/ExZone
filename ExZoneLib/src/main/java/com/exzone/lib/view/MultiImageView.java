package com.exzone.lib.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import com.bumptech.glide.Glide;
import com.exzone.lib.util.ScreenUtils;
import java.util.List;

/**
 * 作者:李鸿浩
 * 描述:显示1~N张图片的View
 * 时间: 2016/10/10.
 */
public class MultiImageView extends LinearLayout {
  public static int MAX_WIDTH = 0;
  private List<String> mImages;//图片的Url列表
  /**
   * 单位Pixel
   */
  private int mOneMaxWH;//单张图片允许的最大宽高
  private int mMoreWH;//多张图片的宽高
  private int mPadding = ScreenUtils.dp2px(getContext(), 3);//图片间的间距

  private int MAX_ROW_COUNT = 3;//每行显示的最大张数

  private LayoutParams mOneParams;
  private LayoutParams mMoreParams, mMoreColumnFirstParams;
  private LayoutParams mRowParams;

  private OnItemClickListener mOnItemClickListener;
  private View.OnClickListener mImageOnClick = new OnClickListener() {
    @Override public void onClick(View view) {
      if (mOnItemClickListener != null) {
        mOnItemClickListener.onItemClick(view, (Integer) view.getTag());
      }
    }
  };

  public MultiImageView(Context context) {
    super(context);
  }

  public MultiImageView(Context context, AttributeSet attrs) {
    super(context, attrs);
  }

  public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
    this.mOnItemClickListener = onItemClickListener;
  }

  public void setImages(List<String> images) {
    if (images == null) {
      throw new IllegalArgumentException("images is null...");
    }
    this.mImages = images;
    if (MAX_WIDTH > 0) {
      mMoreWH = (MAX_WIDTH - mPadding * 2) / 3;////解决右侧图片和内容对不齐问题
      mOneMaxWH = MAX_WIDTH * 2 / 3;
      initImageLayoutParams();
    }
    initView();
  }

  @Override protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
    if (MAX_WIDTH == 0) {
      int width = measureWidth(widthMeasureSpec);
      if (width > 0) {
        MAX_WIDTH = width;
        if (mImages != null && mImages.size() > 0) {
          setImages(mImages);
        }
      }
    }
    super.onMeasure(widthMeasureSpec, heightMeasureSpec);
  }

  /**
   * 确定视图的宽度
   */
  private int measureWidth(int measureSpec) {
    int result = 0;
    int specMode = MeasureSpec.getMode(measureSpec);
    int specSize = MeasureSpec.getSize(measureSpec);
    if (specMode == MeasureSpec.EXACTLY) {
      result = specSize;
    } else {
      if (specMode == MeasureSpec.AT_MOST) {
        result = Math.min(result, specSize);
      }
    }
    return result;
  }

  private void initImageLayoutParams() {
    int wrap = LayoutParams.WRAP_CONTENT;
    int match = LayoutParams.MATCH_PARENT;

    mOneParams = new LayoutParams(mOneMaxWH, wrap);

    mMoreColumnFirstParams = new LayoutParams(mMoreWH, mMoreWH);
    mMoreParams = new LayoutParams(mMoreWH, mMoreWH);
    mMoreParams.setMargins(mPadding, 0, 0, 0);

    mRowParams = new LayoutParams(match, wrap);
  }

  /**
   * 根据ImageView的数量初始化不同的View布局,还要为每一个View做点击效果
   */
  private void initView() {
    this.setOrientation(VERTICAL);
    this.removeAllViews();
    if (MAX_WIDTH == 0) {
      //为了触发onMeasure来测量MultiImageView的最大宽度,MultiImageView的宽度设置为MATCH_PARENT
      addView(new View(getContext()));
      return;
    }
    if (mImages == null || mImages.size() == 0) {
      return;
    }
    if (mImages.size() == 1) {
      addView(createImageView(0, false));
    } else {
      int allCount = mImages.size();
      if (allCount == 4) {
        MAX_ROW_COUNT = 2;
      } else {
        MAX_ROW_COUNT = 3;
      }
      int rowCount = allCount / MAX_ROW_COUNT + (allCount % MAX_ROW_COUNT > 0 ? 1 : 0);// 行数
      for (int rowCursor = 0; rowCursor < rowCount; rowCursor++) {
        LinearLayout rowLayout = new LinearLayout(getContext());
        rowLayout.setOrientation(LinearLayout.HORIZONTAL);

        rowLayout.setLayoutParams(mRowParams);
        if (rowCursor != 0) {
          rowLayout.setPadding(0, mPadding, 0, 0);
        }

        int columnCount =
            allCount % MAX_ROW_COUNT == 0 ? MAX_ROW_COUNT : allCount % MAX_ROW_COUNT;//每行的列数
        if (rowCursor != rowCount - 1) {
          columnCount = MAX_ROW_COUNT;
        }
        addView(rowLayout);

        int rowOffset = rowCursor * MAX_ROW_COUNT;// 行偏移
        for (int columnCursor = 0; columnCursor < columnCount; columnCursor++) {
          int position = columnCursor + rowOffset;
          rowLayout.addView(createImageView(position, true));
        }
      }
    }
  }

  private ImageView createImageView(int position, boolean isMultiImage) {
    String url = mImages.get(position);
    ImageView imageView = new ColorFilterImageView(getContext());
    if (isMultiImage) {
      imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
      imageView.setLayoutParams(
          position % MAX_ROW_COUNT == 0 ? mMoreColumnFirstParams : mMoreParams);
    } else {
      imageView.setAdjustViewBounds(true);
      imageView.setScaleType(ImageView.ScaleType.FIT_START);
      imageView.setMaxHeight(mOneMaxWH);
      imageView.setLayoutParams(mOneParams);
    }

    imageView.setTag(position);
    imageView.setId(url.hashCode());
    imageView.setOnClickListener(mImageOnClick);
    Glide.with(getContext()).load(url).centerCrop().into(imageView);
    return imageView;
  }

  public interface OnItemClickListener {
    void onItemClick(View view, int position);
  }
}
