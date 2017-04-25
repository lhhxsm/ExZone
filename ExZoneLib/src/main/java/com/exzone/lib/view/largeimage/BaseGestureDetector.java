package com.exzone.lib.view.largeimage;

import android.content.Context;
import android.view.MotionEvent;

/**
 * 作者:李鸿浩
 * 描述:
 * 时间: 2017/1/20.
 */

public abstract class BaseGestureDetector {

  protected boolean mGestureInProgress;

  protected MotionEvent mPreMotionEvent;
  protected MotionEvent mCurrentMotionEvent;

  protected Context mContext;

  public BaseGestureDetector(Context context) {
    mContext = context;
  }

  public boolean onTouchEvent(MotionEvent event) {
    if (!mGestureInProgress) {
      handleStartProgressEvent(event);
    } else {
      handleInProgressEvent(event);
    }

    return true;
  }

  protected abstract void handleInProgressEvent(MotionEvent event);

  protected abstract void handleStartProgressEvent(MotionEvent event);

  protected abstract void updateStateByEvent(MotionEvent event);

  protected void resetState() {
    if (mPreMotionEvent != null) {
      mPreMotionEvent.recycle();
      mPreMotionEvent = null;
    }
    if (mCurrentMotionEvent != null) {
      mCurrentMotionEvent.recycle();
      mCurrentMotionEvent = null;
    }
    mGestureInProgress = false;
  }
}
