package com.exzone.lib.okhttp.cookie.store;

import android.content.Context;
import android.content.SharedPreferences;
import android.text.TextUtils;
import android.util.Log;

import com.exzone.lib.util.Logger;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import okhttp3.Cookie;
import okhttp3.HttpUrl;

/**
 * 作者:李鸿浩
 * 描述:
 * 时间：2016/10/23.
 * <pre>
 * OkHttpClient client = new OkHttpClient.Builder().cookieJar(new JavaNetCookieJar(
 * new CookieManager(new PersistentCookieStore(getApplicationContext()),CookiePolicy.ACCEPT_ALL))
 * .build();
 * </pre>
 */
public class PersistentCookieStore implements CookieStore {
    private static final String COOKIE_PREFS = "CookiePrefsFile";
    private static final String COOKIE_NAME_PREFIX = "cookie_";

    private final HashMap<String, ConcurrentHashMap<String, Cookie>> mCookies;
    private final SharedPreferences mCookiePrefs;

    public PersistentCookieStore(Context context) {
        mCookiePrefs = context.getSharedPreferences(COOKIE_PREFS, 0);
        mCookies = new HashMap<>();

        // Load any previously stored cookies into the store
        Map<String, ?> prefsMap = mCookiePrefs.getAll();
        for (Map.Entry<String, ?> entry : prefsMap.entrySet()) {
            if ((entry.getValue()) != null && !((String) entry.getValue()).startsWith
                    (COOKIE_NAME_PREFIX)) {
                String[] cookieNames = TextUtils.split((String) entry.getValue(), ",");
                for (String name : cookieNames) {
                    String encodedCookie = mCookiePrefs.getString(COOKIE_NAME_PREFIX + name, null);
                    if (encodedCookie != null) {
                        Cookie decodedCookie = decodeCookie(encodedCookie);
                        if (decodedCookie != null) {
                            if (!mCookies.containsKey(entry.getKey()))
                                mCookies.put(entry.getKey(), new ConcurrentHashMap<String,
                                        Cookie>());
                            mCookies.get(entry.getKey()).put(name, decodedCookie);
                        }
                    }
                }

            }
        }
    }

    protected void add(HttpUrl httpUrl, Cookie cookie) {
        String name = getCookieToken(cookie);
        if (cookie.persistent()) {
            if (!mCookies.containsKey(httpUrl.host())) {
                mCookies.put(httpUrl.host(), new ConcurrentHashMap<String, Cookie>());
            }
            mCookies.get(httpUrl.host()).put(name, cookie);
        } else {
            if (mCookies.containsKey(httpUrl.host())) {
                mCookies.get(httpUrl.host()).remove(name);
            } else {
                return;
            }
        }

        // Save cookie into persistent store
        SharedPreferences.Editor prefsWriter = mCookiePrefs.edit();
        prefsWriter.putString(httpUrl.host(), TextUtils.join(",", mCookies.get(httpUrl.host())
                .keySet()));
        prefsWriter.putString(COOKIE_NAME_PREFIX + name, encodeCookie(new SerializableHttpCookie
                (cookie)));
        prefsWriter.apply();
    }

    protected String getCookieToken(Cookie cookie) {
        return cookie.name() + cookie.domain();
    }


    @Override
    public void add(HttpUrl httpUrl, List<Cookie> cookies) {
        for (Cookie cookie : cookies) {
            add(httpUrl, cookie);
        }
    }

    @Override
    public List<Cookie> get(HttpUrl httpUrl) {
        ArrayList<Cookie> ret = new ArrayList<>();
        if (mCookies.containsKey(httpUrl.host())) {
            Collection<Cookie> cookies = this.mCookies.get(httpUrl.host()).values();
            for (Cookie cookie : cookies) {
                if (isCookieExpired(cookie)) {
                    remove(httpUrl, cookie);
                } else {
                    ret.add(cookie);
                }
            }
        }

        return ret;
    }

    private static boolean isCookieExpired(Cookie cookie) {
        return cookie.expiresAt() < System.currentTimeMillis();
    }

    @Override
    public List<Cookie> getCookies() {
        ArrayList<Cookie> ret = new ArrayList<>();
        for (String key : mCookies.keySet()) {
            ret.addAll(mCookies.get(key).values());
        }
        return ret;
    }

    @Override
    public boolean remove(HttpUrl httpUrl, Cookie cookie) {
        String name = getCookieToken(cookie);
        if (mCookies.containsKey(httpUrl.host()) && mCookies.get(httpUrl.host()).containsKey
                (name)) {
            mCookies.get(httpUrl.host()).remove(name);
            SharedPreferences.Editor prefsWriter = mCookiePrefs.edit();
            if (mCookiePrefs.contains(COOKIE_NAME_PREFIX + name)) {
                prefsWriter.remove(COOKIE_NAME_PREFIX + name);
            }
            prefsWriter.putString(httpUrl.host(), TextUtils.join(",", mCookies.get(httpUrl.host()
            ).keySet()));
            prefsWriter.apply();
            return true;
        } else {
            return false;
        }
    }

    @Override
    public boolean removeAll() {
        SharedPreferences.Editor prefsWriter = mCookiePrefs.edit();
        prefsWriter.clear();
        prefsWriter.apply();
        mCookies.clear();
        return true;
    }

    protected String encodeCookie(SerializableHttpCookie cookie) {
        if (cookie == null) {
            return null;
        }
        ByteArrayOutputStream os = new ByteArrayOutputStream();
        try {
            ObjectOutputStream outputStream = new ObjectOutputStream(os);
            outputStream.writeObject(cookie);
        } catch (IOException e) {
            Logger.e("IOException in encodeCookie:" + e);
            return null;
        }
        return byteArrayToHexString(os.toByteArray());
    }

    protected Cookie decodeCookie(String cookieString) {
        byte[] bytes = hexStringToByteArray(cookieString);
        ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(bytes);
        Cookie cookie = null;
        try {
            ObjectInputStream objectInputStream = new ObjectInputStream(byteArrayInputStream);
            cookie = ((SerializableHttpCookie) objectInputStream.readObject()).getCookie();
        } catch (IOException e) {
            Logger.e("IOException in decodeCookie:" + e);
        } catch (ClassNotFoundException e) {
            Logger.e("ClassNotFoundException in decodeCookie:" + e);
        }
        return cookie;
    }

    /**
     * Using some super basic byte array &lt;-&gt; hex conversions so we don't have to rely on any
     * large Base64 libraries. Can be overridden if you like!
     *
     * @param bytes byte array to be converted
     * @return string containing hex values
     */
    protected String byteArrayToHexString(byte[] bytes) {
        StringBuilder sb = new StringBuilder(bytes.length * 2);
        for (byte element : bytes) {
            int v = element & 0xff;
            if (v < 16) {
                sb.append('0');
            }
            sb.append(Integer.toHexString(v));
        }
        return sb.toString().toUpperCase(Locale.US);
    }

    /**
     * Converts hex values from strings to byte arra
     *
     * @param hexString string of hex-encoded values
     * @return decoded byte array
     */
    protected byte[] hexStringToByteArray(String hexString) {
        int len = hexString.length();
        byte[] data = new byte[len / 2];
        for (int i = 0; i < len; i += 2) {
            data[i / 2] = (byte) ((Character.digit(hexString.charAt(i), 16) << 4) + Character
                    .digit(hexString.charAt(i + 1), 16));
        }
        return data;
    }
}
