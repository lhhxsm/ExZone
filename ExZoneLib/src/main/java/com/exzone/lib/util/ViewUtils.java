package com.exzone.lib.util;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.res.Configuration;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.view.ViewParent;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;
import com.exzone.lib.R;
import com.exzone.lib.dialog.ConfirmDialog;
import rx.functions.Action1;

/**
 * 作者:李鸿浩
 * 描述:视图工具类
 * 时间：2016/10/10.
 */

public final class ViewUtils {
  private ViewUtils() {
  }

  /**
   * 移除自身布局
   *
   * @param v 被移除的View
   */
  public static void removeParent(View v) {
    // 先找到爹 在通过爹去移除孩子
    ViewParent parent = v.getParent();
    // 所有的控件 都有爹 爹一般情况下 就是ViewGroup
    if (parent instanceof ViewGroup) {
      ViewGroup group = (ViewGroup) parent;
      group.removeView(v);
    }
  }

  /**
   * 修改普通View的高
   */
  public static void changeH(View v, int H) {
    if (v == null) {
      return;
    }
    LayoutParams params = v.getLayoutParams();
    params.height = H;
    v.setLayoutParams(params);
  }

  /**
   * 修改普通View的宽
   */
  public static void changeW(View v, int W) {
    if (v == null) {
      return;
    }
    LayoutParams params = v.getLayoutParams();
    params.width = W;
    v.setLayoutParams(params);
  }

  /**
   * 修改控件的宽高
   */
  public static void changeWH(View v, int W, int H) {
    if (v == null) {
      return;
    }
    LayoutParams params = v.getLayoutParams();
    params.width = W;
    params.height = H;
    v.setLayoutParams(params);
  }

  public static float getScreenDensity(final @NonNull Context context) {
    return context.getResources().getDisplayMetrics().density;
  }

  public static int getScreenHeightDp(final @NonNull Context context) {
    return context.getResources().getConfiguration().screenHeightDp;
  }

  public static int getScreenWidthDp(final @NonNull Context context) {
    return context.getResources().getConfiguration().screenWidthDp;
  }

  public static boolean isFontScaleLarge(final @NonNull Context context) {
    return context.getResources().getConfiguration().fontScale > 1.5f;
  }

  public static boolean isLandscape(final @NonNull Context context) {
    return context.getResources().getConfiguration().orientation
        == Configuration.ORIENTATION_LANDSCAPE;
  }

  public static boolean isPortrait(final @NonNull Context context) {
    return !isLandscape(context);
  }

  /**
   * Set layout margins for a ViewGroup with LinearLayout parent.
   */
  public static void setLinearViewGroupMargins(final @NonNull ViewGroup viewGroup,
      final int leftMargin, final int topMargin, final int rightMargin, final int bottomMargin) {
    final LinearLayout.LayoutParams layoutParams =
        new LinearLayout.LayoutParams(viewGroup.getLayoutParams());
    layoutParams.setMargins(leftMargin, topMargin, rightMargin, bottomMargin);
    viewGroup.setLayoutParams(layoutParams);
  }

  /**
   * Set layout margins for a ViewGroup with RelativeLayout parent.
   */
  public static void setRelativeViewGroupMargins(final @NonNull ViewGroup viewGroup,
      final int leftMargin, final int topMargin, final int rightMargin, final int bottomMargin) {
    final RelativeLayout.LayoutParams layoutParams =
        new RelativeLayout.LayoutParams(viewGroup.getLayoutParams());
    layoutParams.setMargins(leftMargin, topMargin, rightMargin, bottomMargin);
    viewGroup.setLayoutParams(layoutParams);
  }

  /**
   * Show a dialog box to the user.
   */
  public static void showDialog(final @NonNull Context context, final @Nullable String title,
      final @NonNull String message) {
    new ConfirmDialog(context, title, message).show();
  }

  public static void showDialog(final @NonNull Context context, final @Nullable String title,
      final @NonNull String message, final @NonNull String buttonMessage) {
    new ConfirmDialog(context, title, message, buttonMessage).show();
  }

  /**
   * Show a toast with default bottom gravity to the user.
   */
  @SuppressLint("InflateParams") public static void showToast(final @NonNull Context context,
      final @NonNull String message) {
    final LayoutInflater inflater =
        (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    final View view = inflater.inflate(R.layout.toast, null);
    final TextView text = (TextView) view.findViewById(R.id.toast_text_view);
    text.setText(message);

    final Toast toast = new Toast(context);
    toast.setView(view);
    toast.show();
  }

  @SuppressLint("InflateParams")
  public static void showToastFromTop(final @NonNull Context context, final @NonNull String message,
      final int xOffset, final int yOffset) {
    final LayoutInflater inflater =
        (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    final View view = inflater.inflate(R.layout.toast, null);
    final TextView text = (TextView) view.findViewById(R.id.toast_text_view);
    text.setText(message);

    final Toast toast = new Toast(context);
    toast.setView(view);
    toast.setGravity(Gravity.TOP | Gravity.CENTER_HORIZONTAL, xOffset, yOffset);
    toast.show();
  }

  public static Action1<String> showToast(final @NonNull Context context) {
    return new Action1<String>() {
      @Override public void call(String message) {
        showToast(context, message);
      }
    };
  }

  /**
   * Sets the visibility of a view to {@link View#VISIBLE} or {@link View#GONE}. Setting
   * the view to GONE removes it from the layout so that it no longer takes up any space.
   */
  public static void setGone(final @NonNull View view, final boolean gone) {
    if (gone) {
      view.setVisibility(View.GONE);
    } else {
      view.setVisibility(View.VISIBLE);
    }
  }

  public static Action1<Boolean> setGone(final @NonNull View view) {
    return new Action1<Boolean>() {
      @Override public void call(Boolean gone) {
        setGone(view, gone);
      }
    };
  }

  /**
   * Sets the visibility of a view to {@link View#VISIBLE} or {@link View#INVISIBLE}. Setting
   * the view to INVISIBLE makes it hidden, but it still takes up space.
   */
  public static void setInvisible(final @NonNull View view, final boolean hidden) {
    if (hidden) {
      view.setVisibility(View.INVISIBLE);
    } else {
      view.setVisibility(View.VISIBLE);
    }
  }

  public static Action1<Boolean> setInvisible(final @NonNull View view) {
    return new Action1<Boolean>() {
      @Override public void call(Boolean invisible) {
        setInvisible(view, invisible);
      }
    };
  }
}
