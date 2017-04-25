package com.exzone.lib.util;

import java.nio.ByteBuffer;

/**
 * 作者:李鸿浩
 * 描述:
 * 时间:2017/4/3.
 */
public class ByteUtils {
  public static byte[] bufferToBytes(ByteBuffer byteBuffer) {
    byte[] bytes = new byte[byteBuffer.remaining()];
    byteBuffer.get(bytes, 0, bytes.length);
    return bytes;
  }

  public static ByteBuffer bytesToBuffer(byte[] bytes) {
    return ByteBuffer.wrap(bytes);
  }
}
