package com.exzone.lib.util;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.os.Environment;
import android.os.StatFs;
import android.text.TextUtils;
import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.zip.GZIPOutputStream;

/**
 * 作者:lhh
 * 描述:文件工具类
 * 时间：2016/10/7.
 */
public class FileUtils {
  public final static String FILE_EXTENSION_SEPARATOR = ".";

  private FileUtils() {
    throw new AssertionError();
  }

  /**
   * 检查是否已挂载SD卡镜像（是否存在SD卡）
   */
  public static boolean isMountedSDCard() {
    if (Environment.MEDIA_MOUNTED.equals(Environment.getExternalStorageState())) {
      return true;
    } else {
      Logger.e("SD卡不存在!");
      return false;
    }
  }

  /**
   * 获取可以使用的缓存目录
   *
   * @param uniqueName 目录名称
   */
  public static File getDiskCacheDir(Context context, String uniqueName) {
    String cachePath = isMountedSDCard() ? getExternalCacheDir(context).getPath()
        : context.getCacheDir().getPath();
    return new File(cachePath + File.separator + uniqueName);
  }

  /**
   * 获取程序外部的缓存目录
   */
  public static File getExternalCacheDir(Context context) {
    String cacheDir = "/Android/data/" + context.getPackageName() + "/cache/";
    return new File(Environment.getExternalStorageDirectory().getPath() + cacheDir);
  }

  public static String formatFileSize(long fileS) {
    java.text.DecimalFormat df = new java.text.DecimalFormat("#.00");
    String fileSizeString = "";
    if (fileS < 1024) {
      fileSizeString = df.format((double) fileS) + "B";
    } else if (fileS < 1048576) {
      fileSizeString = df.format((double) fileS / 1024) + "KB";
    } else if (fileS < 1073741824) {
      fileSizeString = df.format((double) fileS / 1048576) + "MB";
    } else {
      fileSizeString = df.format((double) fileS / 1073741824) + "G";
    }

    if (fileSizeString.startsWith(".")) {
      return "0B";
    }
    return fileSizeString;
  }

  /**
   * 获取文件路径空间大小
   */
  public static long getUsableSpace(File path) {
    try {
      final StatFs stats = new StatFs(path.getPath());
      return (long) stats.getBlockSize() * (long) stats.getAvailableBlocks();
    } catch (Exception e) {
      Logger.e("获取sdcard缓存大小出错,请查看AndroidManifest.xml 是否添加了sdcard的访问权限");
      e.printStackTrace();
      return -1;
    }
  }

  /**
   * 获取SD卡剩余容量（单位Byte）
   */
  public static long getSDFreeSize() {
    if (isMountedSDCard()) {
      // 取得SD卡文件路径
      File path = Environment.getExternalStorageDirectory();
      StatFs sf = new StatFs(path.getPath());
      // 获取单个数据块的大小(Byte)
      long blockSize = sf.getBlockSize();
      // 空闲的数据块的数量
      long freeBlocks = sf.getAvailableBlocks();
      // 返回SD卡空闲大小
      return freeBlocks * blockSize; // 单位Byte
    } else {
      return 0;
    }
  }

  /**
   * 获取SD卡总容量（单位Byte）
   */
  public static long getSDAllSize() {
    if (isMountedSDCard()) {
      // 取得SD卡文件路径
      File path = Environment.getExternalStorageDirectory();
      StatFs sf = new StatFs(path.getPath());
      // 获取单个数据块的大小(Byte)
      long blockSize = sf.getBlockSize();
      // 获取所有数据块数
      long allBlocks = sf.getBlockCount();
      // 返回SD卡大小（Byte）
      return allBlocks * blockSize;
    } else {
      return 0;
    }
  }

  /**
   * 获取文件大小
   * <ul>
   * <li>if path is null or empty, return -1</li>
   * <li>if path exist and it is a file, return file size, else return -1</li>
   * <ul>
   */
  public static long getFileSize(String path) {
    if (TextUtils.isEmpty(path)) {
      return -1;
    }

    File file = new File(path);
    return (file.exists() && file.isFile() ? file.length() : -1);
  }

