package com.exzone.lib.okhttp.request;

import com.exzone.lib.okhttp.OkHttpManager;
import com.exzone.lib.okhttp.callback.Callback;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

import okhttp3.Call;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

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
        mRequest = mOkHttpRequest.createRequest(callback);
        if (mConnTimeOut > 0 || mReadTimeOut > 0 || mWriteTimeOut > 0) {
            mConnTimeOut = mConnTimeOut > 0 ? mConnTimeOut : OkHttpManager.DEFAULT_MILLISECONDS;
            mReadTimeOut = mReadTimeOut > 0 ? mReadTimeOut : OkHttpManager.DEFAULT_MILLISECONDS;
            mWriteTimeOut = mWriteTimeOut > 0 ? mWriteTimeOut : OkHttpManager.DEFAULT_MILLISECONDS;

            mOkHttpClient = OkHttpManager.getInstance().getOkHttpClient().newBuilder()
                    .connectTimeout(mConnTimeOut, TimeUnit.MILLISECONDS)
                    .readTimeout(mReadTimeOut, TimeUnit.MILLISECONDS)
                    .writeTimeout(mWriteTimeOut, TimeUnit.MILLISECONDS)
                    .build();
            mCall = mOkHttpClient.newCall(mRequest);
        } else {
            mCall = OkHttpManager.getInstance().getOkHttpClient().newCall(mRequest);
        }
        return mCall;
    }

    public void execute(Callback callback) {
        buildCall(callback);
        if (callback != null) {
            callback.onStart(mRequest, getOkHttpRequest().getId());
        }
        OkHttpManager.getInstance().execute(this, callback);
    }

    public OkHttpRequest getOkHttpRequest() {
        return mOkHttpRequest;
    }

    public Call getCall() {
        return mCall;
    }

    public Request getRequest() {
        return mRequest;
    }

    public Response execute() throws IOException {
        buildCall(null);
        return mCall.execute();
    }

    public void cancel() {
        if (mCall != null) {
            mCall.cancel();
        }
    }
}
