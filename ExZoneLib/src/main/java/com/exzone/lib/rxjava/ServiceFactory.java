package com.exzone.lib.rxjava;

import com.exzone.lib.base.BaseApplication;
import com.exzone.lib.util.Logger;
import com.exzone.lib.util.NetWorkUtils;
import com.google.gson.GsonBuilder;

import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;
import java.util.concurrent.TimeUnit;

import okhttp3.Authenticator;
import okhttp3.Cache;
import okhttp3.CacheControl;
import okhttp3.Credentials;
import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.Route;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

import static com.bumptech.glide.gifdecoder.GifHeaderParser.TAG;

/**
 * Created by lhh on 2017/3/25.
 */

public class ServiceFactory {
    private final static HttpLoggingInterceptor HTTP_LOGGING_INTERCEPTOR = new HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BODY);
    private final static Interceptor REWRITE_CACHE_CONTROL_INTERCEPTOR = new Interceptor() {
        @Override
        public Response intercept(Chain chain) throws IOException {
            Request request = chain.request();
            //获取网络状态
            int netWorkState = NetWorkUtils.getConnectedType(BaseApplication.getInstance());
            //无网络，请求强制使用缓存
            if (netWorkState == -1) {
                request = request.newBuilder()
                        .cacheControl(CacheControl.FORCE_CACHE)
                        .build();
            }

            Response originalResponse = chain.proceed(request);

            switch (netWorkState) {
                case 0://mobile network 情况下缓存一分钟
                    int maxAge = 60;
                    return originalResponse.newBuilder()
                            .removeHeader("Pragma")
                            .removeHeader("Cache-Control")
                            .header("Cache-Control", "public, max-age=" + maxAge)
                            .build();

                case 1://wifi network 情况下不使用缓存
                    maxAge = 0;
                    return originalResponse.newBuilder()
                            .removeHeader("Pragma")
                            .removeHeader("Cache-Control")
                            .header("Cache-Control", "public, max-age=" + maxAge)
                            .build();

                case -1://none network 情况下离线缓存7天
                    int maxStale = 60 * 60 * 24 * 7;
                    return originalResponse.newBuilder()
                            .removeHeader("Pragma")
                            .removeHeader("Cache-Control")
                            .header("Cache-Control", "public, only-if-cached, max-stale=" + maxStale)
                            .build();
                default:
                    throw new IllegalStateException("network state  is Error!");
            }
        }
    };

    private ServiceFactory() {
    }

    public static <T> T createService(final Class<T> service) {
        Retrofit retrofit = new Retrofit.Builder()
                .client(getOkHttpClient())
                .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                .addConverterFactory(GsonConverterFactory.create(new GsonBuilder()
                        .setDateFormat("yyyy-MM-dd hh:mm:ss")
                        .create()))
                .baseUrl(APIService.ENDPOINT)
                .build();
        return retrofit.create(service);
    }

    public static OkHttpClient getOkHttpClient() {
        //设置缓存路径
        final File httpCacheDirectory = new File(BaseApplication.getInstance().getCacheDir(), "Cache");

        Logger.e(TAG, httpCacheDirectory.getAbsolutePath());
        //设置缓存 10M
        Cache cache = new Cache(httpCacheDirectory, 10 * 1024 * 1024);   //缓存可用大小为10M

        return new OkHttpClient.Builder()
                .writeTimeout(30 * 1000, TimeUnit.MILLISECONDS)
                .readTimeout(20 * 1000, TimeUnit.MILLISECONDS)
                .connectTimeout(15 * 1000, TimeUnit.MILLISECONDS)
                //设置拦截器，显示日志信息
                .addInterceptor(HTTP_LOGGING_INTERCEPTOR)
                .addNetworkInterceptor(REWRITE_CACHE_CONTROL_INTERCEPTOR)
                .addInterceptor(REWRITE_CACHE_CONTROL_INTERCEPTOR)
                .cache(cache)
                //                .authenticator(new TokenAuthenticator())
                //                .sslSocketFactory(...)
                //                .hostnameVerifier(...)
                //失败重连
                .retryOnConnectionFailure(true)
                .build();
    }

    /**
     * 处理身份验证,有些网络请求是需要用户名密码登录的
     */
    private static class TokenAuthenticator implements Authenticator {
        @Override
        public Request authenticate(Route route, Response response) throws IOException {
            String credential = Credentials.basic("UserName", "Password", Charset.forName("UTF-8"));//
            if (credential.equals(response.request().header("Authorization"))) {
                return null;
            }
            return response.request().newBuilder().header("Authorization", credential).build();
        }
    }
}
