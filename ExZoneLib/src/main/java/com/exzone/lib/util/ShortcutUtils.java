package com.exzone.lib.util;

import android.app.Activity;
import android.content.ComponentName;
import android.content.ContentResolver;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import com.exzone.lib.R;

/**
 * 作者:李鸿浩
 * 描述:桌面快捷图标 需要权限: com.android.launcher.permission.INSTALL_SHORTCUT com.android.launcher.permission.UNINSTALL_SHORTCUT
 * 时间: 2016/10/9.
 */
public class ShortcutUtils {
  private ShortcutUtils() {
    throw new AssertionError();
  }

  /**
   * 检测是否存在快捷键
   *
   * @return 是否存在桌面图标
   */
  public static boolean hasShortcut(Activity activity) {
    boolean isInstallShortcut = false;
    final ContentResolver cr = activity.getContentResolver();
    final String AUTHORITY = "com.android.launcher.settings";
    final Uri CONTENT_URI = Uri.parse("content://" + AUTHORITY + "/favorites?notify=true");
    Cursor c = cr.query(CONTENT_URI, new String[] { "title", "iconResource" }, "title=?",
        new String[] { activity.getString(R.string.app_name).trim() }, null);
    if (c != null && c.getCount() > 0) {
      isInstallShortcut = true;
    }
    return isInstallShortcut;
  }

  /**
   * 为程序创建桌面快捷方式
   *
   * @param name 快捷方式的名称
   * @param icon 快捷方式的图标
   */
  public static void addShortcut(Activity activity, String name, int icon) {
    Intent shortcut = new Intent("com.android.launcher.action.INSTALL_SHORTCUT");
    // 快捷方式的名称
    shortcut.putExtra(Intent.EXTRA_SHORTCUT_NAME, name);
    shortcut.putExtra("duplicate", false); // 不允许重复创建
    Intent shortcutIntent = new Intent(Intent.ACTION_MAIN);
    shortcutIntent.setClassName(activity, activity.getClass().getName());
    shortcut.putExtra(Intent.EXTRA_SHORTCUT_INTENT, shortcutIntent);
    // 快捷方式的图标
    Intent.ShortcutIconResource iconRes = Intent.ShortcutIconResource.fromContext(activity, icon);
    shortcut.putExtra(Intent.EXTRA_SHORTCUT_ICON_RESOURCE, iconRes);
    activity.sendBroadcast(shortcut);
  }

  /**
   * 删除程序的快捷方式
   *
   * @param name 快捷方式的名称
   */
  public static void delShortcut(Activity activity, String name) {
    Intent shortcut = new Intent("com.android.launcher.action.UNINSTALL_SHORTCUT");
    // 快捷方式的名称
    shortcut.putExtra(Intent.EXTRA_SHORTCUT_NAME, name);
    String appClass = activity.getPackageName() + "." + activity.getLocalClassName();
    ComponentName comp = new ComponentName(activity.getPackageName(), appClass);
    shortcut.putExtra(Intent.EXTRA_SHORTCUT_INTENT,
        new Intent(Intent.ACTION_MAIN).setComponent(comp));
    activity.sendBroadcast(shortcut);
  }
}