  /**
   * 获取可用的SD卡路径（若SD卡不没有挂载则返回""）
   */
  public static String getSDCardPath() {
    if (isMountedSDCard()) {
      File sdcardDir = Environment.getExternalStorageDirectory();
      if (!sdcardDir.canWrite()) {
        Logger.e("SDCARD can not write !");
      }
      return sdcardDir.getPath();
    }
    return "";
  }

  /**
   * 以行为单位读取文件内容,一次读一整行,常用于读面向行的格式化文件
   *
   * @param filePath 文件路径
   */
  public static String readFileByLines(String filePath) {
    BufferedReader reader = null;
    StringBuilder sb = new StringBuilder();
    try {
      reader = new BufferedReader(new InputStreamReader(new FileInputStream(filePath),
          System.getProperty("file.encoding")));
      String tempString = null;
      while ((tempString = reader.readLine()) != null) {
        sb.append(tempString);
        sb.append("\n");
      }
      //            reader.close();
    } catch (IOException e) {
      e.printStackTrace();
    } finally {
      //            if (reader != null) {
      //                try {
      //                    reader.close();
      //                } catch (IOException e) {
      //                    e.printStackTrace();
      //                }
      //            }
      IOUtils.close(reader);
    }
    return sb.toString();
  }

  /**
   * 以行为单位读取文件内容,一次读一整行,常用于读面向行的格式化文件
   *
   * @param filePath 文件路径
   * @param encoding 写文件编码
   */
  public static String readFileByLines(String filePath, String encoding) {
    BufferedReader reader = null;
    StringBuilder sb = new StringBuilder();
    try {
      reader = new BufferedReader(new InputStreamReader(new FileInputStream(filePath), encoding));
      String tempString;
      while ((tempString = reader.readLine()) != null) {
        sb.append(tempString);
        sb.append("\n");
      }
      //            reader.close();
    } catch (IOException e) {
      e.printStackTrace();
    } finally {
      //            if (reader != null) {
      //                try {
      //                    reader.close();
      //                } catch (IOException e) {
      //                    e.printStackTrace();
      //                }
      //            }
      IOUtils.close(reader);
    }

    return sb.toString();
  }

  /**
   * 保存内容
   *
   * @param filePath 文件路径
   * @param content 保存的内容
   */
  public static void saveToFile(String filePath, String content) {
    saveToFile(filePath, content, System.getProperty("file.encoding"));
  }

  /**
   * 指定编码保存内容
   *
   * @param filePath 文件路径
   * @param content 保存的内容
   * @param encoding 写文件编码
   */
  public static void saveToFile(String filePath, String content, String encoding) {
    BufferedWriter writer = null;
    File file = new File(filePath);
    try {
      if (!file.getParentFile().exists()) {
        boolean mkdirs = file.getParentFile().mkdirs();
        if (!mkdirs) {
          return;
        }
      }
      writer =
          new BufferedWriter(new OutputStreamWriter(new FileOutputStream(file, false), encoding));
      writer.write(content);
    } catch (IOException e) {
      e.printStackTrace();
    } finally {
      //            if (writer != null) {
      //                try {
      //                    writer.close();
      //                } catch (IOException e) {
      //                    e.printStackTrace();
      //                }
      //            }
      IOUtils.close(writer);
    }
  }

  /**
   * 追加文本
   *
   * @param content 需要追加的内容
   * @param file 待追加文件源
   */
  public static void appendToFile(String content, File file) {
    appendToFile(content, file, System.getProperty("file.encoding"));
  }

