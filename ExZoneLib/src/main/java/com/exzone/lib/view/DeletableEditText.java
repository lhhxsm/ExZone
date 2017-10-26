package com.exzone.lib.view;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.support.v7.widget.AppCompatEditText;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.CycleInterpolator;
import android.view.animation.TranslateAnimation;

/**
 * 作者:lhh
 * 描述:可以自带删除按键的EditText,图标设置方法是drawableRight
 * 时间：2016/10/6.
 */
public class DeletableEditText extends AppCompatEditText {
  private Drawable mRightDrawable;

  public DeletableEditText(Context context) {
    super(context);
    init();
  }

  public DeletableEditText(Context context, AttributeSet attrs) {
    super(context, attrs);
    init();
  }

  public DeletableEditText(Context context, AttributeSet attrs, int defStyleAttr) {
    super(context, attrs, defStyleAttr);
    init();
  }

  private void init() {
    //getCompoundDrawables:Returns drawables for the left, top, right, and bottom borders.
    Drawable[] drawables = this.getCompoundDrawables();
    //取得right位置的Drawable即我们在布局文件中设置的android:drawableRight
    mRightDrawable = drawables[2];
    //设置焦点变化的监听
    this.setOnFocusChangeListener(new FocusChangeListenerImpl());
    //设置EditText文字变化的监听
    this.addTextChangedListener(new TextWatcherImpl());
    //初始化时让右边clean图标不可见
    setClearDrawableVisible(false);
  }

  @Override public boolean onTouchEvent(MotionEvent event) {
    switch (event.getAction()) {
      case MotionEvent.ACTION_UP:
        boolean isClean =
            (event.getX() > (getWidth() - getTotalPaddingRight())) && (event.getX() < (getWidth()
                - getPaddingRight()));
        if (isClean) {
          setText("");
        }
        break;
      default:
        break;
    }
    return super.onTouchEvent(event);
  }

  //隐藏或者显示右边clean的图标
  protected void setClearDrawableVisible(boolean isVisible) {
    Drawable rightDrawable;
    if (isVisible) {
      rightDrawable = mRightDrawable;
    } else {
      rightDrawable = null;
    }
    //使用代码设置该控件left, top, right, and bottom处的图标
    setCompoundDrawables(getCompoundDrawables()[0], getCompoundDrawables()[1], rightDrawable,
        getCompoundDrawables()[3]);
  }

  // 显示一个动画,以提示用户输入
  public void setShakeAnimation() {
    this.startAnimation(shakeAnimation(5));
  }

  //CycleTimes动画重复的次数
  public Animation shakeAnimation(int CycleTimes) {
    Animation translateAnimation = new TranslateAnimation(0, 10, 0, 10);
    translateAnimation.setInterpolator(new CycleInterpolator(CycleTimes));
    translateAnimation.setDuration(1000);
    return translateAnimation;
  }

  private class FocusChangeListenerImpl implements OnFocusChangeListener {
    @Override public void onFocusChange(View v, boolean hasFocus) {
      if (hasFocus) {
        boolean isVisible = getText().toString().length() >= 1;
        setClearDrawableVisible(isVisible);
      } else {
        setClearDrawableVisible(false);
      }
    }
  }

  //当输入结束后判断是否显示右边clean的图标
  private class TextWatcherImpl implements TextWatcher {
    @Override public void beforeTextChanged(CharSequence s, int start, int count, int after) {

    }

    @Override public void onTextChanged(CharSequence s, int start, int before, int count) {

    }

    @Override public void afterTextChanged(Editable s) {
      boolean isVisible = getText().toString().length() >= 1;
      setClearDrawableVisible(isVisible);
    }
  }
}
