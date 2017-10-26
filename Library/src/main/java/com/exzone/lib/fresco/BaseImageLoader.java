package com.exzone.lib.fresco;

import android.net.Uri;
import android.widget.ImageView;
import java.io.File;

/**
 * 作者:lhh
 * 描述:
 * 时间: 2016/10/11.
 */
public abstract class BaseImageLoader<TARGET extends ImageView, OPTION extends ImageLoaderWrapper.ImageOption>
    implements ImageLoaderWrapper<TARGET, OPTION> {

  @Override public void showImage(TARGET imageView, String url, OPTION option) {
    showImage(imageView, url == null ? Uri.EMPTY : Uri.parse(url), option);
  }

  @Override public void showImage(TARGET imageView, File file, OPTION option) {
    showImage(imageView, Uri.fromFile(file), option);
  }
}
