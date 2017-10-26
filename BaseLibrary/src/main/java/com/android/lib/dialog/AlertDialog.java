package com.android.lib.dialog;

import android.content.Context;
import android.support.v7.app.AppCompatDialog;
import android.view.View;
import com.android.lib.R;

/**
 * 作者:lhh
 * 描述:
 * 时间:2017/5/17.
 */
public class AlertDialog extends AppCompatDialog {
  private AlertController mController;

  public AlertDialog(Context context, int themeResId) {
    super(context, themeResId);
    mController = new AlertController(this, getWindow());
  }

  public static class Builder {
    private final AlertController.AlertParams mParams;

    public Builder(Context context) {
      this(context, R.style.dialog);
    }

    public Builder(Context context, int themeResId) {
      mParams = new AlertController.AlertParams(context, themeResId);
    }

    /**
     * 设置布局
     */
    public Builder setContentView(View view) {
      mParams.mView = view;
      mParams.mViewLayoutResId = 0;
      return this;
    }

    public Builder setContentView(int LayoutId) {
      mParams.mView = null;
      mParams.mViewLayoutResId = LayoutId;
      return this;
    }

    public Builder setCancelable(boolean cancelable) {
      mParams.mCancelable = cancelable;
      return this;
    }

    public Builder setOnCancelListener(OnCancelListener onCancelListener) {
      mParams.mOnCancelListener = onCancelListener;
      return this;
    }

    public Builder setOnDismissListener(OnDismissListener onDismissListener) {
      mParams.mOnDismissListener = onDismissListener;
      return this;
    }

    public Builder setOnKeyListener(OnKeyListener onKeyListener) {
      mParams.mOnKeyListener = onKeyListener;
      return this;
    }

    public Builder setText(int resId, CharSequence text) {
      mParams.mTextArray.put(resId, text);
      return this;
    }

    public Builder setOnClickListener(int resId, View.OnClickListener listener) {
      mParams.mClickArray.put(resId, listener);
      return this;
    }

    public AlertDialog create() {
      final AlertDialog dialog = new AlertDialog(mParams.mContext, mParams.mThemeResId);
      mParams.apply(dialog.mController);
      dialog.setCancelable(mParams.mCancelable);
      if (mParams.mCancelable) {
        dialog.setCanceledOnTouchOutside(true);
      }
      dialog.setOnCancelListener(mParams.mOnCancelListener);
      dialog.setOnDismissListener(mParams.mOnDismissListener);
      if (mParams.mOnKeyListener != null) {
        dialog.setOnKeyListener(mParams.mOnKeyListener);
      }
      return dialog;
    }

    public AlertDialog show() {
      final AlertDialog dialog = create();
      dialog.show();
      return dialog;
    }
  }
}
