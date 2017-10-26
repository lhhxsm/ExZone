package com.android.frame.db;

import android.content.ContentValues;
import android.database.sqlite.SQLiteDatabase;
import android.support.v4.util.ArrayMap;
import android.text.TextUtils;
import android.util.Log;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.List;
import java.util.Map;

/**
 * 作者:lhh
 * 描述:
 * 时间:2017/5/23.
 */
public class Dao<T> implements IDao<T> {

  private static final Object[] mPutMethodArgs = new Object[2];
  private static final Map<String, Method> mPutMethods = new ArrayMap<>();
  private SQLiteDatabase mSQLiteDatabase;
  private Class<T> mClazz;

  @Override public void init(SQLiteDatabase database, Class<T> clazz) {
    mSQLiteDatabase = database;
    this.mClazz = clazz;
    StringBuilder sb = new StringBuilder();
    sb.append("create table if not exists ")
        .append(clazz.getSimpleName())
        .append(" (id integer primary key autoincrement,");
    Field[] fields = mClazz.getDeclaredFields();
    for (Field field : fields) {
      //设置权限,私有和共有的方法都可以使用
      field.setAccessible(true);
      String name = field.getName();
      //如果在排除的字段条件范围内 就跳过
      if (!DaoUtil.checkFiled(name)) {
        continue;
      }
      // type 基本的数据类型 --> int String boolean
      String type = field.getType().getSimpleName();
      //type 需要进行转换,int --> integer ,String --> text 等
      String dbType = DaoUtil.getColumnType(type);
      if (!TextUtils.isEmpty(dbType)) {
        sb.append(" ");
        sb.append(name);
        sb.append(" ");
        sb.append(DaoUtil.getColumnType(dbType));
        sb.append(",");
      }
    }
    sb.replace(sb.length() - 1, sb.length(), ")");
    Log.e("tag", " 创建表 --> " + sb.toString());
    mSQLiteDatabase.execSQL(sb.toString());
  }

  /**
   * 插入数据库
   *
   * @param t 任意对象
   */
  @Override public long insert(T t) {
    ContentValues values = contentValuesByObj(t);
    return mSQLiteDatabase.insert(mClazz.getSimpleName(), null, values);
  }

  @Override public long insert(List<T> data) {
    int result = 0;
    // 批量插入采用 事物  节约时间
    mSQLiteDatabase.beginTransaction();
    for (T t : data) {
      // 调用单条插入
      result += insert(t);
    }
    mSQLiteDatabase.setTransactionSuccessful();
    mSQLiteDatabase.endTransaction();
    return result;
  }

  private ContentValues contentValuesByObj(T t) {
    ContentValues values = new ContentValues();
    Field[] fields = mClazz.getDeclaredFields();
    for (Field field : fields) {
      try {
        // 设置权限，私有和共有都可以访问
        field.setAccessible(true);
        String key = field.getName();
        //如果在排除的字段条件范围内 就跳过
        if (!DaoUtil.checkFiled(key)) {
          continue;
        }
        // 获取value
        Object value = field.get(t);
        // put 第二个参数是类型  把它转换

        mPutMethodArgs[0] = key;
        mPutMethodArgs[1] = value;

        // 方法使用反射 ， 反射在一定程度上会影响性能
        // 源码里面  activity实例 反射  View创建反射
        // 第三方以及是源码给我们提供的是最好的学习教材   插件换肤

        String filedTypeName = field.getType().getName();
        // 还是使用反射  获取方法  put  缓存方法
        Method putMethod = mPutMethods.get(filedTypeName);
        if (putMethod == null) {
          putMethod = ContentValues.class.getDeclaredMethod("put", String.class, value.getClass());
          mPutMethods.put(filedTypeName, putMethod);
        }
        // 通过反射执行
        putMethod.invoke(values, mPutMethodArgs);
      } catch (Exception e) {
        e.printStackTrace();
        Log.e(Dao.class.getSimpleName(), e.getMessage());
      } finally {
        mPutMethodArgs[0] = null;
        mPutMethodArgs[1] = null;
      }
    }
    return values;
  }

  @Override public long update(T t) {
    return 0;
  }
}
