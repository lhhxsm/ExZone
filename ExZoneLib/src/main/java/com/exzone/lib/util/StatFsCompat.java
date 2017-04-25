package com.exzone.lib.util;

import android.os.Build;
import android.os.Environment;
import android.os.StatFs;

/**
 * 作者:李鸿浩
 * 描述:StatFs兼容类:由于StatFs的API从API18之后发生了较大的变化，主要是Android官方认识到之前的API用起来不方便并且有坑，于是新添加了几个方法来替代旧的方法
 * 时间: 2016/10/10.
 */
public class StatFsCompat {

  private StatFs mStatFs;

  public StatFsCompat(String dir) {
    this.mStatFs = new StatFs(dir);
  }

  /**
   * 创建一个用于查询外部存储（Environment.getExternalStorageDirectory()）容量的StatFsCompat
   *
   * @return null：外部存储不可用
   */
  @SuppressWarnings("deprecation") public static StatFsCompat newByExternalStorage() {
    if (!Environment.MEDIA_MOUNTED.equals(Environment.getExternalStorageState())) {
      return null;
    }
    return new StatFsCompat(Environment.getExternalStorageDirectory().getPath());
  }

  /**
   * 创建一个用于查询内部存储（/data）容量的StatFsCompat
   *
   * @return null：内部存储不可用
   */
  @SuppressWarnings("deprecation") public static StatFsCompat newByInternalStorage() {
    return new StatFsCompat("/data");
  }

  /**
   * 获取可用字节,单位byte
   */
  @SuppressWarnings("deprecation") public long getAvailableBytes() {
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR2) {
      return mStatFs.getAvailableBytes();
    } else {
      return (long) mStatFs.getAvailableBlocks() * mStatFs.getBlockSize();
    }
  }

  /**
   * 获取总字节,单位byte
   */
  @SuppressWarnings("deprecation") public long getTotalBytes() {
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR2) {
      return mStatFs.getTotalBytes();
    } else {
      return (long) mStatFs.getBlockCount() * mStatFs.getBlockSize();
    }
  }

  /**
   * 获取剩余字节,单位byte
   */
  @SuppressWarnings("deprecation") public long getFreeBytes() {
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR2) {
      return mStatFs.getFreeBytes();
    } else {
      return (long) mStatFs.getFreeBlocks() * mStatFs.getBlockSize();
    }
  }
}
