package com.exzone.lib.rxjava;

import android.database.Observable;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import junit.framework.Test;

import java.util.List;

import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.http.GET;
import retrofit2.http.Headers;

/**
 * 作者:李鸿浩
 * 描述:
 * 时间:2016/7/6.
 */
public interface APIService {
    @Headers(RetrofitManager.CACHE_CONTROL_AGE + RetrofitManager.CACHE_STALE_SHORT)
    @GET("")
    Observable<Test> getTest();

    String ENDPOINT = "https://api.ribot.io/";

    @GET("")
    Observable<List<?>> getRibots();

    /********
     * Helper class that sets up a new services
     *******/
    class Creator {

        public static APIService newAPIService() {
            Gson gson = new GsonBuilder()
//                    .registerTypeAdapterFactory(new AutoValueGsonTypeAdapterFactory())
                    .setDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'")
                    .create();
            Retrofit retrofit = new Retrofit.Builder()
                    .baseUrl(APIService.ENDPOINT)
                    .addConverterFactory(GsonConverterFactory.create(gson))
                    .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                    .build();
            return retrofit.create(APIService.class);
        }
    }
}
