package com.exzone.lib.util;

import android.content.Context;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.List;

/**
 * 描述:
 * 作者:李鸿浩
 * 时间:2016/12/6.
 */

public class GsonUtils {


    public static <T> List<T> convertList(String jsonStr) {
        Type token = new TypeToken<List<T>>() {
        }.getType();
        Gson gson = new Gson();
        return gson.fromJson(jsonStr, token);
    }

    public static <T> T convertEntity(Context context, String jsonStr) {
        T t = null;
        Type trueType = ((ParameterizedType) context.getClass().getGenericSuperclass()).getActualTypeArguments()[0];
        Gson gson = new Gson();
        t = gson.fromJson(jsonStr, trueType);
        return t;
    }
}
