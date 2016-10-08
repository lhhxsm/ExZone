package com.exzone.lib.util;

import android.database.Cursor;
import android.os.ParcelFileDescriptor;

import java.io.Closeable;
import java.io.IOException;

/**
 * 作者:李鸿浩
 * 描述:
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
}
