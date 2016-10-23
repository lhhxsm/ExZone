package com.exzone.lib.okhttp.cookie.store;

import java.util.List;

import okhttp3.Cookie;
import okhttp3.HttpUrl;

/**
 * 作者:李鸿浩
 * 描述:
 * 时间：2016/10/23.
 */
public interface CookieStore {

    void add(HttpUrl httpUrl, List<Cookie> cookies);

    List<Cookie> get(HttpUrl httpUrl);

    List<Cookie> getCookies();

    boolean remove(HttpUrl httpUrl, Cookie cookie);

    boolean removeAll();
}
