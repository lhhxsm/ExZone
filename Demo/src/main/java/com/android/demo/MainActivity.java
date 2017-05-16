package com.android.demo;

import android.os.Environment;
import android.text.TextUtils;
import android.view.View;
import android.widget.TextView;
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

  private TextView mTvHello;

  @Override protected void setContentView() {
    setContentView(R.layout.activity_main);
  }

  @Override protected void initTitle() {

  }

  @Override protected void initView() {
    mTvHello = viewById(R.id.tv_hello);
    mTvHello.setOnClickListener(new View.OnClickListener() {
      @Override public void onClick(View v) {
        Toast.makeText(MainActivity.this, 2 / 1 + "Bug修复", Toast.LENGTH_SHORT).show();
      }
    });
  }

  @Override protected void initData() {

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
    fixDexBug();
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

