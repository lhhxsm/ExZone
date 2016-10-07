package com.exzone.lib.util;

import android.content.Context;

import com.exzone.lib.base.BaseApplication;

import java.io.InputStream;
import java.util.Properties;

/**
 * 作者:李鸿浩
 * 描述:配置文件工具类,读取配置文件assets目录xxx.properties文件里面的内容
 * 时间：2016/10/7.
 */
public class PropertiesUtils extends Properties {
    private static Properties property = new Properties();

    public static String readAssetsProp(String fileName, String key) {
        String value = "";
        try {
            InputStream in = BaseApplication.sContext.getAssets().open(fileName);
            property.load(in);
            value = property.getProperty(key);
        } catch (Exception e1) {
            e1.printStackTrace();
        }

        return value;
    }

    public static String readAssetsProp(Context context, String fileName, String key) {
        String value = "";
        try {
            InputStream in = context.getAssets().open(fileName);
            property.load(in);
            value = property.getProperty(key);
        } catch (Exception e1) {
            e1.printStackTrace();
        }

        return value;
    }

    public static String readAssetsProp(String fileName, String key, String defaultValue) {
        String value = "";
        try {
            InputStream in = BaseApplication.sContext.getAssets().open(fileName);
            property.load(in);
            value = property.getProperty(key, defaultValue);
        } catch (Exception e1) {
            e1.printStackTrace();
        }

        return value;
    }

    public static String readAssetsProp(Context context, String fileName, String key, String defaultValue) {
        String value = "";
        try {
            InputStream in = context.getAssets().open(fileName);
            property.load(in);
            value = property.getProperty(key, defaultValue);
        } catch (Exception e1) {
            e1.printStackTrace();
        }

        return value;
    }
}
