package com.exzone.lib.util;

import android.database.Cursor;
import android.os.ParcelFileDescriptor;
import android.support.annotation.NonNull;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.ByteArrayOutputStream;
import java.io.Closeable;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;

/**
 * 作者:李鸿浩
 * 描述:IO工具类
 * 时间：2016/10/8.
 */
public class IOUtils {

    public static void close(Closeable c) {
        if (c == null)
            return;
        try {
            c.close();
        } catch (Throwable t) {
            Logger.e("fail to close:" + t);
        }
    }

    public static void close(ParcelFileDescriptor descriptor) {
        if (descriptor == null)
            return;
        try {
            descriptor.close();
        } catch (Throwable t) {
            Logger.e("fail to close:" + t);
        }
    }

    public static void close(Cursor cursor) {
        try {
            if (cursor != null) cursor.close();
        } catch (Throwable t) {
            Logger.e("fail to close:" + t);
        }
    }

    /**
     * 文本的写入操作
     *
     * @param filePath 文件路径。一定要加上文件名字 <br>
     *                 例如：../a/a.txt
     * @param content  写入内容
     */
    public static void write(String filePath, String content) {
        BufferedWriter bw = null;
        try {
            bw = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(filePath)));
            bw.write(content);
        } catch (Exception e1) {
            e1.printStackTrace();
        } finally {
            if (bw != null) {
                try {
                    bw.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    /**
     * 文本的读取操作
     *
     * @param path 文件路径,一定要加上文件名字<br>
     *             例如：../a/a.txt
     * @return
     */
    public static String read(String path) {
        BufferedReader br = null;
        try {
            br = new BufferedReader(new InputStreamReader(new FileInputStream(path)));
            StringBuffer sb = new StringBuffer();
            String str = null;
            while ((str = br.readLine()) != null) {
                sb.append(str);
            }
            return sb.toString();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (br != null) {
                try {
                    br.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return null;
    }

    /**
     * 文本的读取操作
     *
     * @param is 文件路径,一定要加上文件名字<br>
     *           例如：../a/a.txt
     * @return
     */
    public static String read(InputStream is) {
        BufferedReader br = null;
        try {
            br = new BufferedReader(new InputStreamReader(is));
            StringBuffer sb = new StringBuffer();
            String str = null;
            while ((str = br.readLine()) != null) {
                sb.append(str);
            }
            return sb.toString();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (br != null) {
                try {
                    br.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return null;
    }

    public static byte[] readFully(final @NonNull InputStream inputStream) throws IOException {
        final ByteArrayOutputStream out = new ByteArrayOutputStream();
        final byte[] buffer = new byte[1024];
        for (int count; (count = inputStream.read(buffer)) != -1; ) {
            out.write(buffer, 0, count);
        }
        return out.toByteArray();
    }
}
