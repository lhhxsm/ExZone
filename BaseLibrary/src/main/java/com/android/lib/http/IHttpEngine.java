package com.android.lib.http;

import android.content.Context;
import java.util.Map;

/**
 * 作者:李鸿浩
 * 描述:引擎的规范
 * 时间:2017/5/21.
 */
public interface IHttpEngine {
  // get请求
  void get(boolean cache, Context context, String url, Map<String, Object> params,
      EngineCallBack callBack);

  // post请求
  void post(boolean cache, Context context, String url, Map<String, Object> params,
      EngineCallBack callBack);

  // 下载文件

  // 上传文件

  // https 添加证书
}
