package com.exzone.lib.okhttp;

import android.content.Context;

import com.exzone.lib.okhttp.callback.Callback;
import com.exzone.lib.okhttp.log.LoggerInterceptor;
import com.exzone.lib.okhttp.request.RequestCall;
import com.exzone.lib.okhttp.utils.HttpsUtils;
import com.exzone.lib.okhttp.utils.Platform;
import com.franmontiel.persistentcookiejar.ClearableCookieJar;
import com.franmontiel.persistentcookiejar.PersistentCookieJar;
import com.franmontiel.persistentcookiejar.cache.SetCookieCache;
import com.franmontiel.persistentcookiejar.persistence.SharedPrefsCookiePersistor;

import java.io.IOException;
import java.util.concurrent.Executor;
import java.util.concurrent.TimeUnit;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.SSLSession;

import okhttp3.Call;
import okhttp3.OkHttpClient;
import okhttp3.Response;

/**
 * 作者:李鸿浩
 * 描述:
 * 时间：2016/10/23.
 */
public class OkHttpManager {
    public static final long DEFAULT_MILLISECONDS = 10000L;
    private volatile static OkHttpManager sInstance;
    private OkHttpClient mOkHttpClient;
    private Platform mPlatform;

    public OkHttpManager(OkHttpClient client) {
        if (client == null) {
            this.mOkHttpClient = new OkHttpClient();
        } else {
            this.mOkHttpClient = client;
        }
        mPlatform = Platform.getPlatform();
    }

    public static OkHttpManager init(OkHttpClient client) {
        if (sInstance == null) {
            synchronized (OkHttpManager.class) {
                if (sInstance == null) {
                    sInstance = new OkHttpManager(client);
                }
            }
        }
        return sInstance;
    }

    public static OkHttpManager getInstance() {
        return init(null);
    }


    //    public OkHttpManager(Context context) {
    //        ClearableCookieJar cookieJar = new PersistentCookieJar(new SetCookieCache(),
    //                new SharedPrefsCookiePersistor(context));
    //        HttpsUtils.SSLParams sslParams = HttpsUtils.getSSLSocketFactory(null, null, null);
    //        mOkHttpClient = new OkHttpClient.Builder().cookieJar(cookieJar)
    //                .connectTimeout(DEFAULT_MILLISECONDS, TimeUnit.MILLISECONDS)
    //                .readTimeout(DEFAULT_MILLISECONDS, TimeUnit.MILLISECONDS)
    //                .writeTimeout(DEFAULT_MILLISECONDS, TimeUnit.MILLISECONDS)
    //                .addInterceptor(new LoggerInterceptor("OkHttp"))
    //                .followRedirects(true)
    //                .sslSocketFactory(sslParams.sSLSocketFactory, sslParams.trustManager)
    //                .build();
    //        mPlatform = Platform.getPlatform();
    //    }
    //
    //    public static OkHttpManager init(Context context) {
    //        if (sInstance == null) {
    //            synchronized (OkHttpManager.class) {
    //                if (sInstance == null) {
    //                    sInstance = new OkHttpManager(context);
    //                }
    //            }
    //        }
    //        return sInstance;
    //    }


    public Executor getDeliver() {
        return mPlatform.defaultCallbackExecutor();
    }

    public OkHttpClient getOkHttpClient() {
        return mOkHttpClient;
    }

    public void execute(final RequestCall requestCall, Callback callback) {
        if (callback == null) {
            callback = Callback.CALLBACK_DEFAULT;
        }
        final Callback finalCallback = callback;
        final int id = requestCall.getOkHttpRequest().getId();
        requestCall.getCall().enqueue(new okhttp3.Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                onFailureCallback(call, e, finalCallback, id);
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                try {
                    if (call.isCanceled()) {
                        onFailureCallback(call, new IOException("Canceled"), finalCallback, id);
                        return;
                    }
                    if (!finalCallback.validateResponse(response, id)) {
                        onFailureCallback(call, new IOException("request failed , response's code" +
                                " is : " + response.code()), finalCallback, id);
                        return;
                    }
                    Object object = finalCallback.parseResponse(response, id);
                    onSuccessCallback(object, finalCallback, id);
                } catch (Exception e) {
                    //                    e.printStackTrace();
                    onFailureCallback(call, e, finalCallback, id);
                } finally {
                    if (response.body() != null) {
                        response.body().close();
                    }
                }
            }
        });
    }

    public void onFailureCallback(final Call call, final Exception e, final Callback callback,
                                  final int id) {
        if (callback == null) {
            return;
        }
        mPlatform.execute(new Runnable() {
            @Override
            public void run() {
                callback.onError(call, e, id);
                callback.onEnd(id);
            }
        });
    }

    public void onSuccessCallback(final Object object, final Callback callback, final int id) {
        if (callback == null) {
            return;
        }
        mPlatform.execute(new Runnable() {
            @Override
            public void run() {
                callback.onResponse(object, id);
                callback.onEnd(id);
            }
        });
    }

    public void cancelTag(Object tag) {
        for (Call call : mOkHttpClient.dispatcher().queuedCalls()) {
            if (tag.equals(call.request().tag())) {
                call.cancel();
            }
        }
        for (Call call : mOkHttpClient.dispatcher().runningCalls()) {
            if (tag.equals(call.request().tag())) {
                call.cancel();
            }
        }
    }

    public static class METHOD {
        public static final String HEAD = "HEAD";
        public static final String DELETE = "DELETE";
        public static final String PUT = "PUT";
        public static final String PATCH = "PATCH";
    }
}
