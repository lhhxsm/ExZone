package com.exzone.lib.okhttp.request;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;

/**
 * 作者:李鸿浩
 * 描述:
 * 时间：2016/10/24.
 */
public class RequestCall {
    private OkHttpRequest mOkHttpRequest;
    private Request mRequest;
    private Call mCall;
    private long mConnTimeOut;
    private long mReadTimeOut;
    private long mWriteTimeOut;
    private OkHttpClient mOkHttpClient;

    public RequestCall(OkHttpRequest okHttpRequest) {
        this.mOkHttpRequest = okHttpRequest;
    }

    public RequestCall connTimeOut(long connTimeOut) {
        this.mConnTimeOut = connTimeOut;
        return this;
    }

    public RequestCall readTimeOut(long readTimeOut) {
        this.mReadTimeOut = readTimeOut;
        return this;
    }

    public RequestCall writeTimeOut(long writeTimeOut) {
        this.mWriteTimeOut = writeTimeOut;
        return this;
    }

    public Call buildCall(Callback callback) {
        return mCall;
    }
}
