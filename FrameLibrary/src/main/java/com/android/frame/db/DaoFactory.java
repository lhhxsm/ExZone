package com.android.frame.db;

import android.database.sqlite.SQLiteDatabase;
import android.os.Environment;
import java.io.File;

/**
 * 作者:lhh
 * 描述:
 * 时间:2017/5/23.
 */
public class DaoFactory {
  private static DaoFactory mDaoFactory;
  //持有外部数据库的引用
  private SQLiteDatabase mSQLiteDatabase;

  private DaoFactory() {
    //把数据库放到内容卡里面
    //1.判断是否有存储卡
    //2. 6.0需要动态申请权限
    File dbRoot =
        new File(Environment.getExternalStorageDirectory().getAbsolutePath() + File.separator
            //+ context.getApplicationInfo().packageName
            + "zone" + File.separator + "database");
    if (!dbRoot.exists()) {
      dbRoot.mkdirs();
    }
    File dbFile = new File(dbRoot, "zone.db");
    //打开或者创建数据库
    mSQLiteDatabase = SQLiteDatabase.openOrCreateDatabase(dbFile, null);
  }

  public static DaoFactory getFactory() {
    if (mDaoFactory == null) {
      synchronized (DaoFactory.class) {
        if (mDaoFactory == null) {
          mDaoFactory = new DaoFactory();
        }
      }
    }
    return mDaoFactory;
  }

  public <T> IDao<T> getDao(Class<T> clazz) {
    IDao<T> dao = new Dao<T>();
    dao.init(mSQLiteDatabase, clazz);
    return dao;
  }
}
