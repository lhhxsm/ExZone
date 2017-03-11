package com.exzone.lib.dialog;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.support.v7.app.AlertDialog;

import com.exzone.lib.R;


/**
 * 作者:李鸿浩
 * 描述:对话框工具类, 提供常用对话框显示, 使用support.v7包内的AlertDialog样式
 * 时间: 2017/2/7.
 */

public class DialogUtils {
    public static Dialog createProgressDialog(Context context) {
        return createProgressDialog(context, true);
    }

    public static Dialog createProgressDialog(Context context, boolean needCancel) {
        ProgressDialog dialog = new ProgressDialog(context);
        dialog.setMessage("Loading ...");
        dialog.setCancelable(needCancel);
        dialog.setCanceledOnTouchOutside(false);
        return dialog;
    }

    public static Dialog showCommonDialog(Context context, String message, DialogInterface.OnClickListener listener) {
        return new AlertDialog.Builder(context)
                .setMessage(message)
                .setPositiveButton(context.getString(R.string.sure), listener)
                .setNegativeButton(context.getString(R.string.cancel), null)
                .show();
    }

    public static Dialog showConfirmDialog(Context context, String message, DialogInterface.OnClickListener listener) {
        return new AlertDialog.Builder(context)
                .setMessage(message)
                .setPositiveButton(context.getString(R.string.sure), listener)
                .show();
    }
}
