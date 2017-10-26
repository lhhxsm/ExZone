package com.exzone.lib.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import com.exzone.lib.R;

/**
 * 作者:lhh
 * 描述:
 * 时间: 2017/2/8.
 */

public class PositionBar extends LinearLayout {

  private String[] mItems = {
      "A", "B", "C", "D", "E", "F", "G", "H", "I", "J", "K", "L", "M", "N", "O", "P", "Q", "R", "S",
      "T", "U", "V", "W", "X", "Y", "Z"
  };

  private OnPositionChangedListener listener;

  public PositionBar(Context context, AttributeSet attrs) {
    super(context, attrs);

    setOrientation(VERTICAL);
    setBackgroundResource(R.drawable.cirrect_5a_primary_solid);
  }

  public PositionBar(Context context) {
    this(context, null);
  }

  public void setOnPositionChangedListener(OnPositionChangedListener listener) {
    this.listener = listener;
  }

  public String[] getItems() {
    return mItems;
  }

  public void setItems(String[] items) {
    this.mItems = items;
    for (CharSequence s : mItems) {
      TextView t = new TextView(getContext());
      t.setText(s);
      t.setTextSize(10);
      LayoutParams params = new LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, 0);
      params.weight = 1;
      t.setLayoutParams(params);
      t.setTextColor(getResources().getColor(R.color.black));
      addView(t);
    }
  }

  @Override public boolean onTouchEvent(MotionEvent ev) {
    final int action = ev.getAction();
    TextView child = null;

    switch (action & MotionEvent.ACTION_MASK) {
      case MotionEvent.ACTION_DOWN:
        setBackgroundResource(R.drawable.cirrect_10a_primary_solid);
        child = findChildByLocation(ev.getX(), ev.getY());
        if (listener != null) {
          listener.onPositionSelected(child == null ? "" : child.getText().toString());
        }
        break;
      case MotionEvent.ACTION_MOVE:
        setBackgroundResource(R.drawable.cirrect_10a_primary_solid);
        child = findChildByLocation(ev.getX(), ev.getY());
        if (listener != null) {
          listener.onPositionSelected(child == null ? "" : child.getText().toString());
        }
        break;
      case MotionEvent.ACTION_UP:
        setBackgroundResource(R.drawable.cirrect_5a_primary_solid);
        if (listener != null) {
          listener.onPositionSelected("");
        }
        break;
      default:
        break;
    }

    return true;
  }

  private TextView findChildByLocation(float x, float y) {
    TextView child = null;
    int mContentTop = getChildAt(0).getTop();
    int mContentBottom = getChildAt(getChildCount() - 1).getBottom();
    int defSize = (mContentBottom - mContentTop) / mItems.length;

    int index = (int) ((y - mContentTop) / defSize);
    if (index >= 0 && index < mItems.length && x >= 0 && x <= getWidth()) {
      child = (TextView) getChildAt(index);
    }
    return child;
  }

  public interface OnPositionChangedListener {
    void onPositionSelected(String key);
  }
}