  /**
   * 追加文本
   *
   * @param content 需要追加的内容
   * @param file 待追加文件源
   * @param encoding 文件编码
   * @throws IOException
   */
  public static void appendToFile(String content, File file, String encoding) {
    BufferedWriter writer = null;
    try {
      if (!file.getParentFile().exists()) {
        boolean mkdirs = file.getParentFile().mkdirs();
        if (!mkdirs) {
          return;
        }
      }
      writer =
          new BufferedWriter(new OutputStreamWriter(new FileOutputStream(file, true), encoding));
      writer.write(content);
    } catch (IOException e) {
      e.printStackTrace();
    } finally {
      //            if (writer != null) {
      //                try {
      //                    writer.close();
      //                } catch (IOException e) {
      //                    e.printStackTrace();
      //                }
      //            }
      IOUtils.close(writer);
    }
  }

  /**
   * 判断文件是否存在
   *
   * @param filePath 文件路径
   * @return 是否存在
   */
  public static Boolean isExist(String filePath) {
    Boolean flag = false;
    try {
      File file = new File(filePath);
      if (file.exists()) {
        flag = true;
      }
    } catch (Exception e) {
      Logger.e("判断文件失败-->" + e.getMessage());
    }
    return flag;
  }

  /**
   * 快速读取程序应用包下的文件内容
   *
   * @param context 上下文
   * @param filename 文件名称
   * @return 文件内容
   */
  public static String read(Context context, String filename) {
    String content = null;
    FileInputStream inStream = null;
    try {
      inStream = context.openFileInput(filename);
      ByteArrayOutputStream outStream = new ByteArrayOutputStream();
      byte[] buffer = new byte[1024];
      int len;
      while ((len = inStream.read(buffer)) != -1) {
        outStream.write(buffer, 0, len);
      }
      byte[] data = outStream.toByteArray();
      content = new String(data, Charset.forName("UTF-8"));
    } catch (Exception e) {
      e.printStackTrace();
    }

    return content;
  }

  /**
   * 读取指定目录文件的文件内容
   *
   * @param fileName 文件名称
   * @return 文件内容
   */
  public static String read(String fileName) {
    String content = null;
    FileInputStream inStream = null;
    try {
      inStream = new FileInputStream(fileName);
      ByteArrayOutputStream outStream = new ByteArrayOutputStream();
      byte[] buffer = new byte[1024];
      int len = 0;
      while ((len = inStream.read(buffer)) != -1) {
        outStream.write(buffer, 0, len);
      }
      byte[] data = outStream.toByteArray();
      content = new String(data, Charset.forName("UTF-8"));
    } catch (Exception e) {
      e.printStackTrace();
    } finally {
      //            if (inStream != null) {
      //                try {
      //                    inStream.close();
      //                } catch (IOException e) {
      //                    e.printStackTrace();
      //                }
      //            }
      IOUtils.close(inStream);
    }
    return content;
  }

  /***
   * 以行为单位读取文件内容,一次读一整行,常用于读面向行的格式化文件
   *
   * @param fileName 文件名称
   * @param encoding 文件编码
   * @return 字符串内容
   * @throws IOException
   */
  public static String read(String fileName, String encoding) throws IOException {
    BufferedReader reader = null;
    StringBuilder sb = new StringBuilder();
    try {
      reader = new BufferedReader(new InputStreamReader(new FileInputStream(fileName), encoding));
      String tempString = null;
      while ((tempString = reader.readLine()) != null) {
        sb.append(tempString);
      }
    } catch (IOException e) {
      e.printStackTrace();
    } finally {
      //            if (reader != null) {
      //                reader.close();
      //            }
      IOUtils.close(reader);
    }

    return sb.toString();
  }

  /**
   * 读取raw目录的文件内容
   *
   * @param context 内容上下文
   * @param rawFileId raw文件名id
   */
  public static String readRawValue(Context context, int rawFileId) {
    String result = "";
    InputStream is = null;
    try {
      is = context.getResources().openRawResource(rawFileId);
      int len = is.available();
      byte[] buffer = new byte[len];
      int read = is.read(buffer);
      if (read <= 0) {
        return null;
      }
      result = new String(buffer, "UTF-8");
      //            is.close();
    } catch (Exception e) {
      e.printStackTrace();
    } finally {
      IOUtils.close(is);
    }
    return result;
  }

