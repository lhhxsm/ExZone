package com.exzone.lib.widget.badge;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.graphics.drawable.StateListDrawable;
import android.os.Build;
import android.support.v7.widget.AppCompatTextView;
import android.util.AttributeSet;

import com.exzone.lib.R;
import com.exzone.lib.util.ScreenUtils;


/**
 * 作者:李鸿浩
 * 描述:用于需要圆角矩形框背景的TextView的情况,减少直接使用TextView时引入的shape资源文件
 * 时间:2017/3/26.
 */
public class BadgeView extends AppCompatTextView {
    private Context mContext;
    private GradientDrawable mBackground = new GradientDrawable();
    private int mBackgroundColor;
    private int mCornerRadius;
    private int mStrokeWidth;
    private int mStrokeColor;
    private boolean isRadiusHalfHeight;
    private boolean isWidthHeightEqual;

    public BadgeView(Context context) {
        this(context, null);
    }

    public BadgeView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public BadgeView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context, attrs);
    }

    private void init(Context context, AttributeSet attrs) {
        this.mContext = context;
        TypedArray ta = mContext.obtainStyledAttributes(attrs, R.styleable.BadgeView);
        mBackgroundColor = ta.getColor(R.styleable.BadgeView_backgroundColor, Color.TRANSPARENT);
        mCornerRadius = ta.getDimensionPixelSize(R.styleable.BadgeView_cornerRadius, 0);
        mStrokeWidth = ta.getDimensionPixelSize(R.styleable.BadgeView_strokeWidth, 0);
        mStrokeColor = ta.getColor(R.styleable.BadgeView_strokeColor, Color.TRANSPARENT);
        isRadiusHalfHeight = ta.getBoolean(R.styleable.BadgeView_isRadiusHalfHeight, false);
        isWidthHeightEqual = ta.getBoolean(R.styleable.BadgeView_isWidthHeightEqual, false);
        ta.recycle();
    }


    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        if (isWidthHeightEqual() && getWidth() > 0 && getHeight() > 0) {
            int max = Math.max(getWidth(), getHeight());
            int measureSpec = MeasureSpec.makeMeasureSpec(max, MeasureSpec.EXACTLY);
            super.onMeasure(measureSpec, measureSpec);
            return;
        }
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    }

    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        super.onLayout(changed, left, top, right, bottom);
        if (isRadiusHalfHeight()) {
            setCornerRadius(getHeight() / 2);
        } else {
            setBgSelector();
        }
    }

    public void setIsRadiusHalfHeight(boolean isRadiusHalfHeight) {
        this.isRadiusHalfHeight = isRadiusHalfHeight;
        setBgSelector();
    }

    public void setIsWidthHeightEqual(boolean isWidthHeightEqual) {
        this.isWidthHeightEqual = isWidthHeightEqual;
        setBgSelector();
    }

    public int getBackgroundColor() {
        return mBackgroundColor;
    }

    public void setBackgroundColor(int backgroundColor) {
        this.mBackgroundColor = backgroundColor;
        setBgSelector();
    }

    public int getCornerRadius() {
        return mCornerRadius;
    }

    public void setCornerRadius(int cornerRadius) {
        this.mCornerRadius = ScreenUtils.dp2Px(mContext, cornerRadius);
        setBgSelector();
    }

    public int getStrokeWidth() {
        return mStrokeWidth;
    }

    public void setStrokeWidth(int strokeWidth) {
        this.mStrokeWidth = ScreenUtils.dp2Px(mContext, strokeWidth);
        setBgSelector();
    }

    public int getStrokeColor() {
        return mStrokeColor;
    }

    public void setStrokeColor(int strokeColor) {
        this.mStrokeColor = strokeColor;
        setBgSelector();
    }

    public boolean isRadiusHalfHeight() {
        return isRadiusHalfHeight;
    }

    public boolean isWidthHeightEqual() {
        return isWidthHeightEqual;
    }

    private void setDrawable(GradientDrawable gd, int color, int strokeColor) {
        gd.setColor(color);
        gd.setCornerRadius(mCornerRadius);
        gd.setStroke(mStrokeWidth, strokeColor);
    }

    public void setBgSelector() {
        StateListDrawable bg = new StateListDrawable();
        setDrawable(mBackground, mBackgroundColor, mStrokeColor);
        bg.addState(new int[]{-android.R.attr.state_pressed}, mBackground);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {//16
            setBackground(bg);
        } else {
            //noinspection deprecation
            setBackgroundDrawable(bg);
        }
    }
}
