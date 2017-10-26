package com.exzone.demo.camera;

import android.content.Context;
import android.graphics.Point;
import android.hardware.Camera;
import android.hardware.Camera.AutoFocusCallback;
import android.hardware.Camera.CameraInfo;
import android.hardware.Camera.PictureCallback;
import android.os.Build;
import android.util.AttributeSet;
import android.view.Display;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.WindowManager;
import com.exzone.lib.util.Logger;
import com.exzone.lib.util.ToastUtils;
import java.io.IOException;
import java.lang.reflect.Method;
import java.util.regex.Pattern;

/**
 * 作者:lhh
 * 描述:
 * 时间:2017/4/4.
 */
public class CameraPreview extends SurfaceView implements SurfaceHolder.Callback {

  private static final int TEN_DESIRED_ZOOM = 27;
  private static final int DESIRED_SHARPNESS = 30;
  private static final Pattern COMMA_PATTERN = Pattern.compile(",");
  private static final int SDK_INT;

  static {
    int sdkInt;
    try {
      sdkInt = Integer.parseInt(Build.VERSION.SDK);
    } catch (NumberFormatException nfe) {
      // 为安全起见
      sdkInt = 10000;
    }
    SDK_INT = sdkInt;
  }

  private final SurfaceHolder mHolder;
  public int cameraPosition = 1;
  private Camera mCamera = null;
  private OnCameraStatusListener listener;
  private Context mContext;
  private Point screenResolution;
  private Point cameraResolution;
  private int previewFormat;
  private String previewFormatString;
  private boolean initialized;
  // 创建一个PictureCallback对象，并实现其中的onPictureTaken方法
  private PictureCallback pictureCallback = new PictureCallback() {

    // 该方法用于处理拍摄后的照片数据
    @Override public void onPictureTaken(byte[] data, Camera camera) {
      // 停止照片拍摄
      Logger.e("callback" + listener + "");
      camera.stopPreview();
      camera = null;
      // 调用结束事件
      if (null != listener) {
        listener.onCameraStopped(data);
      }
    }
  };

