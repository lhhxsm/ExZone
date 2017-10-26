package com.android.lib;

import android.content.Context;
import android.util.Log;
import com.android.lib.utils.FileUtil;
import dalvik.system.BaseDexClassLoader;
import java.io.File;
import java.io.FileNotFoundException;
import java.lang.reflect.Array;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

/**
 * 作者:lhh
 * 描述:
 * 时间:2017/5/13.
 */
public class FixDexManager {
  private static final String TAG = FixDexManager.class.getSimpleName();
  private Context mContext;
  private File mDexDir;

  public FixDexManager(Context context) {
    this.mContext = context;
    //获取应用可以访问的dex目录
    this.mDexDir = context.getDir("odex", Context.MODE_PRIVATE);
  }

  /**
   * 修复dex包
   *
   * @param fixDexPath dex包文件路径
   */
  public void fixDex(String fixDexPath) throws Exception {
    //2. 获取下载好的补丁的 dexElement
    //2.1 移动到系统能够访问的 dex目录下 ClassLoader
    File srcFile = new File(fixDexPath);
    if (!srcFile.exists()) {
      throw new FileNotFoundException(fixDexPath);
    }
    File destFile = new File(mDexDir, srcFile.getName());
    if (destFile.exists()) {
      Log.e(TAG, "patch [" + fixDexPath + "] has be loaded");
      return;
    }

    FileUtil.copyFile(srcFile, destFile);

    //2.2 ClassLoader读取fixDex把路径转 为什么要加入集合，因为一启动就要修复Application
    List<File> fixDexFiles = new ArrayList<>();
    fixDexFiles.add(destFile);

    fixDexFiles(fixDexFiles);
  }

  /**
   * 把dexElement 注入到ClassLoader
   */
  private void injectDexElements(ClassLoader classLoader, Object dexElements) throws Exception {
    //先获取pathList
    Field pathListField = BaseDexClassLoader.class.getDeclaredField("pathList");
    pathListField.setAccessible(true);
    Object pathList = pathListField.get(classLoader);
    //pathList里面的dexElement
    Field dexElementsField = pathList.getClass().getDeclaredField("dexElements");
    dexElementsField.setAccessible(true);
    dexElementsField.set(pathList, dexElements);
  }

  /**
   * 从ClassLoader中获取 dexElement
   */
  private Object getDexElementsByClassLoader(ClassLoader classLoader) throws Exception {
    //先获取pathList
    Field pathListField = BaseDexClassLoader.class.getDeclaredField("pathList");
    pathListField.setAccessible(true);
    Object pathList = pathListField.get(classLoader);
    //pathList里面的dexElement
    Field dexElementsField = pathList.getClass().getDeclaredField("dexElements");
    dexElementsField.setAccessible(true);
    return dexElementsField.get(pathList);
  }

  /**
   * 合并两个数字
   *
   * @param arrayLhs 左边数组
   * @param arrayRhs 右边数组
   */
  private Object combineArray(Object arrayLhs, Object arrayRhs) {
    Class<?> localClass = arrayLhs.getClass().getComponentType();
    int i = Array.getLength(arrayLhs);
    int j = i + Array.getLength(arrayRhs);
    Object result = Array.newInstance(localClass, j);
    for (int k = 0; k < j; k++) {
      if (k < i) {
        Array.set(result, k, Array.get(arrayLhs, k));
      } else {
        Array.set(result, k, Array.get(arrayRhs, k - i));
      }
    }
    return result;
  }

  /**
   * 加载全部的修复包
   */
  public void loadFixDex() throws Exception {
    File[] dexFiles = mDexDir.listFiles();

    List<File> fixDexFiles = new ArrayList<>();

    for (File dexFile : dexFiles) {
      if (dexFile.getName().endsWith(".dex")) {
        fixDexFiles.add(dexFile);
      }
    }

    fixDexFiles(fixDexFiles);
  }

  /**
   * 修复dex
   */
  private void fixDexFiles(List<File> fixDexFiles) throws Exception {
    //1. 先获取已经运行的 dexElement
    ClassLoader applicationClassLoader = mContext.getClassLoader();
    Object applicationDexElements = getDexElementsByClassLoader(applicationClassLoader);

    File optimizedDirectory = new File(mDexDir, "odex");

    if (!optimizedDirectory.exists()) {
      optimizedDirectory.mkdirs();
    }
    //修复
    for (File fixDexFile : fixDexFiles) {
      //dexPath dex路径
      //optimizedDirectory 解压路径
      //libraryPath .so文件位置
      //parent 父ClassLoader
      ClassLoader fixDexClassLoader = new BaseDexClassLoader(//
          fixDexFile.getAbsolutePath(), //dex路径 必须要做应用目录下的odex文件中
          optimizedDirectory, //解压路径
          null,//.so文件位置
          applicationClassLoader//父ClassLoader
      );

      Object fixDexElements = getDexElementsByClassLoader(fixDexClassLoader);
      //3. 把补丁的dexElement 插到已经运行的dexElement的最前面
      // applicationClassLoader数组 合并 fixDexElements数组
      // 3.1合并完成
      applicationDexElements = combineArray(fixDexElements, applicationDexElements);
    }
    // 3.2 把合并的数组注入到原来的applicationClassLoader中
    injectDexElements(applicationClassLoader, applicationDexElements);
  }
}
