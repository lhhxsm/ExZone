package com.android.lib.ioc;

import android.app.Activity;
import android.view.View;

/**
 * 作者:lhh
 * 描述:View的findViewById的辅助类
 * 时间:2017/5/4.
 */
public class ViewFinder {
  private Activity mActivity;
  private View mView;

  public ViewFinder(Activity activity) {
    this.mActivity = activity;
  }

  public ViewFinder(View view) {
    this.mView = view;
  }

  public View findViewById(int viewId) {
    return mActivity != null ? mActivity.findViewById(viewId) : mView.findViewById(viewId);
  }
}
