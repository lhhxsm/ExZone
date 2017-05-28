package com.android.lib.utils;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.channels.FileChannel;

/**
 * 作者:李鸿浩
 * 描述:
 * 时间:2017/5/13.
 */
public class FileUtil {
  /**
   * 复制文件
   *
   * @param src 源文件
   * @param dest 目标文件
   * @throws IOException
   */
  public static void copyFile(File src, File dest) throws IOException {
    FileChannel inChannel = null;
    FileChannel outChannel = null;
    try {
      if (!dest.exists()) {
        dest.createNewFile();
      }
      inChannel = new FileInputStream(src).getChannel();
      outChannel = new FileOutputStream(dest).getChannel();
      inChannel.transferTo(0, inChannel.size(), outChannel);
    } finally {
      if (inChannel != null) {
        inChannel.close();
      }
      if (outChannel != null) {
        outChannel.close();
      }
    }
  }

  /**
   * 删除dir文件目录,包括目录下的所有文件。如果任何文件不能被删除,或者如果dir文件不是一个可读的目录,则抛出一个IOException。
   */
  public static void deleteDir(File dir) throws IOException {
    File[] files = dir.listFiles();
    if (files == null) {
      throw new IOException("not a readable directory: " + dir);
    }
    for (File file : files) {
      if (file.isDirectory()) {
        deleteDir(file);
      }
      if (!file.delete()) {
        throw new IOException("failed to delete file: " + file);
      }
    }
  }
}
