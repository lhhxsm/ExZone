package com.exzone.lib.okhttp.callback;

import okhttp3.Call;
import okhttp3.Request;
import okhttp3.Response;

/**
 * 作者:李鸿浩
 * 描述:
 * 时间：2016/10/24.
 */
public abstract class Callback<T> {
    public void onStart(Request request, int id) {
    }

    public void onEnd(int id) {
    }

    public void inProgress(float progress, long total, int id) {
    }

    /**
     * 验证响应码是否正确
     */
    public boolean validateResponse(Response response, int id) {
        return response.isSuccessful();
    }

    public abstract T parseResponse(Response response, int id) throws Exception;

    public abstract void onResponse(T response, int id);

    public abstract void onError(Call call, Exception e, int id);

    public static Callback CALLBACK_DEFAULT = new Callback() {
        @Override
        public Object parseResponse(Response response, int id) throws Exception {
            return null;
        }

        @Override
        public void onResponse(Object response, int id) {

        }

        @Override
        public void onError(Call call, Exception e, int id) {

        }
    };
}
