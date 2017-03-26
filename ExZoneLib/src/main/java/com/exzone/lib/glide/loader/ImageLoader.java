package com.exzone.lib.glide.loader;

import android.widget.ImageView;

import com.exzone.lib.R;

/**
 * 作者:李鸿浩
 * 描述:
 * 时间:2017/3/26.
 */
public class ImageLoader {
    private static volatile GlideImageLoaderProvider sProvider;

    private static GlideImageLoaderProvider getProvider() {
        if (sProvider == null) {
            synchronized (ImageLoader.class) {
                if (sProvider == null) {
                    sProvider = new GlideImageLoaderProvider();
                }
            }
        }
        return sProvider;
    }

    public static void displayImage(ImageView iv, String url) {
        ImageRequest request = new ImageRequest.Builder()
                .url(url)
                .placeHolder(R.mipmap.ic_launcher)
                .imageView(iv)
                .create();
        getProvider().loadImage(request);
    }

    public static void displayImage(ImageView iv, String url, int placeHolder) {
        ImageRequest request = new ImageRequest.Builder()
                .url(url)
                .placeHolder(placeHolder)
                .imageView(iv)
                .create();
        getProvider().loadImage(request);
    }
}