  public CameraPreview(Context context, AttributeSet attrs) {
    super(context, attrs);
    this.mContext = context;
    mHolder = getHolder();
    mHolder.addCallback(this);
    // 推荐的设置，但需要在Android 3以前的版本
    mHolder.setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);
  }

  private static Point getCameraResolution(Camera.Parameters parameters, Point screenResolution) {

    String previewSizeValueString = parameters.get("preview-size-values");
    // saw this on Xperia
    if (previewSizeValueString == null) {
      previewSizeValueString = parameters.get("preview-size-value");
    }

    Point cameraResolution = null;

    if (previewSizeValueString != null) {
      Logger.e("preview-size-values parameter: " + previewSizeValueString);
      cameraResolution = findBestPreviewSizeValue(previewSizeValueString, screenResolution);
    }

    if (cameraResolution == null) {
      // Ensure that the camera resolution is a multiple of 8, as the screen may not be.
      cameraResolution = new Point((screenResolution.x >> 3) << 3, (screenResolution.y >> 3) << 3);
    }

    return cameraResolution;
  }

  private static Point findBestPreviewSizeValue(CharSequence previewSizeValueString,
      Point screenResolution) {
    int bestX = 0;
    int bestY = 0;
    int diff = Integer.MAX_VALUE;
    for (String previewSize : COMMA_PATTERN.split(previewSizeValueString)) {

      previewSize = previewSize.trim();
      int dimPosition = previewSize.indexOf('x');
      if (dimPosition < 0) {
        Logger.e("Bad preview-size: " + previewSize);
        continue;
      }

      int newX;
      int newY;
      try {
        newX = Integer.parseInt(previewSize.substring(0, dimPosition));
        newY = Integer.parseInt(previewSize.substring(dimPosition + 1));
      } catch (NumberFormatException nfe) {
        Logger.e("Bad preview-size: " + previewSize);
        continue;
      }

      int newDiff = Math.abs(newX - screenResolution.x) + Math.abs(newY - screenResolution.y);
      if (newDiff == 0) {
        bestX = newX;
        bestY = newY;
        break;
      } else if (newDiff < diff) {
        bestX = newX;
        bestY = newY;
        diff = newDiff;
      }
    }

    if (bestX > 0 && bestY > 0) {
      return new Point(bestX, bestY);
    }
    return null;
  }

  private static int findBestMotZoomValue(CharSequence stringValues, int tenDesiredZoom) {
    int tenBestValue = 0;
    for (String stringValue : COMMA_PATTERN.split(stringValues)) {
      stringValue = stringValue.trim();
      double value;
      try {
        value = Double.parseDouble(stringValue);
      } catch (NumberFormatException nfe) {
        return tenDesiredZoom;
      }
      int tenValue = (int) (10.0 * value);
      if (Math.abs(tenDesiredZoom - value) < Math.abs(tenDesiredZoom - tenBestValue)) {
        tenBestValue = tenValue;
      }
    }
    return tenBestValue;
  }

  public static int getDesiredSharpness() {
    return DESIRED_SHARPNESS;
  }

  @Override public void surfaceCreated(SurfaceHolder holder) {
    // surface已被创建，现在告诉相机在哪里画预览
    try {
      cameraPosition = 1;
      if (mCamera != null) {
        mCamera.setPreviewCallback(null);
        mCamera.stopPreview();//停掉原来摄像头的预览
        mCamera.release();//释放资源
      }
      mCamera = Camera.open();
      mCamera.setPreviewDisplay(holder);
      mCamera.startPreview();
    } catch (IOException e) {
      e.printStackTrace();
      Logger.e("open()方法有问题");
      Logger.e("Error setting camera preview: " + e.getMessage());
    }
  }

  @Override public void surfaceDestroyed(SurfaceHolder holder) {
    // 空，记得在activity中释放摄像头
    mCamera.setPreviewCallback(null);
    mCamera.stopPreview();
    mCamera.release();//加上这句，就OK！
    mCamera = null;
  }

  @Override public void surfaceChanged(SurfaceHolder holder, int format, int w, int h) {
    // 如果预览可以改变或旋转，注意这里的那些事件。
    // 确保在调整大小或格式时停止预览。
    if (mHolder.getSurface() == null) {
      // 预览不存在
      return;
    }
    Logger.e("camera size: " + w + " :　" + h);
    // 在更改前停止预览
    try {
      mCamera.stopPreview();
    } catch (Exception e) {
      e.printStackTrace();
      // 一个不存在的预览
    }
    try {
      mCamera.setPreviewDisplay(mHolder);
      initParameters();
      mCamera.startPreview();
    } catch (Exception e) {
      e.printStackTrace();
      Logger.e("Error starting camera preview: " + e.getMessage());
    }
  }

  private void initParameters() {
    if (!initialized) {
      initialized = true;
      initFromCameraParameters(mCamera);
    }
    setDesiredCameraParameters(mCamera);
  }

  public void startCameraPreview() {
    mCamera.startPreview();
  }

  public void releaseCamera() {
    if (mCamera != null) {
      mCamera.setPreviewCallback(null);
      mCamera.release();
    }
  }

  public void changeCameraFacing() {
    //切换前后摄像头
    int cameraCount = 0;
    CameraInfo cameraInfo = new CameraInfo();
    cameraCount = Camera.getNumberOfCameras();//得到摄像头的个数

    for (int i = 0; i < cameraCount; i++) {
      Camera.getCameraInfo(i, cameraInfo);//得到每一个摄像头的信息
      if (cameraPosition == 1) {
        //现在是后置，变更为前置
        if (cameraInfo.facing
            == Camera.CameraInfo.CAMERA_FACING_FRONT) {//代表摄像头的方位，CAMERA_FACING_FRONT前置      CAMERA_FACING_BACK后置
          mCamera.setPreviewCallback(null);
          mCamera.stopPreview();//停掉原来摄像头的预览
          mCamera.release();//释放资源
          mCamera = null;//取消原来摄像头
          mCamera = Camera.open(i);//打开当前选中的摄像头
          try {
            mCamera.setPreviewDisplay(mHolder);//通过SurfaceView显示取景画面
            initParameters();
          } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
          }
          mCamera.startPreview();//开始预览
          cameraPosition = 0;
          break;
        }
      } else {
        //现在是前置， 变更为后置
        if (cameraInfo.facing
            == Camera.CameraInfo.CAMERA_FACING_BACK) {//代表摄像头的方位，CAMERA_FACING_FRONT前置      CAMERA_FACING_BACK后置
          mCamera.setPreviewCallback(null);
          mCamera.stopPreview();//停掉原来摄像头的预览
          mCamera.release();//释放资源
          mCamera = null;//取消原来摄像头
          mCamera = Camera.open(i);//打开当前选中的摄像头
          try {
            mCamera.setPreviewDisplay(mHolder);//通过SurfaceView显示取景画面
            initParameters();
          } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
          }
          mCamera.startPreview();//开始预览
          cameraPosition = 1;
          break;
        }
      }
    }
  }

  // 停止拍照，并将拍摄的照片传入PictureCallback接口的onPictureTaken方法
  public void takePicture() {
    if (mCamera != null) {
      // 自动对焦
      mCamera.autoFocus(new AutoFocusCallback() {
        @Override public void onAutoFocus(boolean success, Camera camera) {
          if (null != listener) {
            try {
              success = true;//我这里把他设为true了，其实
              listener.onAutoFocus(success);
            } catch (Exception e) {
              e.printStackTrace();
              success = true;
            }
          }
          // 自动对焦成功后才拍摄
          if (success || cameraPosition == 0) {
            camera.takePicture(null, null, pictureCallback);
          } else {
            ToastUtils.showShort(mContext, "焦距不准，请重拍！");
          }
        }
      });
    }
  }

  // 设置监听事件
  public void setOnCameraStatusListener(OnCameraStatusListener listener) {
    if (listener != null) {
      this.listener = listener;
    }
  }

  /**
   * 应用程序所需要的相机值
   */
  void initFromCameraParameters(Camera camera) {
    Camera.Parameters parameters = camera.getParameters();
    previewFormat = parameters.getPreviewFormat();
    previewFormatString = parameters.get("preview-format");
    Logger.e("Default preview format: " + previewFormat + '/' + previewFormatString);
    WindowManager manager = (WindowManager) mContext.getSystemService(Context.WINDOW_SERVICE);
    Display display = manager.getDefaultDisplay();
    screenResolution = new Point(display.getWidth(), display.getHeight());
    Logger.e("Screen resolution: " + screenResolution);
    cameraResolution = getCameraResolution(parameters, screenResolution);
    Logger.e("Camera resolution: " + screenResolution);
  }

  /**
   * 这部分是用的zxing解析二维码中的代码
   */
  void setDesiredCameraParameters(Camera camera) {
    Camera.Parameters parameters = camera.getParameters();
    Logger.e("Setting preview size: " + cameraResolution);
    parameters.setPreviewSize(cameraResolution.x, cameraResolution.y);
    setFlash(parameters);
    setZoom(parameters);
    //setSharpness(parameters);
    //modify here

    //	    camera.setDisplayOrientation(90);
    //2.1
    setDisplayOrientation(camera, 90);
    camera.setParameters(parameters);
  }

  private void setFlash(Camera.Parameters parameters) {
    // FIXME: This is a hack to turn the flash off on the Samsung Galaxy.
    // And this is a hack-hack to work around a different value on the Behold II
    // Restrict Behold II check to Cupcake, per Samsung's advice
    //if (Build.MODEL.contains("Behold II") &&
    //    CameraManager.SDK_INT == Build.VERSION_CODES.CUPCAKE) {
    if (Build.MODEL.contains("Behold II") && SDK_INT == 3) { // 3 = Cupcake
      parameters.set("flash-value", 1);
    } else {
      parameters.set("flash-value", 2);
    }
    // This is the standard setting to turn the flash off that all devices should honor.
    parameters.set("flash-mode", "off");
  }

  private void setZoom(Camera.Parameters parameters) {

    String zoomSupportedString = parameters.get("zoom-supported");
    if (zoomSupportedString != null && !Boolean.parseBoolean(zoomSupportedString)) {
      return;
    }

    int tenDesiredZoom = TEN_DESIRED_ZOOM;

    String maxZoomString = parameters.get("max-zoom");
    if (maxZoomString != null) {
      try {
        int tenMaxZoom = (int) (10.0 * Double.parseDouble(maxZoomString));
        if (tenDesiredZoom > tenMaxZoom) {
          tenDesiredZoom = tenMaxZoom;
        }
      } catch (NumberFormatException e) {
        e.printStackTrace();
        Logger.e("Bad max-zoom: " + maxZoomString);
      }
    }

    String takingPictureZoomMaxString = parameters.get("taking-picture-zoom-max");
    if (takingPictureZoomMaxString != null) {
      try {
        int tenMaxZoom = Integer.parseInt(takingPictureZoomMaxString);
        if (tenDesiredZoom > tenMaxZoom) {
          tenDesiredZoom = tenMaxZoom;
        }
      } catch (NumberFormatException e) {
        e.printStackTrace();
        Logger.e("Bad taking-picture-zoom-max: " + takingPictureZoomMaxString);
      }
    }

    String motZoomValuesString = parameters.get("mot-zoom-values");
    if (motZoomValuesString != null) {
      tenDesiredZoom = findBestMotZoomValue(motZoomValuesString, tenDesiredZoom);
    }

    String motZoomStepString = parameters.get("mot-zoom-step");
    if (motZoomStepString != null) {
      try {
        double motZoomStep = Double.parseDouble(motZoomStepString.trim());
        int tenZoomStep = (int) (10.0 * motZoomStep);
        if (tenZoomStep > 1) {
          tenDesiredZoom -= tenDesiredZoom % tenZoomStep;
        }
      } catch (NumberFormatException nfe) {
        // continue
      }
    }

    // Set zoom. This helps encourage the user to pull back.
    // Some devices like the Behold have a zoom parameter
    if (maxZoomString != null || motZoomValuesString != null) {
      parameters.set("zoom", String.valueOf(tenDesiredZoom / 10.0));
    }

    // Most devices, like the Hero, appear to expose this zoom parameter.
    // It takes on values like "27" which appears to mean 2.7x zoom
    if (takingPictureZoomMaxString != null) {
      parameters.set("taking-picture-zoom", tenDesiredZoom);
    }
  }

  /**
   * compatible  1.6
   */
  protected void setDisplayOrientation(Camera camera, int angle) {
    Method downPolymorphic;
    try {
      downPolymorphic =
          camera.getClass().getMethod("setDisplayOrientation", new Class[] { int.class });
      if (downPolymorphic != null) {
        downPolymorphic.invoke(camera, new Object[] { angle });
      }
    } catch (Exception e) {
      e.printStackTrace();
    }
  }

  /**
   * 相机拍照监听接口
   */
  public interface OnCameraStatusListener {

    // 相机拍照结束事件
    void onCameraStopped(byte[] data);

    // 拍摄时自动对焦事件
    void onAutoFocus(boolean success);
  }
}
