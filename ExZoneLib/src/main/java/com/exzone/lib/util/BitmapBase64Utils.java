package com.exzone.lib.util;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import java.io.ByteArrayOutputStream;
import java.io.UnsupportedEncodingException;

/**
 * 作者:李鸿浩
 * 描述:
 * 时间: 2017/1/18.
 */

public class BitmapBase64Utils {

    private BitmapBase64Utils() {
        throw new AssertionError();
    }

    public static String bitmap2String(Bitmap bitmap) {
        return encodeToString(bitmap2Bytes(bitmap));
    }

    public static Bitmap string2Bitmap(String string) {
        byte[] bytes = Base64Utils.decode(string);
        return BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
    }


    private static byte[] bitmap2Bytes(Bitmap bm) {
        if (bm == null) {
            return null;
        }
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bm.compress(Bitmap.CompressFormat.PNG, 100, baos);
        return baos.toByteArray();
    }

    public static String encodeToString(byte[] input) {
        try {
            return new String(Base64Utils.encode(input, 0, input.length), "US-ASCII");
        } catch (UnsupportedEncodingException e) {
            // US-ASCII is guaranteed to be available.
            throw new AssertionError(e);
        }
    }


}
