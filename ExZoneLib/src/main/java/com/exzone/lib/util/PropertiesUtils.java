package com.exzone.lib.util;

import android.content.Context;

import com.exzone.lib.base.BaseApplication;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

/**
 * 作者:李鸿浩
 * 描述:配置文件工具类
 * 时间：2016/10/7.
 */
public class PropertiesUtils extends Properties {
    private static Properties property = new Properties();

    /**
     * 读取配置文件assets目录xxx.properties文件里面的内容
     */
    public static String readAssetsProp(String fileName, String key) {
        String value = "";
        try {
            InputStream in = BaseApplication.getInstance().getAssets().open(fileName);
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
            InputStream in = BaseApplication.getInstance().getAssets().open(fileName);
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

    public static Properties loadProperties(Context context, String fileName, String dirName) {
        Properties props = new Properties();
        try {
            int id = context.getResources().getIdentifier(fileName, dirName, context.getPackageName());
            props.load(context.getResources().openRawResource(id));
        } catch (Exception e) {
            Logger.e(e.toString());
        }
        return props;
    }

    /**
     * 读取Properties文件(指定目录)
     */
    public static Properties loadConfig(String file) {
        Properties properties = new Properties();
        FileInputStream s = null;
        try {
            s = new FileInputStream(file);
            properties.load(s);
        } catch (Exception e) {
            e.printStackTrace();
            Logger.e(e.toString());
        } finally {
            if (s != null) {
                try {
                    s.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return properties;
    }

    /**
     * 保存Properties(指定目录)
     */
    public static void saveConfig(String file, Properties properties) {
        FileOutputStream s = null;
        try {
            s = new FileOutputStream(file, false);
            properties.store(s, "");
        } catch (Exception e) {
            e.printStackTrace();
            Logger.e(e.toString());
        } finally {
            if (s != null) {
                try {
                    s.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    /**
     * 读取文件 文件在/data/data/package_name/files下 无法指定位置
     *
     * @return 设定文件
     */
    public static Properties loadConfigNoDirs(Context context, String fileName) {
        Properties properties = new Properties();
        try {
            FileInputStream s = context.openFileInput(fileName);
            properties.load(s);
        } catch (Exception e) {
            Logger.e(e.toString());
        }
        return properties;
    }

    /**
     * 保存文件到/data/data/package_name/files下 无法指定位置
     */
    public static void saveConfigNoDirs(Context context, String fileName, Properties properties) {
        try {
            FileOutputStream s = context.openFileOutput(fileName, Context.MODE_PRIVATE);
            properties.store(s, "");
        } catch (Exception e) {
            Logger.e(e.toString());
        }
    }

    public static Properties loadConfigAssets(Context context, String fileName) {
        Properties properties = new Properties();
        try {
            InputStream is = context.getAssets().open(fileName);
            properties.load(is);
        } catch (Exception e) {
            Logger.e(e.toString());
        }
        return properties;
    }
}
