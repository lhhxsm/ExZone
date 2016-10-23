package com.exzone.lib.okhttp.cookie.store;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import okhttp3.Cookie;
import okhttp3.HttpUrl;

/**
 * 作者:李鸿浩
 * 描述:
 * 时间：2016/10/23.
 */
public class MemoryCookieStore implements CookieStore {
    private final HashMap<String, List<Cookie>> mAllCookies = new HashMap<>();

    @Override
    public void add(HttpUrl httpUrl, List<Cookie> cookies) {
        List<Cookie> oldCookies = mAllCookies.get(httpUrl.host());
        if (oldCookies != null) {
            Iterator<Cookie> itNew = cookies.iterator();
            Iterator<Cookie> itOld = oldCookies.iterator();
            while (itNew.hasNext()) {
                String va = itNew.next().name();
                while (va != null && itOld.hasNext()) {
                    String v = itOld.next().name();
                    if (v != null && va.equals(v)) {
                        itOld.remove();
                    }
                }
            }
            oldCookies.addAll(cookies);
        } else {
            mAllCookies.put(httpUrl.host(), cookies);
        }
    }

    @Override
    public List<Cookie> get(HttpUrl httpUrl) {
        List<Cookie> cookies = mAllCookies.get(httpUrl.host());
        if (cookies == null) {
            cookies = new ArrayList<>();
            mAllCookies.put(httpUrl.host(), cookies);
        }
        return cookies;
    }

    @Override
    public List<Cookie> getCookies() {
        List<Cookie> cookies = new ArrayList<>();
        Set<String> httpUrls = mAllCookies.keySet();
        for (String url : httpUrls) {
            cookies.addAll(mAllCookies.get(url));
        }
        return cookies;
    }

    @Override
    public boolean remove(HttpUrl httpUrl, Cookie cookie) {
        List<Cookie> cookies = mAllCookies.get(httpUrl.host());
        //        if (cookie != null) {
        //            return cookies.remove(cookie);
        //        }
        //        return false;
        return cookie != null && cookies.remove(cookie);
    }

    @Override
    public boolean removeAll() {
        mAllCookies.clear();
        return true;
    }
}
