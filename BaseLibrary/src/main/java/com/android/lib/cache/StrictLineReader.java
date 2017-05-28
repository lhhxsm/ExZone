/*
 * Copyright (C) 2012 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.android.lib.cache;

import com.android.lib.Constants;
import java.io.ByteArrayOutputStream;
import java.io.Closeable;
import java.io.EOFException;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.nio.charset.Charset;

/**
 * 从InputStream输入的缓冲区用于读取行。 行是以“\ n”或“\ r \ n”结尾。
 * 读取结束抛出EOFException。
 * hasUnterminatedLine用于判断是否读取完成。
 * 该类仅支持将'\ r'和'\ n'编码为单个字节，分别为13和10的字符集，而没有其他字符的表示包含这些值。
 * 默认字符集是US_ASCII。
 */
public class StrictLineReader implements Closeable {
  private static final byte CR = (byte) '\r';
  private static final byte LF = (byte) '\n';
  private final InputStream in;
  private final Charset charset;

  /*
缓冲数据存储在buf中。只要不出现异常，0小于=pos
而在范围pos终端中的数据)是用来进行读取的。在输入端，如果有的话
一个未终止的行，我们设置结束==-1，否则结束==pos。
@code InputStream抛出一个@code IOException，结束可能保留为pos或-1。
   */
  private byte[] buf;
  private int pos;
  private int end;

  /**
   * Constructs a new {@code LineReader} with the specified charset and the default capacity.
   *
   * @param in the {@code InputStream} to read data from.
   * @param charset the charset used to decode data. Only US-ASCII, UTF-8 and ISO-8859-1 are
   * supported.
   * @throws NullPointerException if {@code in} or {@code charset} is null.
   * @throws IllegalArgumentException if the specified charset is not supported.
   */
  public StrictLineReader(InputStream in, Charset charset) {
    this(in, 8192, charset);
  }

  /**
   * Constructs a new {@code LineReader} with the specified capacity and charset.
   *
   * @param in the {@code InputStream} to read data from.
   * @param capacity the capacity of the buffer.
   * @param charset the charset used to decode data. Only US-ASCII, UTF-8 and ISO-8859-1 are
   * supported.
   * @throws NullPointerException if {@code in} or {@code charset} is null.
   * @throws IllegalArgumentException if {@code capacity} is negative or zero
   * or the specified charset is not supported.
   */
  public StrictLineReader(InputStream in, int capacity, Charset charset) {
    if (in == null || charset == null) {
      throw new NullPointerException();
    }
    if (capacity < 0) {
      throw new IllegalArgumentException("capacity <= 0");
    }
    if (!(charset.equals(Constants.US_ASCII))) {
      throw new IllegalArgumentException("Unsupported encoding");
    }

    this.in = in;
    this.charset = charset;
    buf = new byte[capacity];
  }

  /**
   * Closes the reader by closing the underlying {@code InputStream} and
   * marking this reader as closed.
   *
   * @throws IOException for errors when closing the underlying {@code InputStream}.
   */
  public void close() throws IOException {
    synchronized (in) {
      if (buf != null) {
        buf = null;
        in.close();
      }
    }
  }

  /**
   * Reads the next line. A line ends with {@code "\n"} or {@code "\r\n"},
   * this end of line marker is not included in the result.
   *
   * @return the next line from the input.
   * @throws IOException for underlying {@code InputStream} errors.
   * @throws EOFException for the end of source stream.
   */
  public String readLine() throws IOException {
    synchronized (in) {
      if (buf == null) {
        throw new IOException("LineReader is closed");
      }

      // Read more data if we are at the end of the buffered data.
      // Though it's an error to read after an exception, we will let {@code fillBuf()}
      // throw again if that happens; thus we need to handle end == -1 as well as end == pos.
      if (pos >= end) {
        fillBuf();
      }
      // Try to find LF in the buffered data and return the line if successful.
      for (int i = pos; i != end; ++i) {
        if (buf[i] == LF) {
          int lineEnd = (i != pos && buf[i - 1] == CR) ? i - 1 : i;
          String res = new String(buf, pos, lineEnd - pos, charset.name());
          pos = i + 1;
          return res;
        }
      }

      // Let's anticipate up to 80 characters on top of those already read.
      ByteArrayOutputStream out = new ByteArrayOutputStream(end - pos + 80) {
        @Override public String toString() {
          int length = (count > 0 && buf[count - 1] == CR) ? count - 1 : count;
          try {
            return new String(buf, 0, length, charset.name());
          } catch (UnsupportedEncodingException e) {
            throw new AssertionError(e); // Since we control the charset this will never happen.
          }
        }
      };

      while (true) {
        out.write(buf, pos, end - pos);
        // Mark unterminated line in case fillBuf throws EOFException or IOException.
        end = -1;
        fillBuf();
        // Try to find LF in the buffered data and return the line if successful.
        for (int i = pos; i != end; ++i) {
          if (buf[i] == LF) {
            if (i != pos) {
              out.write(buf, pos, i - pos);
            }
            pos = i + 1;
            return out.toString();
          }
        }
      }
    }
  }

  /**
   * 是否还有未读取的行
   */
  public boolean hasUnterminatedLine() {
    return end == -1;
  }

  /**
   * 将新的输入数据读取到缓冲区中。
   * 如果函数抛出EOFException,表示读取完成
   */
  private void fillBuf() throws IOException {
    int result = in.read(buf, 0, buf.length);
    if (result == -1) {
      throw new EOFException();
    }
    pos = 0;
    end = result;
  }
}

