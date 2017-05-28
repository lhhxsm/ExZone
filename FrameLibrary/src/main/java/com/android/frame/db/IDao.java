package com.android.frame.db;

import android.database.sqlite.SQLiteDatabase;
import java.util.List;

/**
 * 作者:李鸿浩
 * 描述:
 * 时间:2017/5/23.
 */
public interface IDao<T> {

  /**
   * 初始化
   */
  void init(SQLiteDatabase database, Class<T> clazz);

  /**
   * 插入
   */
  long insert(T t);

  long insert(List<T> data);

  /**
   * 更新
   */
  long update(T t);
}
