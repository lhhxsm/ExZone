package com.exzone.lib.okhttp;

import android.content.Context;

import com.exzone.lib.okhttp.log.LoggerInterceptor;
import com.exzone.lib.okhttp.utils.HttpsUtils;
import com.exzone.lib.okhttp.utils.Platform;
import com.franmontiel.persistentcookiejar.ClearableCookieJar;
import com.franmontiel.persistentcookiejar.PersistentCookieJar;
import com.franmontiel.persistentcookiejar.cache.SetCookieCache;
import com.franmontiel.persistentcookiejar.persistence.SharedPrefsCookiePersistor;

import java.util.concurrent.Executor;
import java.util.concurrent.TimeUnit;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.SSLSession;

import okhttp3.OkHttpClient;

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
}
