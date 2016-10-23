package com.exzone.lib.okhttp.request;

import java.util.concurrent.ConcurrentHashMap;

import okhttp3.Callback;
import okhttp3.Headers;
import okhttp3.Request;
import okhttp3.RequestBody;

/**
 * 作者:李鸿浩
 * 描述:
 * 时间：2016/10/23.
 */
public abstract class OkHttpRequest {
    protected String mUrl;
    protected Object mTag;
    protected ConcurrentHashMap<String, String> mParams;
    protected ConcurrentHashMap<String, String> mHeaders;
    protected int mId;
    protected Request.Builder mBuilder = new Request.Builder();

    public OkHttpRequest(String url, Object tag, ConcurrentHashMap<String, String> params,
                         ConcurrentHashMap<String, String> headers, int id) {
        mUrl = url;
        mTag = tag;
        mParams = params;
        mHeaders = headers;
        mId = id;
        if (mUrl == null) {
            throw new IllegalArgumentException("url can not be null.");
        }
        init();
    }

    /**
     * 初始化一些基本参数url,tag,headers
     */
    private void init() {
        mBuilder.url(mUrl).tag(mTag);
        appendHeaders();
    }

    protected abstract RequestBody buildRequestBody();

    protected RequestBody wrapRequestBody(RequestBody requestBody) {
        return requestBody;
    }

    protected abstract Request buildRequest(RequestBody requestBody);

    public RequestCall build() {
        return new RequestCall(this);
    }

    public Request request(Callback callback) {
        RequestBody requestBody = buildRequestBody();
        RequestBody wrapRequestBody = wrapRequestBody(requestBody);
        return buildRequest(wrapRequestBody);
    }


    protected void appendHeaders() {
        Headers.Builder builder = new Headers.Builder();
        if (mHeaders == null || mHeaders.isEmpty()) {
            return;
        }
        for (String key : mHeaders.keySet()) {
            builder.add(key, mHeaders.get(key));
        }
        mBuilder.headers(builder.build());
    }

    public int getId() {
        return mId;
    }
}