  /**
   * 读取assets目录的文件内容
   *
   * @param context 内容上下文
   * @param fileName 文件名称，包含扩展名
   */
  public static String readAssetsValue(Context context, String fileName) {
    String result = "";
    InputStream is = null;
    try {
      is = context.getResources().getAssets().open(fileName);
      int len = is.available();
      byte[] buffer = new byte[len];
      is.read(buffer);
      result = new String(buffer, "UTF-8");
      //            is.close();
    } catch (Exception e) {
      e.printStackTrace();
    } finally {
      IOUtils.close(is);
    }
    return result;
  }

  /**
   * 读取assets目录的文件内容
   *
   * @param context 内容上下文
   * @param fileName 文件名称,包含扩展名
   */
  public static List<String> readAssetsListValue(Context context, String fileName) {
    List<String> list = new ArrayList<String>();
    InputStream in = null;
    BufferedReader br = null;
    try {
      in = context.getResources().getAssets().open(fileName);
      br = new BufferedReader(new InputStreamReader(in, Charset.forName("UTF-8")));
      String str = null;
      while ((str = br.readLine()) != null) {
        list.add(str);
      }
    } catch (IOException e) {
      e.printStackTrace();
    } finally {
      //            try {
      //                if (br != null) {
      //                    br.close();
      //                }
      //                if (in != null) {
      //                    in.close();
      //                }
      //            } catch (IOException e) {
      //                e.printStackTrace();
      //            }
      IOUtils.close(br);
      IOUtils.close(in);
    }
    return list;
  }

  /**
   * 获取SharedPreferences文件内容
   *
   * @param context 上下文
   * @param fileNameNoExt 文件名称（不用带后缀名）
   */
  public static Map<String, ?> readSharedPreferences(Context context, String fileNameNoExt) {
    SharedPreferences preferences =
        context.getSharedPreferences(fileNameNoExt, Context.MODE_PRIVATE);
    return preferences.getAll();
  }

  /**
   * 写入SharedPreferences文件内容
   *
   * @param context 上下文
   * @param fileNameNoExt 文件名称（不用带后缀名）
   * @param values 需要写入的数据Map(String,Boolean,Float,Long,Integer)
   */
  public static void writeSharedPreferences(Context context, String fileNameNoExt,
      Map<String, ?> values) {
    try {
      SharedPreferences preferences =
          context.getSharedPreferences(fileNameNoExt, Context.MODE_PRIVATE);
      SharedPreferences.Editor editor = preferences.edit();
      for (Iterator iterator = values.entrySet().iterator(); iterator.hasNext(); ) {
        Map.Entry<String, ?> entry = (Map.Entry<String, ?>) iterator.next();
        if (entry.getValue() instanceof String) {
          editor.putString(entry.getKey(), (String) entry.getValue());
        } else if (entry.getValue() instanceof Boolean) {
          editor.putBoolean(entry.getKey(), (Boolean) entry.getValue());
        } else if (entry.getValue() instanceof Float) {
          editor.putFloat(entry.getKey(), (Float) entry.getValue());
        } else if (entry.getValue() instanceof Long) {
          editor.putLong(entry.getKey(), (Long) entry.getValue());
        } else if (entry.getValue() instanceof Integer) {
          editor.putInt(entry.getKey(), (Integer) entry.getValue());
        }
      }
      editor.apply();
    } catch (Exception e) {
      e.printStackTrace();
    }
  }

  /**
   * 写入应用程序包files目录下文件
   *
   * @param context 上下文
   * @param fileName 文件名称
   * @param content 文件内容
   */
  public static void write(Context context, String fileName, String content) {
    FileOutputStream outStream = null;
    try {
      outStream = context.openFileOutput(fileName, Context.MODE_PRIVATE);
      outStream.write(content.getBytes(Charset.forName("UTF-8")));
      //            outStream.close();
    } catch (Exception e) {
      e.printStackTrace();
    } finally {
      IOUtils.close(outStream);
    }
  }

