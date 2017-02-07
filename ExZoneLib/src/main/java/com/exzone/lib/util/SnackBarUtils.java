package com.exzone.lib.util;

import android.content.Context;
import android.support.design.widget.Snackbar;
import android.view.View;

/**
 * 作者:李鸿浩
 * 描述:
 * 时间:2016/7/9.
 */
public class SnackBarUtils {

    private SnackBarUtils() {
        throw new AssertionError();
    }

    public static void makeSnackBarShort(Context context, View view, String message) {
        KeyBoardUtils.hideKeyboard(context, view);
        Snackbar.make(view, message, Snackbar.LENGTH_SHORT).show();
    }

    public static void makeSnackBarLong(Context context, View view, String message) {
        KeyBoardUtils.hideKeyboard(context, view);
        Snackbar.make(view, message, Snackbar.LENGTH_LONG).show();
    }

    public static void makeSnackBarShortByCallBack(Context context, View view, String message, String actionMessage, View.OnClickListener onClickListener) {
        KeyBoardUtils.hideKeyboard(context, view);
        Snackbar.make(view, message, Snackbar.LENGTH_SHORT).setAction(actionMessage, onClickListener).show();
    }

    public static void makeSnackBarLongByCallBack(Context context, View view, String message, String actionMessage, View.OnClickListener onClickListener) {
        KeyBoardUtils.hideKeyboard(context, view);
        Snackbar.make(view, message, Snackbar.LENGTH_LONG).setAction(actionMessage, onClickListener).show();
    }

    public static void makeSnackBarIndefiniteByCallBack(Context context, View view, String message, String actionMessage, View.OnClickListener onClickListener) {
        KeyBoardUtils.hideKeyboard(context, view);
        Snackbar.make(view, message, Snackbar.LENGTH_INDEFINITE).setAction(actionMessage, onClickListener).show();
    }
}
