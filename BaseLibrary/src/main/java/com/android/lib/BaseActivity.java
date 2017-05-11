package com.android.lib;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.IdRes;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import com.android.lib.ioc.ViewUtils;

/**
 * 作者:lihonghao
 * 描述:
 * 时间:2017/5/11.
 */
public abstract class BaseActivity extends AppCompatActivity {

  @Override protected void onCreate(@Nullable Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    //设置布局Layout
    setContentView(layoutResID());
    // 一些特定的算法，子类基本都会使用的
    ViewUtils.inject(this);
    //初始化头部
    initTitle();
    //初始化界面
    initView();
    //初始化数据
    initData();
  }

  protected abstract int layoutResID();

  protected abstract void initTitle();

  protected abstract void initView();

  protected abstract void initData();

  /**
   * 通过Class跳转界面
   **/
  protected void startActivity(Class<?> clazz) {
    startActivity(clazz, null);
  }

  /**
   * 含有Bundle通过Class跳转界面
   **/
  protected void startActivity(Class<?> clazz, Bundle bundle) {
    Intent intent = new Intent();
    intent.setClass(this, clazz);
    if (bundle != null) {
      intent.putExtras(bundle);
    }
    startActivity(intent);
  }

  /**
   * 通过Class跳转界面
   **/
  protected void startActivityForResult(Class<?> clazz, int requestCode) {
    startActivityForResult(clazz, null, requestCode);
  }

  /**
   * 含有Bundle通过Class跳转界面
   **/
  protected void startActivityForResult(Class<?> clazz, Bundle bundle, int requestCode) {
    Intent intent = new Intent();
    intent.setClass(this, clazz);
    if (bundle != null) {
      intent.putExtras(bundle);
    }
    startActivityForResult(intent, requestCode);
  }

  protected <T extends View> T viewById(@IdRes int viewId) {
    return (T) findViewById(viewId);
  }
}