  /**
   * 写入应用程序包files目录下文件
   *
   * @param context 上下文
   * @param fileName 文件名称
   * @param content 文件内容
   */
  public static void write(Context context, String fileName, byte[] content) {
    FileOutputStream outStream = null;
    try {
      outStream = context.openFileOutput(fileName, Context.MODE_PRIVATE);
      outStream.write(content);
      //            outStream.close();
    } catch (Exception e) {
      e.printStackTrace();
    } finally {
      IOUtils.close(outStream);
    }
  }

  /**
   * 写入应用程序包files目录下文件
   *
   * @param context 上下文
   * @param fileName 文件名称
   * @param modeType 文件写入模式（Context.MODE_PRIVATE、Context.MODE_APPEND、Context.
   * MODE_WORLD_READABLE、Context.MODE_WORLD_WRITEABLE）
   * @param content 文件内容
   */
  public static void write(Context context, String fileName, byte[] content, int modeType) {
    FileOutputStream outStream = null;
    try {
      outStream = context.openFileOutput(fileName, modeType);
      outStream.write(content);
      //            outStream.close();
    } catch (Exception e) {
      e.printStackTrace();
    } finally {
      IOUtils.close(outStream);
    }
  }

  /**
   * 指定编码将内容写入目标文件
   *
   * @param target 目标文件
   * @param content 文件内容
   * @param encoding 写入文件编码
   */
  public static void write(File target, String content, String encoding) {
    BufferedWriter writer = null;
    try {
      if (!target.getParentFile().exists()) {
        boolean mkdirs = target.getParentFile().mkdirs();
        if (!mkdirs) {
          return;
        }
      }
      writer =
          new BufferedWriter(new OutputStreamWriter(new FileOutputStream(target, false), encoding));
      writer.write(content);
    } catch (IOException e) {
      e.printStackTrace();
    } finally {
      //            if (writer != null) {
      //                try {
      //                    writer.close();
      //                } catch (IOException e) {
      //                    e.printStackTrace();
      //                }
      //            }
      IOUtils.close(writer);
    }
  }

  /**
   * 指定目录写入文件内容
   *
   * @param filePath 文件路径+文件名
   * @param content 文件内容
   */
  public static void write(String filePath, byte[] content) {
    FileOutputStream fos = null;
    try {
      File file = new File(filePath);
      if (!file.getParentFile().exists()) {
        boolean mkdirs = file.getParentFile().mkdirs();
        if (!mkdirs) {
          return;
        }
      }
      fos = new FileOutputStream(file);
      fos.write(content);
      fos.flush();
    } catch (IOException e) {
      e.printStackTrace();
    } finally {
      //            if (fos != null) {
      //                try {
      //                    fos.close();
      //                } catch (IOException e) {
      //                    e.printStackTrace();
      //                }
      //            }
      IOUtils.close(fos);
    }
  }

  /**
   * 写入文件
   *
   * @param inputStream 下载文件的字节流对象
   * @param filePath 文件的存放路径(带文件名称)
   */
  public static File write(InputStream inputStream, String filePath) {
    // 在指定目录创建一个空文件并获取文件对象
    File mFile = new File(filePath);
    if (!mFile.getParentFile().exists()) {
      boolean mkdirs = mFile.getParentFile().mkdirs();
      if (!mkdirs) {
        return null;
      }
    }
    OutputStream outputStream = null;
    try {
      outputStream = new FileOutputStream(mFile);
      byte buffer[] = new byte[4 * 1024];
      int lenght;
      while ((lenght = inputStream.read(buffer)) > 0) {
        outputStream.write(buffer, 0, lenght);
      }
      outputStream.flush();
    } catch (IOException e) {
      e.printStackTrace();
      Logger.e("写入文件失败,原因:" + e.getMessage());
    } finally {
      //            try {
      //                if (outputStream != null) {
      //                    outputStream.close();
      //                }
      //                if (inputStream != null) {
      //                    inputStream.close();
      //                }
      //            } catch (IOException e) {
      //                e.printStackTrace();
      //            }
      IOUtils.close(outputStream);
      IOUtils.close(inputStream);
    }
    return mFile;
  }

