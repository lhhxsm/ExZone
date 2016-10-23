package com.exzone.lib.okhttp.log;

import android.text.TextUtils;
import android.util.Log;

import com.exzone.lib.util.Logger;

import java.io.IOException;

import okhttp3.Headers;
import okhttp3.Interceptor;
import okhttp3.MediaType;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import okhttp3.ResponseBody;
import okio.Buffer;

/**
 * 作者:李鸿浩
 * 描述:
 * 时间：2016/10/23.
 */
public class LoggerInterceptor implements Interceptor {

    private String mTag;
    private boolean mShowResponse;

    public LoggerInterceptor(String tag, boolean showResponse) {
        this.mTag = tag;
        this.mShowResponse = showResponse;
    }

    public LoggerInterceptor(String tag) {
        this(tag, false);
    }

    @Override
    public Response intercept(Chain chain) throws IOException {
        Request request = chain.request();
        logForRequest(request);
        Response response = chain.proceed(request);
        return logForResponse(response);
    }

    private void logForRequest(Request request) {
        try {
            String url = request.url().toString();
            Headers headers = request.headers();
            Logger.e(mTag, "========== request'log ========== start");
            Logger.e(mTag, "method : " + request.method());
            Logger.e(mTag, "url : " + url);
            if (headers != null && headers.size() > 0) {
                Logger.e(mTag, "headers : " + headers.toString());
            }
            RequestBody requestBody = request.body();
            if (requestBody != null) {
                MediaType mediaType = requestBody.contentType();
                if (mediaType != null) {
                    Logger.e(mTag, "requestBody's contentType : " + mediaType.toString());
                    if (isText(mediaType)) {
                        Logger.e(mTag, "requestBody's content : " + bodyToString(request));
                    } else {
                        Logger.e(mTag, "requestBody's content : " + " maybe [file part] , too " +
                                "large too print , ignored!");
                    }
                }
            }
            Logger.e(mTag, "========== request'log ========== end");
        } catch (Exception e) {
            //            e.printStackTrace();
        }
    }


    private Response logForResponse(Response response) {
        try {
            Logger.e(mTag, "========== response'log ========== start");
            Response.Builder builder = response.newBuilder();
            Response clone = builder.build();
            Logger.e(mTag, "url : " + clone.request().url());
            Logger.e(mTag, "code : " + clone.code());
            Logger.e(mTag, "protocol : " + clone.protocol());
            if (!TextUtils.isEmpty(clone.message())) {
                Logger.e(mTag, "message : " + clone.message());
            }
            if (mShowResponse) {
                ResponseBody body = clone.body();
                if (body != null) {
                    MediaType mediaType = body.contentType();
                    if (mediaType != null) {
                        Logger.e(mTag, "responseBody's contentType : " + mediaType.toString());
                        if (isText(mediaType)) {
                            String resp = body.string();
                            Logger.e(mTag, "responseBody's content : " + resp);
                            body = ResponseBody.create(mediaType, resp);
                            return response.newBuilder().body(body).build();
                        } else {
                            Logger.e(mTag, "responseBody's content : " + " maybe [file part] , " +
                                    "too large too print , ignored!");
                        }
                    }
                }
            }
            Logger.e(mTag, "========== response'log ========== end");
        } catch (IOException e) {
            //            e.printStackTrace();
        }
        return response;
    }

    private boolean isText(MediaType mediaType) {
        if (mediaType.type() != null && mediaType.type().equals("text")) {
            return true;
        }
        if (mediaType.subtype() != null) {
            if (mediaType.subtype().equals("json") || mediaType.subtype().equals("xml") ||
                    mediaType.subtype().equals("html") || mediaType.subtype().equals
                    ("webviewhtml")) {
                return true;
            }
        }
        return false;
    }

    private String bodyToString(Request request) {
        try {
            Request copy = request.newBuilder().build();
            Buffer buffer = new Buffer();
            copy.body().writeTo(buffer);
            return buffer.readUtf8();
        } catch (IOException e) {
            return "something error when show requestBody.";
        }
    }
}
