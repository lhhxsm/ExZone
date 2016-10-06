package com.exzone.lib.view;

import android.content.Context;
import android.util.AttributeSet;
import android.webkit.WebSettings;
import android.webkit.WebView;

import com.exzone.lib.util.NetUtils;

/**
 * 作者:李鸿浩
 * 描述:
 * 时间:2016/10/6.
 */
public class ExWebView extends WebView {

    public ExWebView(Context context) {
        super(context);
        init(context);
    }

    public ExWebView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public ExWebView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    private void init(Context context) {
        WebSettings webSettings = getSettings();
        if (NetUtils.isConnected(context)) {
            webSettings.setCacheMode(WebSettings.LOAD_DEFAULT);
        } else {
            webSettings.setCacheMode(WebSettings.LOAD_CACHE_ELSE_NETWORK);
        }
        webSettings.setJavaScriptEnabled(true);
    }
}
