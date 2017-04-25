package com.exzone.demo;

import android.content.Intent;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import com.exzone.demo.camera.CameraCore;
import com.exzone.demo.camera.CameraProxy;
import java.io.File;
import net.bither.util.NativeUtil;

/**
 * 作者:李鸿浩
 * 描述:
 * 时间：2016/10/7.
 */
public class CameraActivity extends AppCompatActivity implements CameraCore.CameraResult {
  /**
   * SD卡根目录
   */
  private final String externalStorageDirectory =
      Environment.getExternalStorageDirectory().getPath() + "/atest/picture/";
  private Button choose_image;
  private CameraProxy cameraProxy;
  private String fileDir = "";
  private ImageView choose_bit;

  @Override protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_camera);
    //APP缓存目录 拍照缓存
    fileDir = getExternalCacheDir().getPath();
    //压缩后保存临时文件目录
    File tempFile = new File(externalStorageDirectory);
    if (!tempFile.exists()) {
      tempFile.mkdirs();
    }
    cameraProxy = new CameraProxy(this, CameraActivity.this);

    choose_image = (Button) findViewById(R.id.choose_image);
    choose_image.setOnClickListener(new View.OnClickListener() {

      @Override public void onClick(View v) {
        cameraProxy.getPhoto2Camera(fileDir + "/temp.jpg");
      }
    });

    choose_bit = (ImageView) findViewById(R.id.choose_bit);
  }

  //拍照选图片成功回调
  @Override public void onSuccess(final String filePath) {
    File file = new File(filePath);
    if (file.exists()) {
      new Thread() {
        public void run() {
          final File file = new File(externalStorageDirectory + "/temp.jpg");
          NativeUtil.compressBitmap(BitmapFactory.decodeFile(filePath), file.getPath());
          CameraActivity.this.runOnUiThread(new Runnable() {
            @Override public void run() {
              choose_bit.setImageBitmap(BitmapFactory.decodeFile(file.getPath()));
            }
          });
        }
      }.start();
    }
  }

  //拍照选图片失败回调
  @Override public void onFail(String message) {
    System.out.println("message = " + message);
  }

  @Override protected void onActivityResult(int requestCode, int resultCode, Intent data) {
    super.onActivityResult(requestCode, resultCode, data);
    cameraProxy.onResult(requestCode, resultCode, data);
  }
}