package com.exzone.lib.glide.loader;

import android.content.Context;
import com.bumptech.glide.Glide;
import com.bumptech.glide.Priority;
import com.bumptech.glide.load.data.DataFetcher;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.model.stream.StreamModelLoader;
import com.exzone.lib.util.NetWorkUtils;
import java.io.IOException;
import java.io.InputStream;

/**
 * 作者:李鸿浩
 * 描述:
 * 时间:2017/3/26.
 */
public class GlideImageLoaderProvider implements IImageLoaderProvider {
  @Override public void loadImage(ImageRequest request) {
    Context context = request.getImageView().getContext();
    if (NetWorkUtils.isConnected(context)) {
      loadNormal(context, request);
    } else {
      loadCache(context, request);
    }
  }

  private void loadNormal(Context context, ImageRequest request) {
    Glide.with(context)
        .load(request.getUrl())
        .placeholder(request.getPlaceHolder())
        .dontAnimate()
        .diskCacheStrategy(DiskCacheStrategy.SOURCE)
        .into(request.getImageView());
  }

  private void loadCache(Context context, ImageRequest request) {
    Glide.with(context).using(new StreamModelLoader<String>() {
      @Override public DataFetcher<InputStream> getResourceFetcher(final String model, int width,
          int height) {
        return new DataFetcher<InputStream>() {
          @Override public InputStream loadData(Priority priority) throws Exception {
            throw new IOException();
          }

          @Override public void cleanup() {

          }

          @Override public String getId() {
            return model;
          }

          @Override public void cancel() {

          }
        };
      }
    });
  }
}