  /**
   * 指定目录写入文件内容
   *
   * @param filePath 文件路径+文件名
   * @param bitmap 文件内容
   */
  public static void saveAsJPEG(Bitmap bitmap, String filePath) {
    FileOutputStream fos = null;
    try {
      File file = new File(filePath);
      if (!file.getParentFile().exists()) {
        boolean mkdirs = file.getParentFile().mkdirs();
        if (!mkdirs) {
          return;
        }
      }
      fos = new FileOutputStream(file);
      bitmap.compress(Bitmap.CompressFormat.JPEG, 100, fos);
      fos.flush();
    } catch (IOException e) {
      e.printStackTrace();
    } finally {
      //            if (fos != null) {
      //                try {
      //                    fos.close();
      //                } catch (IOException e) {
      //                    e.printStackTrace();
      //                }
      //            }
      IOUtils.close(fos);
    }
  }

  /**
   * 指定目录写入文件内容
   *
   * @param filePath 文件路径+文件名
   * @param bitmap 文件内容
   */
  public static void saveAsPNG(Bitmap bitmap, String filePath) {
    FileOutputStream fos = null;
    try {
      File file = new File(filePath);
      if (!file.getParentFile().exists()) {
        boolean mkdirs = file.getParentFile().mkdirs();
        if (!mkdirs) {
          return;
        }
      }
      fos = new FileOutputStream(file);
      bitmap.compress(Bitmap.CompressFormat.PNG, 100, fos);
      fos.flush();
    } catch (IOException e) {
      e.printStackTrace();
    } finally {
      //            if (fos != null) {
      //                try {
      //                    fos.close();
      //                } catch (IOException e) {
      //                    e.printStackTrace();
      //                }
      //            }
      IOUtils.close(fos);
    }
  }

  /**
   * 文件拷贝
   */
  public static void copyFile(File sourceFile, File targetFile) {
    BufferedInputStream inBuff = null;
    BufferedOutputStream outBuff = null;
    try {
      // 新建文件输入流并对它进行缓冲
      inBuff = new BufferedInputStream(new FileInputStream(sourceFile));
      // 新建文件输出流并对它进行缓冲
      outBuff = new BufferedOutputStream(new FileOutputStream(targetFile));
      // 缓冲数组
      byte[] b = new byte[1024 * 5];
      int len;
      while ((len = inBuff.read(b)) != -1) {
        outBuff.write(b, 0, len);
      }
      // 刷新此缓冲的输出流
      outBuff.flush();
      boolean delete = sourceFile.delete();
      if (!delete) {
        return;
      }
      sourceFile.deleteOnExit();
    } catch (Exception e) {
      e.printStackTrace();
    } finally {
      // 关闭流
      //            try {
      //                if (inBuff != null)
      //                    inBuff.close();
      //                if (outBuff != null)
      //                    outBuff.close();
      //            } catch (IOException e) {
      //                e.printStackTrace();
      //            }
      IOUtils.close(inBuff);
      IOUtils.close(outBuff);
    }
  }

  /**
   * delete file or directory
   * <ul>
   * <li>if path is null or empty, return true</li>
   * <li>if path not exist, return true</li>
   * <li>if path exist, delete recursion. return true</li>
   * <ul>
   */
  public static boolean deleteFile(String path) {
    if (TextUtils.isEmpty(path)) {
      return true;
    }
    File file = new File(path);
    if (!file.exists()) {
      return true;
    }
    if (file.isFile()) {
      return file.delete();
    }
    if (!file.isDirectory()) {
      return false;
    }
    File[] files = file.listFiles();
    if (files == null || files.length <= 0) {
      return false;
    }
    for (File f : files) {
      if (f.isFile()) {
        boolean delete = f.delete();
        if (!delete) {
          return false;
        }
      } else if (f.isDirectory()) {
        deleteFile(f.getAbsolutePath());
      }
    }
    return file.delete();
  }

