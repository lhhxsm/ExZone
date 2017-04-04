package com.exzone.lib.cache;

import android.content.Context;
import android.graphics.Bitmap;

import com.exzone.lib.util.Logger;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

/**
 * 作者:李鸿浩
 * 描述:
 * 时间:2017/4/3.
 */
public class FileCache {
    private File cacheDir;
    public final String cachePath = "/mnt/sdcard/DCIM/";
    public final String imgCacheDir = "CacheDir/";
    // 机型适配
    public final String cachePath2 = "/sdcard/DCIM/";

    /**
     * 优先存放SD卡目录 其次系统默认缓存
     */
    public FileCache(Context context) {
        if (hasSDCard()) {
            cacheDir = createFilePath(cachePath + imgCacheDir);
            if (!cacheDir.exists()) {
                cacheDir = createFilePath(cachePath2 + imgCacheDir);
            }
        } else {
            cacheDir = createFilePath(context.getCacheDir() + imgCacheDir);
        }
    }

    /**
     * 初始化检查SD卡
     */
    public boolean hasSDCard() {
        return android.os.Environment.getExternalStorageState().equals(android.os.Environment.MEDIA_MOUNTED);
    }

    /**
     * 初始化路径
     *
     * @return boolean
     */
    private File createFilePath(String filePath) {
        return createFilePath(new File(filePath));
    }

    private File createFilePath(File file) {
        if (!file.exists()) {
            boolean mkdirs = file.mkdirs();// 按照文件夹路径创建文件夹
            if (!mkdirs) {
                return null;
            }
        }
        return file;
    }

    /**
     * 保存Bitmap到SdCard,返回本地文件路径,不插入记录到数据库
     *
     * @return String
     */
    public String addBitmapToSdCard(Bitmap bitmap) {
        if (bitmap == null) {
            return null;
        }

        try {
            String fileName = System.currentTimeMillis() + ".jpg";
            // 创建文件夹
            createFilePath(cacheDir);
            File bitmapFile = new File(cacheDir, fileName);
            boolean newFile = bitmapFile.createNewFile();
            if (!newFile) {
                return null;
            }
            BufferedOutputStream bos = new BufferedOutputStream(new FileOutputStream(bitmapFile));
            bitmap.compress(Bitmap.CompressFormat.PNG, 100, bos);
            bos.flush();
            bos.close();
            return bitmapFile.getAbsolutePath();
        } catch (IOException e) {
            e.printStackTrace();
            Logger.e("FileCache中:保存图片错误-------为：" + e.getMessage());
        }
        return null;
    }
}
