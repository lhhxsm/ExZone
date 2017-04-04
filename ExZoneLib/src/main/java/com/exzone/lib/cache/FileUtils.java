package com.exzone.lib.cache;

import android.content.Context;
import android.os.Environment;
import android.text.TextUtils;

import com.exzone.lib.util.Logger;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileWriter;

/**
 * 作者:李鸿浩
 * 描述:文件操作工具类,提供一套统一的文件操作util,以屏蔽底层的机型系统的差异性等情况。
 * 时间:2017/4/3.
 */
public class FileUtils {

    private static final String FILE_CACHE = "cache";
    private static final String FILE_TEMP = "temp";
    private static final String FILE_DOCUMENT = "document";

    /**
     * 获取app的系统目录
     *
     * @return app主路径
     */
    public static String getMainPath(Context context) {
        String appPath = "";
        appPath = context.getFilesDir().getAbsolutePath();
        return appPath;
    }

    /**
     * 获取app缓存路径，缓存路径在某些情况下会被清空
     *
     * @return 缓存路径
     */
    public static String getCachePath(Context context) {
        String appMainPath = getMainPath(context);
        String appCachePath = appMainPath + "/" + FILE_CACHE + "/";
        makeDir(appCachePath);
        return appCachePath;
    }

    /**
     * 获取app的临时文件路径，临时路径下的文件app退出会被清空
     *
     * @return 文件路径
     */
    public static String getTempPath(Context context) {
        String appMainPath = getMainPath(context);
        String appTempPath = appMainPath + "/" + FILE_TEMP + "/";
        makeDir(appTempPath);
        return appTempPath;
    }

    /**
     * 获取app文件路径，改路径下的文件不会被清空
     *
     * @return 返回路径
     */
    public static String getFilePath(Context context) {
        String appMainPath = getMainPath(context);
        String appFilePath = appMainPath + "/" + FILE_DOCUMENT + "/";
        makeDir(appMainPath);
        return appFilePath;
    }

    /**
     * 获取sdcard路径
     */
    public static String getSDCardPath() {
        String sdcard = Environment.getExternalStorageDirectory().getPath();
        if (!sdcard.endsWith("/"))
            sdcard += "/";
        return sdcard;
    }

    public static String getCacheDir(Context context) {
        return context.getCacheDir().getAbsolutePath();
    }

    /**
     * 获取符合路径规范的路径，当末尾没有 "/",时，自动添加
     */
    public static String getRightPath(String path) {
        String rightPath = "";
        if (!path.endsWith("/")) {
            rightPath = path + "/";
        }
        return rightPath;
    }

    /**
     * 保存字符串到文件结尾
     */
    public static boolean appendStrToFile(String saveString, String path, String fileName) {
        boolean isSuccess = false;
        boolean isPathValid = false;
        //先判断路径是否存在，如果不存在则需要创建
        isPathValid = isExists(path) || makeDir(path);
        //如果路径正确，就写入文件
        if (isPathValid) {
            try {
                String filePathName = path + fileName;
                //打开一个写文件器，构造函数中的第二个参数true表示以追加形式写文件
                FileWriter writer = new FileWriter(filePathName, true);
                writer.write(saveString);
                writer.close();
                isSuccess = true;
            } catch (Exception e) {
                e.printStackTrace();
                Logger.e(e.getMessage());
                isSuccess = false;
            }
        }
        return isSuccess;
    }

    /**
     * 保存string 到指定文件
     *
     * @param saveString 待保存的字符串
     * @param path       保存的路径
     * @param fileName   保存的文件名
     * @return 保存成功或失败
     */
    public static boolean saveStrToFile(String saveString, String path, String fileName) {
        boolean isSuccess = false;
        //判断文件是否存在,不存在则创建
        boolean isPathValid;
        isPathValid = isExists(path) || makeDir(path);

        if (isPathValid) {
            //读写文件操作
            try {
                String writePath = "";
                if (path.endsWith("/")) {
                    writePath = path + fileName;
                } else {
                    writePath = path + "/" + fileName;
                }
                File file = new File(writePath);
                FileOutputStream fos = new FileOutputStream(file);
                byte[] bytes = saveString.getBytes();
                fos.write(bytes);
                fos.close();
                //读写成功
                isSuccess = true;
            } catch (Exception e) {
                e.printStackTrace();
                Logger.e(e.getMessage());
                //读写失败
                isSuccess = false;
            }
        }
        return isSuccess;
    }

    /**
     * 删除文件函数
     *
     * @return 操作
     */
    public static boolean deleteFile(String filePath, String fileName) {
        boolean isSuccess = false;
        String readPath = "";
        if (filePath.endsWith("/")) {
            readPath = filePath + fileName;
        } else {
            readPath = filePath + "/" + fileName;
        }

        String fileFinalPath = readPath;
        if (isExists(fileFinalPath)) {
            try {
                File file = new File(fileFinalPath);
                isSuccess = file.delete();
            } catch (Exception e) {
                e.printStackTrace();
                Logger.e(e.getMessage());
            }
        }
        return isSuccess;
    }

