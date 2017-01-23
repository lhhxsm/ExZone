package com.exzone.lib.util;

import android.content.Context;
import android.text.TextUtils;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;

/**
 * 作者:李鸿浩
 * 描述:资源文件工具类
 * 时间:2016/7/9.
 */
public class ResourcesUtils {

    private ResourcesUtils() {
        throw new AssertionError();
    }

    /**
     * 从assert文件夹下读取文本资源
     */
    public static String readFileFromAssets(Context context, String fileName) {
        if (context == null || TextUtils.isEmpty(fileName)) {
            return null;
        }

        StringBuilder s = new StringBuilder("");
        try {
            InputStreamReader in = new InputStreamReader(context.getResources().getAssets().open(fileName));
            BufferedReader br = new BufferedReader(in);
            String line;
            while ((line = br.readLine()) != null) {
                s.append(line);
            }
            return s.toString();
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    public static List<String> readFileListFromAssets(Context context, String fileName) {
        if (context == null || TextUtils.isEmpty(fileName)) {
            return null;
        }

        List<String> fileContent = new ArrayList<String>();
        try {
            InputStreamReader in = new InputStreamReader(context.getResources().getAssets().open(fileName));
            BufferedReader br = new BufferedReader(in);
            String line;
            while ((line = br.readLine()) != null) {
                fileContent.add(line);
            }
            br.close();
            return fileContent;
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * 从assert文件夹下读取文本资源
     *
     * @param context  上下文
     * @param fileName 文件名
     * @return 文件内容字符串
     */
    public static String readStringFromAssert(Context context, String fileName) {
        String result = null;
        byte[] buffer = readBytesFromAssert(context, fileName);
        try {
            result = new String(buffer, "UTF-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return result;
    }

    /**
     * 从assert文件夹下读取文件到字节数组
     *
     * @param context  上下文
     * @param fileName 文件名
     * @return 文件字节数组
     */
    public static byte[] readBytesFromAssert(Context context, String fileName) {
        InputStream is = null;
        byte[] buffer = null;
        try {
            is = context.getAssets().open(fileName);
            int size = is.available();
            buffer = new byte[size];
            is.read(buffer);
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (is != null) {
                try {
                    is.close();
                    is = null;
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }

        return buffer;
    }


    /**
     * 从raw文件夹下读取文本资源
     */
    public static String readFileFromRaw(Context context, int resId) {
        if (context == null) {
            return null;
        }

        StringBuilder s = new StringBuilder();
        try {
            InputStreamReader in = new InputStreamReader(context.getResources().openRawResource(resId));
            BufferedReader br = new BufferedReader(in);
            String line;
            while ((line = br.readLine()) != null) {
                s.append(line);
            }
            return s.toString();
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    public static List<String> readFileListFromRaw(Context context, int resId) {
        if (context == null) {
            return null;
        }

        List<String> fileContent = new ArrayList<String>();
        BufferedReader reader = null;
        try {
            InputStreamReader in = new InputStreamReader(context.getResources().openRawResource(resId));
            reader = new BufferedReader(in);
            String line = null;
            while ((line = reader.readLine()) != null) {
                fileContent.add(line);
            }
            reader.close();
            return fileContent;
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * 从raw文件夹下读取文本资源
     *
     * @param context 上下文
     * @param rawId   raw资源id
     * @return 文件内容字符串
     */
    public static String readStringFromRaw(Context context, int rawId) {
        String result = null;
        byte[] buffer = readBytesFromRaw(context, rawId);
        try {
            result = new String(buffer, "UTF-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return result;
    }

    /**
     * 从raw文件夹下读取文件到字节数组
     *
     * @param context 上下文
     * @param rawId   raw资源id
     * @return 文件字节数组
     */
    public static byte[] readBytesFromRaw(Context context, int rawId) {
        InputStream is = null;
        byte[] buffer = null;
        try {
            is = context.getResources().openRawResource(rawId);
            int size = is.available();
            buffer = new byte[size];
            is.read(buffer);
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (is != null) {
                try {
                    is.close();
                    is = null;
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return buffer;
    }
}
