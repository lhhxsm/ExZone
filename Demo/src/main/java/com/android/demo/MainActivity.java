package com.android.demo;

import android.content.res.Configuration;
import android.os.Environment;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.util.Log;
import android.widget.Toast;
import com.android.frame.BaseSkinActivity;
import com.android.lib.FixDexManager;
import java.io.BufferedInputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;

public class MainActivity extends BaseSkinActivity {

  @Override protected void setContentView() {
    setContentView(R.layout.activity_main);
  }

  @Override protected void initTitle() {

  }

  @Override protected void initView() {

  }

  @Override protected void initData() {
    Configuration config = getResources().getConfiguration();
    int smallestScreenWidthDp = config.smallestScreenWidthDp;//屏幕最小宽度
    DisplayMetrics metric = new DisplayMetrics();
    getWindowManager().getDefaultDisplay().getMetrics(metric);
    int width = metric.widthPixels;     // 屏幕宽度（像素）
    int height = metric.heightPixels;   // 屏幕高度（像素）
    float density = metric.density;      // 屏幕密度（0.75 / 1.0 / 1.5）
    int densityDpi = metric.densityDpi;  // 屏幕密度DPI（120 / 160 / 240）
    Log.e("tag", "屏幕最小宽度:"
        + smallestScreenWidthDp
        + "\n"
        + "屏幕宽度(像素):"
        + width
        + "\n"
        + "屏幕高度(像素):"
        + height
        + "\n"
        + "屏幕密度:"
        + density
        + "\n"
        + "屏幕宽度DPI:"
        + densityDpi
        + "\n");

    //IDao<Person> dao = DaoFactory.getFactory().getDao(Person.class);
    //List<Person> list = new ArrayList<>();
    //list.add(new Person("张三", 21));
    //list.add(new Person("李四", 22));
    //list.add(new Person("王五", 20));
    //for (int i = 0; i < 10000; i++) {
    //  list.add(new Person("key = " + i, 20 + i));
    //}
    //long millis = System.currentTimeMillis();
    //long insert = dao.insert(list);
    //Log.e("Tag", "size = " + insert);
    //Log.e("tag", "time = " + (System.currentTimeMillis() - millis));

    ////获取上次的崩溃信息
    //File crashFile = ExceptionCrashHandler.getInstance().getCrashFile();
    //if (crashFile.exists()) {
    //  //上传到服务器
    //  try {
    //    FileInputStream inputStream = new FileInputStream(crashFile);
    //    String s = readStr(inputStream, "UTF-8");
    //    Log.e("tag", "" + s);
    //  } catch (Exception e) {
    //    e.printStackTrace();
    //  }
    //}
    ////int num = 10 / 0;

    //File fixFile = new File(Environment.getExternalStorageDirectory(), "fix.apatch");
    //if (fixFile.exists()) {
    //  //修复bug
    //  try {
    //    GlobalApplication.sPatchManager.addPatch(fixFile.getAbsolutePath());
    //    Log.e("tag", "修复成功");
    //  } catch (IOException e) {
    //    e.printStackTrace();
    //    Log.e("tag", "修复失败");
    //  }
    //}
    //fixDexBug();
  }

  private void fixDexBug() {
    File fixFile = new File(Environment.getExternalStorageDirectory(), "fix.dex");
    if (fixFile.exists()) {
      FixDexManager fixDexManager = new FixDexManager(this);
      try {
        fixDexManager.fixDex(fixFile.getAbsolutePath());
        Toast.makeText(this, "修复成功", Toast.LENGTH_SHORT).show();
      } catch (Exception e) {
        e.printStackTrace();
        Toast.makeText(this, "修复失败", Toast.LENGTH_SHORT).show();
      }
    }
  }

  public static String readStr(InputStream in, String charset) throws IOException {
    if (TextUtils.isEmpty(charset)) {
      charset = "UTF-8";
    }

    if (!(in instanceof BufferedInputStream)) {
      in = new BufferedInputStream(in);
    }
    Reader reader = new InputStreamReader(in, charset);
    StringBuilder sb = new StringBuilder();
    char[] buf = new char[1024];
    int len;
    while ((len = reader.read(buf)) >= 0) {
      sb.append(buf, 0, len);
    }
    return sb.toString();
  }
}

