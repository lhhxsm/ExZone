package com.exzone.lib.util;

import android.annotation.TargetApi;
import android.app.Activity;
import android.support.annotation.ColorInt;
import android.support.annotation.NonNull;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;

import com.exzone.lib.constant.ApiCapabilities;

/**
 * 作者:李鸿浩
 * 描述:
 * 时间: 2017/2/6.
 */

public final class StatusBarUtils {
    private StatusBarUtils() {
    }

    @SuppressWarnings("InlinedApi")
    @TargetApi(21)
    public static void apply(final @NonNull Activity activity, final @ColorInt int color) {
        if (!ApiCapabilities.canSetStatusBarColor()) {
            return;
        }

        final Window window = activity.getWindow();
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        window.setStatusBarColor(color);
    }

    public static void apply(final @NonNull Activity activity, final @ColorInt int color,
                             final boolean overlayShouldBeLight) {
        apply(activity, color);
        setIconOverlay(activity, overlayShouldBeLight);
    }

    public static void setDarkStatusBarIcons(final @NonNull Activity activity) {
        setIconOverlay(activity, true);
    }

    public static void setLightStatusBarIcons(final @NonNull Activity activity) {
        setIconOverlay(activity, false);
    }

    @SuppressWarnings("InlinedApi")
    private static void setIconOverlay(final @NonNull Activity activity, final boolean light) {
        if (!ApiCapabilities.canSetDarkStatusBarIcons()) {
            return;
        }

        final int uiFlag = light ?
                View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR :
                View.SYSTEM_UI_FLAG_VISIBLE;
        final Window window = activity.getWindow();
        window.getDecorView().setSystemUiVisibility(uiFlag);
    }
}