  /**
   * Indicates if this file represents a file on the underlying file system.
   */
  public static boolean isFileExist(String filePath) {
    if (TextUtils.isEmpty(filePath)) {
      return false;
    }
    File file = new File(filePath);
    return (file.exists() && file.isFile());
  }

  /**
   * Indicates if this file represents a directory on the underlying file system.
   */
  public static boolean isFolderExist(String directoryPath) {
    if (TextUtils.isEmpty(directoryPath)) {
      return false;
    }
    File dire = new File(directoryPath);
    return (dire.exists() && dire.isDirectory());
  }

  /**
   * get file name from path, not include suffix
   * <p>
   * <pre>
   *      getFileNameWithoutExtension(null)               =   null
   *      getFileNameWithoutExtension("")                 =   ""
   *      getFileNameWithoutExtension("   ")              =   "   "
   *      getFileNameWithoutExtension("abc")              =   "abc"
   *      getFileNameWithoutExtension("a.mp3")            =   "a"
   *      getFileNameWithoutExtension("a.b.rmvb")         =   "a.b"
   *      getFileNameWithoutExtension("c:\\")              =   ""
   *      getFileNameWithoutExtension("c:\\a")             =   "a"
   *      getFileNameWithoutExtension("c:\\a.b")           =   "a"
   *      getFileNameWithoutExtension("c:a.txt\\a")        =   "a"
   *      getFileNameWithoutExtension("/home/admin")      =   "admin"
   *      getFileNameWithoutExtension("/home/admin/a.txt/b.mp3")  =   "b"
   * </pre>
   */
  public static String getFileNameWithoutExtension(String filePath) {
    if (TextUtils.isEmpty(filePath)) {
      return filePath;
    }
    int extensionPosition = filePath.lastIndexOf(FILE_EXTENSION_SEPARATOR);
    int filePosition = filePath.lastIndexOf(File.separator);
    if (filePosition == -1) {
      return (extensionPosition == -1 ? filePath : filePath.substring(0, extensionPosition));
    }
    if (extensionPosition == -1) {
      return filePath.substring(filePosition + 1);
    }
    return (filePosition < extensionPosition ? filePath.substring(filePosition + 1,
        extensionPosition) : filePath.substring(filePosition + 1));
  }

  /**
   * get file name from path, include suffix
   * <p>
   * <pre>
   *      getFileName(null)               =   null
   *      getFileName("")                 =   ""
   *      getFileName("   ")              =   "   "
   *      getFileName("a.mp3")            =   "a.mp3"
   *      getFileName("a.b.rmvb")         =   "a.b.rmvb"
   *      getFileName("abc")              =   "abc"
   *      getFileName("c:\\")              =   ""
   *      getFileName("c:\\a")             =   "a"
   *      getFileName("c:\\a.b")           =   "a.b"
   *      getFileName("c:a.txt\\a")        =   "a"
   *      getFileName("/home/admin")      =   "admin"
   *      getFileName("/home/admin/a.txt/b.mp3")  =   "b.mp3"
   * </pre>
   *
   * @return file name from path, include suffix
   */
  public static String getFileName(String filePath) {
    if (TextUtils.isEmpty(filePath)) {
      return filePath;
    }
    int filePosition = filePath.lastIndexOf(File.separator);
    return (filePosition == -1) ? filePath : filePath.substring(filePosition + 1);
  }

