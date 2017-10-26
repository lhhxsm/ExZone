package com.exzone.lib.base;

import android.annotation.TargetApi;
import android.app.Activity;
import android.os.Build;
import android.support.annotation.NonNull;

/**
 * 作者:lhh
 * 描述:
 * 时间:2017/4/25.
 */
public class ActivityUtils {
  private ActivityUtils() {
    throw new AssertionError();
  }

  /**
   * Restart the Activity
   */
  @TargetApi(Build.VERSION_CODES.HONEYCOMB) public static void restartActivity(
      @NonNull Activity activity) {
    //        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
    activity.recreate();
    //        } else {
    //            Intent intent = activity.getIntent();
    //            activity.overridePendingTransition(0, 0);
    //            intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
    //            activity.finish();
    //
    //            activity.overridePendingTransition(0, 0);
    //            activity.startActivity(intent);
    //        }
  }
}
