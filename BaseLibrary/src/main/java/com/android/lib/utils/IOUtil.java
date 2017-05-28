package com.android.lib.utils;

import java.io.Closeable;
import java.io.IOException;
import java.io.Reader;
import java.io.StringWriter;

/**
 * IO操作工具类
 */

public class IOUtil {

  /**
   * 读取内容
   */
  public static String readFully(Reader reader) throws IOException {
    try {
      StringWriter writer = new StringWriter();
      char[] buffer = new char[1024];
      int count;
      while ((count = reader.read(buffer)) != -1) {
        writer.write(buffer, 0, count);
      }
      return writer.toString();
    } finally {
      reader.close();
    }
  }

  /**
   * 关闭IO流
   */
  public static void closeQuietly(Closeable closeable) {
    if (closeable != null) {
      try {
        closeable.close();
      } catch (RuntimeException rethrown) {
        throw rethrown;
      } catch (Exception e) {
        e.printStackTrace();
        Logger.e(e.getMessage());
      }
    }
  }
}
