package com.exzone.lib.widget.webview;

import android.annotation.SuppressLint;
import android.webkit.WebSettings;
import android.webkit.WebView;

/**
 * 作者:李鸿浩
 * 描述:WebView管理器，提供常用设置
 * 时间: 2016/10/10.
 */
public class WebViewManager {
    private WebView mWebView;
    private WebSettings mWebSettings;

    public WebViewManager(WebView webView) {
        this.mWebView = webView;
        mWebSettings = mWebView.getSettings();
        mWebSettings.setLayoutAlgorithm(WebSettings.LayoutAlgorithm.NORMAL);
    }

    /**
     * 开启自适应功能
     */
    public WebViewManager enableAdaptive() {
        mWebSettings.setUseWideViewPort(true);
        mWebSettings.setLoadWithOverviewMode(true);
        return this;
    }

    /**
     * 关闭自适应功能
     */
    public WebViewManager disableAdaptive() {
        mWebSettings.setUseWideViewPort(false);
        mWebSettings.setLoadWithOverviewMode(false);
        return this;
    }

    /**
     * 开启缩放功能
     */
    public WebViewManager enableZoom() {
        mWebSettings.setSupportZoom(true);
        mWebSettings.setUseWideViewPort(true);
        mWebSettings.setBuiltInZoomControls(true);
        return this;
    }

    /**
     * 关闭缩放功能
     */
    public WebViewManager disableZoom() {
        mWebSettings.setSupportZoom(false);
        mWebSettings.setUseWideViewPort(false);
        mWebSettings.setBuiltInZoomControls(false);
        return this;
    }

    /**
     * 开启JavaScript
     */
    @SuppressLint("SetJavaScriptEnabled")
    public WebViewManager enableJavaScript() {
        mWebSettings.setJavaScriptEnabled(true);
        return this;
    }

    /**
     * 禁用JavaScript
     */
    public WebViewManager disableJavaScript() {
        mWebSettings.setJavaScriptEnabled(false);
        return this;
    }

    /**
     * 开启JavaScript自动弹窗
     */
    public WebViewManager enableJavaScriptOpenWindowsAutomatically() {
        mWebSettings.setJavaScriptCanOpenWindowsAutomatically(true);
        return this;
    }

    /**
     * 禁用JavaScript自动弹窗
     */
    public WebViewManager disableJavaScriptOpenWindowsAutomatically() {
        mWebSettings.setJavaScriptCanOpenWindowsAutomatically(false);
        return this;
    }

    /**
     * 返回
     *
     * @return true：已经返回，false：到头了没法返回了
     */
    public boolean goBack() {
        if (mWebView.canGoBack()) {
            mWebView.goBack();
            return true;
        } else {
            return false;
        }
    }
}
