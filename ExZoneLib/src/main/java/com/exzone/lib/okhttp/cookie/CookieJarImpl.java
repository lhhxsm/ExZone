package com.exzone.lib.okhttp.cookie;

import com.exzone.lib.okhttp.cookie.store.CookieStore;

import java.util.List;

import okhttp3.Cookie;
import okhttp3.CookieJar;
import okhttp3.HttpUrl;

/**
 * 作者:李鸿浩
 * 描述:
 * 时间：2016/10/23.
 */
public class CookieJarImpl implements CookieJar {
    private CookieStore mCookieStore;

    public CookieJarImpl(CookieStore cookieStore) {
        if (cookieStore == null) {
            throw new IllegalArgumentException("cookieStore can not be null.");
        }
        this.mCookieStore = cookieStore;
    }

    @Override
    public synchronized void saveFromResponse(HttpUrl url, List<Cookie> cookies) {
        mCookieStore.add(url, cookies);
    }

    @Override
    public synchronized List<Cookie> loadForRequest(HttpUrl url) {
        return mCookieStore.get(url);
    }

    public CookieStore getCookieStore() {
        return mCookieStore;
    }
}
