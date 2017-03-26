package com.exzone.lib.glide.loader;

import android.text.TextUtils;
import android.widget.ImageView;

import com.exzone.lib.R;

/**
 * 作者:李鸿浩
 * 描述:
 * 时间:2017/3/26.
 */
public class ImageRequest {
    private String url;
    private int placeHolder;
    private ImageView imageView;

    public ImageRequest(Builder builder) {
        this.url = builder.url;
        this.placeHolder = builder.placeHolder;
        this.imageView = builder.imageView;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public int getPlaceHolder() {
        return placeHolder;
    }

    public void setPlaceHolder(int placeHolder) {
        this.placeHolder = placeHolder;
    }

    public ImageView getImageView() {
        return imageView;
    }

    public void setImageView(ImageView imageView) {
        this.imageView = imageView;
    }

    public static class Builder {
        private String url;
        private int placeHolder;
        private ImageView imageView;

        public Builder() {
            this.url = "";
            this.placeHolder = R.mipmap.ic_launcher;
            this.imageView = null;
        }

        public Builder url(String url) {
            this.url = url;
            return this;
        }

        public Builder placeHolder(int placeHolder) {
            this.placeHolder = placeHolder;
            return this;
        }

        public Builder imageView(ImageView imageView) {
            this.imageView = imageView;
            return this;
        }

        public ImageRequest create() {
            if (imageView == null) {
                throw new IllegalArgumentException("the imageView required");
            }
            if (TextUtils.isEmpty(url)) {
                throw new IllegalArgumentException("the url cannot be empty");
            }
            return new ImageRequest(this);
        }
    }
}
