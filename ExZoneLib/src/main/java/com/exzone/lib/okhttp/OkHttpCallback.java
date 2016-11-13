package com.exzone.lib.okhttp;

/**
 * 作者:李鸿浩
 * 描述:
 * 时间：2016/10/24.
 */
public interface OkHttpCallback {
    void onSuccess(String json);

    void onFailure();
}
