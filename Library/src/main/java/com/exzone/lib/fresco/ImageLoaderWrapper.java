package com.exzone.lib.fresco;

import android.net.Uri;
import android.support.annotation.DrawableRes;
import java.io.File;

/**
 * 作者:lhh
 * 描述:
 * 时间: 2016/10/11.
 */
public interface ImageLoaderWrapper<TARGET extends Object, OPTION extends ImageLoaderWrapper.ImageOption> {
  void showImage(TARGET imageView, Uri uri, OPTION option);

  void showImage(TARGET imageView, String url, OPTION option);

  void showImage(TARGET imageView, File file, OPTION option);

  void showImage(TARGET imageView, @DrawableRes int id, OPTION option);

  OPTION newOption(int resizeW, int resizeH);

  interface ImageOption {
  }
}
