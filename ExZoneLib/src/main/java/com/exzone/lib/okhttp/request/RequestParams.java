package com.exzone.lib.okhttp.request;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 作者:李鸿浩
 * 描述:
 * 时间：2016/10/24.
 */
public class RequestParams {
    public ConcurrentHashMap<String, String> mParams = new ConcurrentHashMap<>();

    public RequestParams() {
        this(null);
    }

    public RequestParams(Map<String, String> params) {
        if (params != null) {
            for (Map.Entry<String, String> entry : params.entrySet()) {
                put(entry.getKey(), entry.getValue());
            }
        }
    }

    public RequestParams(final String key, final String value) {
        this(new HashMap<String, String>() {
            {
                put(key, value);
            }
        });
    }

    public void put(String key, String value) {
        if (key != null && value != null) {
            mParams.put(key, value);
        }
    }
}