    /**
     * 获取保存文件里面的字符串
     *
     * @return 文件李的字符串
     */
    public static String getStrFromFile(String path, String fileName) {
        String fileString = "";
        //判断文件是否存在,不存在则创建
        if (isExists(path)) {
            //只有当路径是存在的
            try {
                String readPath = "";
                if (path.endsWith("/")) {
                    readPath = path + fileName;
                } else {
                    readPath = path + "/" + fileName;
                }
                //读取文件内容
                File file = new File(readPath);
                FileInputStream fin = new FileInputStream(file);
                //定义一个比较大的空间存储读取结果,一百肯定够了
                byte fileByte[] = new byte[1024 * 600];
                int readSize = fin.read(fileByte);
                byte fileRealByte[] = new byte[readSize];
                System.arraycopy(fileByte, 0, fileRealByte, 0, readSize);
                //                for (int i = 0; i < readSize; i++) {
                //                    fileRealByte[i] = fileByte[i];
                //                }

                //生成string
                fileString = new String(fileRealByte);
            } catch (Exception e) {
                e.printStackTrace();
                Logger.e(e.getMessage());
            }
        }

        return fileString;
    }

    /**
     * 保存saveBytes 到指定文件
     *
     * @param saveBytes 待保存的字符串
     * @param path      保存的路径
     * @param fileName  保存的文件名
     * @return 保存成功或失败
     */
    public static boolean saveBytesToFile(byte[] saveBytes, String path, String fileName) {
        boolean isSuccess = false;
        //判断文件是否存在,不存在则创建
        boolean isPathValid = false;
        isPathValid = isExists(path) || makeDir(path);

        if (isPathValid) {
            //读写文件操作
            try {
                String writePath = "";
                if (path.endsWith("/")) {
                    writePath = path + fileName;
                } else {
                    writePath = path + "/" + fileName;
                }
                File file = new File(writePath);
                FileOutputStream fos = new FileOutputStream(file);
                fos.write(saveBytes);
                fos.close();
                //读写成功
                isSuccess = true;
            } catch (Exception e) {
                e.printStackTrace();
                Logger.e(e.getMessage());
                //读写失败
                isSuccess = false;
            }
        }
        return isSuccess;
    }

    /**
     * 获取保存文件里面的bytes
     *
     * @return 文件里的字符串
     */
    public static byte[] getBytesFromFile(String path, String fileName) {
        byte[] fileBytes = null;
        //判断文件是否存在,不存在则创建
        if (isExists(path)) {
            //只有当路径是存在的
            try {
                String readPath = "";
                if (path.endsWith("/")) {
                    readPath = path + fileName;
                } else {
                    readPath = path + "/" + fileName;
                }
                //读取文件内容
                File file = new File(readPath);
                FileInputStream fin = new FileInputStream(file);
                //定义一个比较大的空间存储读取结果,一百肯定够了
                byte tempByte[] = new byte[2048 * 100];
                int readSize = fin.read(tempByte);
                fileBytes = new byte[readSize];
                //复制数组，不用系统函数。担心出问题
                System.arraycopy(tempByte, 0, fileBytes, 0, readSize);
                //                for (int i = 0; i < readSize; i++) {
                //                    fileBytes[i] = tempByte[i];
                //                }
            } catch (Exception e) {
                e.printStackTrace();
                Logger.e(e.getMessage());
            }
        }
        return fileBytes;
    }


    /**
     * 判断文件夹是否存在
     *
     * @return 存在为 true，不存在为false
     */
    public static boolean isExists(String path) {
        File file = new File(path);
        //判断文件夹是否存在,如果不存在则创建文件夹
        return file.exists();
    }

    /**
     * 创建文件夹路径
     *
     * @return 如果，已经存在目录，则不会创建，返回false
     */
    public static boolean makeDir(String path) {
        boolean isSuccess = false;
        if (isExists(path)) {
            isSuccess = false;
        } else {
            File file = new File(path);
            isSuccess = file.mkdirs();
        }
        return isSuccess;
    }

    /**
     * 获取一个路径里面的path
     */
    public static String getPath(String filePathName) {
        if (TextUtils.isEmpty(filePathName)) {
            return "";
        }
        int lastIndex = filePathName.lastIndexOf("/");
        if (lastIndex <= 0) {
            return "";
        }
        return filePathName.substring(0, lastIndex);
    }

    /**
     * 获取一个路径里面的文件名
     */
    public static String getName(String filePathName) {
        if (TextUtils.isEmpty(filePathName)) {
            return "";
        }
        int lastIndex = filePathName.lastIndexOf("/");
        if (lastIndex <= 0) {
            return "";
        }
        if (lastIndex < filePathName.length() - 1) {
            return filePathName.substring(lastIndex, filePathName.length());
        } else {
            return "";
        }
    }

    /**
     * 文件复制方法，目前文件最多容许为10M
     *
     * @param fromPath     完整的原始路径,包含路径
     * @param fromFileName 原文件文件名
     * @param toPath       目的路径，包含路径
     * @param toFileName   目的文件名
     */
    public static void copyFile(String fromPath, String fromFileName, String toPath, String toFileName) {
        BufferedInputStream bis;
        BufferedOutputStream bos;
        try {
            String fromFile = getRightPath(fromPath) + fromFileName;
            //判断路径是否存在
            if (!isExists(toPath)) {
                makeDir(toPath);
            }
            String toFile = getRightPath(toPath) + toFileName;
            bis = new BufferedInputStream(new FileInputStream(fromFile));
            bos = new BufferedOutputStream(new FileOutputStream(toFile));

            byte[] b = new byte[1024 * 10];
            int len;
            while ((len = bis.read(b)) != -1) {
                bos.write(b, 0, len);
            }
            bos.flush();
            bos.close();
            bis.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
