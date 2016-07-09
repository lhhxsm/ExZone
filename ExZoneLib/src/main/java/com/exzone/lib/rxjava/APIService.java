package com.exzone.lib.rxjava;

import android.database.Observable;

import com.exzone.lib.Test;

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
}
