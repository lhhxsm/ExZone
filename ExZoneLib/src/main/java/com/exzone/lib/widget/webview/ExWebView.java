package com.exzone.lib.widget.webview;

import android.annotation.SuppressLint;
import android.content.Context;
import android.util.AttributeSet;
import android.webkit.WebSettings;
import android.webkit.WebView;
import com.exzone.lib.util.NetWorkUtils;

/**
 * 作者:李鸿浩
 * 描述:封装一些常用的方法的WebView
 * 时间:2016/10/6.
 */
public class ExWebView extends WebView {

  private boolean mZoom = false;

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

  @SuppressLint({ "NewApi", "SetJavaScriptEnabled" }) private void init(Context context) {
    WebSettings settings = getSettings();
    if (NetWorkUtils.isConnected(context)) {
      settings.setCacheMode(WebSettings.LOAD_DEFAULT);
    } else {
      settings.setCacheMode(WebSettings.LOAD_CACHE_ELSE_NETWORK);
    }
    //设置是否支持Javascript
    settings.setJavaScriptEnabled(true);
    //是否支持缩放
    settings.setSupportZoom(mZoom);
    //是否显示缩放按钮
    //settings.setDisplayZoomControls(false);
    //提高渲染优先级
    settings.setRenderPriority(WebSettings.RenderPriority.HIGH);
    //设置页面自适应手机屏幕
    settings.setUseWideViewPort(true);
    //WebView自适应屏幕大小
    settings.setLoadWithOverviewMode(true);
    //加载url前，设置不加载图片WebViewClient-->onPageFinished加载图片
    //settings.setBlockNetworkImage(true);
    //设置网页编码
    settings.setDefaultTextEncodingName("UTF-8");
  }

  /**
   * 是否支持缩放 默认false
   */
  public void setZoom(boolean zoom) {
    this.mZoom = zoom;
  }
}