  /**
   * get folder name from path
   * <p>
   * <pre>
   *      getFolderName(null)               =   null
   *      getFolderName("")                 =   ""
   *      getFolderName("   ")              =   ""
   *      getFolderName("a.mp3")            =   ""
   *      getFolderName("a.b.rmvb")         =   ""
   *      getFolderName("abc")              =   ""
   *      getFolderName("c:\\")              =   "c:"
   *      getFolderName("c:\\a")             =   "c:"
   *      getFolderName("c:\\a.b")           =   "c:"
   *      getFolderName("c:a.txt\\a")        =   "c:a.txt"
   *      getFolderName("c:a\\b\\c\\d.txt")    =   "c:a\\b\\c"
   *      getFolderName("/home/admin")      =   "/home"
   *      getFolderName("/home/admin/a.txt/b.mp3")  =   "/home/admin/a.txt"
   * </pre>
   */
  public static String getFolderName(String filePath) {
    if (TextUtils.isEmpty(filePath)) {
      return filePath;
    }
    int filePosition = filePath.lastIndexOf(File.separator);
    return (filePosition == -1) ? "" : filePath.substring(0, filePosition);
  }

  /**
   * 从文件路径中获取文件后缀名
   * <p>
   * <pre>
   *      getFileExtension(null)               =   ""
   *      getFileExtension("")                 =   ""
   *      getFileExtension("   ")              =   "   "
   *      getFileExtension("a.mp3")            =   "mp3"
   *      getFileExtension("a.b.rmvb")         =   "rmvb"
   *      getFileExtension("abc")              =   ""
   *      getFileExtension("c:\\")              =   ""
   *      getFileExtension("c:\\a")             =   ""
   *      getFileExtension("c:\\a.b")           =   "b"
   *      getFileExtension("c:a.txt\\a")        =   ""
   *      getFileExtension("/home/admin")      =   ""
   *      getFileExtension("/home/admin/a.txt/b")  =   ""
   *      getFileExtension("/home/admin/a.txt/b.mp3")  =   "mp3"
   * </pre>
   */
  public static String getFileExtension(String filePath) {
    if (TextUtils.isEmpty(filePath)) {
      return filePath;
    }
    int extensionPosition = filePath.lastIndexOf(FILE_EXTENSION_SEPARATOR);
    int filePosition = filePath.lastIndexOf(File.separator);
    if (extensionPosition == -1) {
      return "";
    }
    return (filePosition >= extensionPosition) ? "" : filePath.substring(extensionPosition + 1);
  }

  /**
   * 从文件中获取文件后缀名
   */
  public static String getFileExtension(File file) {
    String name = file.getName();
    try {
      return name.substring(name.lastIndexOf(FILE_EXTENSION_SEPARATOR) + 1);
    } catch (Exception e) {
      return "";
    }
  }

  /**
   * Creates the directory named by the trailing filename of this file, including the complete
   * directory path required to create this directory.
   */
  public static boolean makeDirs(String filePath) {
    String folderName = getFolderName(filePath);
    if (TextUtils.isEmpty(folderName)) {
      return false;
    }
    File folder = new File(folderName);
    return (folder.exists() && folder.isDirectory()) || folder.mkdirs();
  }

  /**
   * @see #makeDirs(String)
   */
  public static boolean makeFolders(String filePath) {
    return makeDirs(filePath);
  }

  // gzip压缩
  public static byte[] gZip(File srcFile) throws IOException {
    byte[] data = null;
    try {
      ByteArrayOutputStream out = new ByteArrayOutputStream();

      FileInputStream fis = new FileInputStream(srcFile);
      byte[] buf = new byte[1024];
      int len = 0;
      while ((len = fis.read(buf)) > 0) {
        out.write(buf, 0, len);
      }
      byte[] bContent = out.toByteArray();
      out.reset();// 清空byte数组

      GZIPOutputStream gOut = new GZIPOutputStream(out, bContent.length); // 压缩级别,缺省为1级
      DataOutputStream objOut = new DataOutputStream(gOut);
      objOut.write(bContent);
      objOut.flush();
      gOut.close();
      data = out.toByteArray();
      out.close();
    } catch (IOException e) {
      e.printStackTrace();
      throw e;
    }
    return data;
  }
}
