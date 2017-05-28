package com.android.lib.dialog;

import android.content.Context;
import android.content.DialogInterface;
import android.util.SparseArray;
import android.view.View;
import android.view.Window;

/**
 * 作者:李鸿浩
 * 描述:
 * 时间:2017/5/17.
 */
class AlertController {
  private AlertDialog mDialog;
  private Window mWindow;

  public AlertController(AlertDialog dialog, Window window) {
    this.mDialog = dialog;
    this.mWindow = window;
  }

  /**
   * 获取Dialog
   */
  public AlertDialog getDialog() {
    return mDialog;
  }

  /**
   * 获取Dialog的Window
   */
  public Window getWindow() {
    return mWindow;
  }

  public static class AlertParams {
    public Context mContext;
    public int mThemeResId;
    //点击空白是否可以取消
    public boolean mCancelable = false;
    //dialog 监听
    public DialogInterface.OnCancelListener mOnCancelListener;
    public DialogInterface.OnDismissListener mOnDismissListener;
    public DialogInterface.OnKeyListener mOnKeyListener;
    //布局View
    public View mView;
    //布局Layout Id
    public int mViewLayoutResId;
    public SparseArray<CharSequence> mTextArray = new SparseArray();
    public SparseArray<View.OnClickListener> mClickArray = new SparseArray();

    public AlertParams(Context context, int themeResId) {
      this.mContext = context;
      this.mThemeResId = themeResId;
    }

    /**
     * 绑定和设置参数
     */
    public void apply(AlertController controller) {

    }
  }
}
