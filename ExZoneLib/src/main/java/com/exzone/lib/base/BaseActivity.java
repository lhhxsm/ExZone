package com.exzone.lib.base;

import android.app.ProgressDialog;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;

import com.exzone.lib.util.Logger;

import butterknife.ButterKnife;

import static android.R.attr.name;

/**
 * 作者:李鸿浩
 * 描述:
 * 时间:2016/7/5.
 */
public abstract class BaseActivity extends AppCompatActivity {
    private ProgressDialog progressDialog;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(layoutResID());
        ButterKnife.bind(this);
        setupActivityComponent();
        init(savedInstanceState);
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
//        ButterKnife.unbind(this);
    }

    protected void showProgressDialog(String message) {
        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage(message);
        progressDialog.show();
    }

    protected void dismissProgressDialog() {
        if (progressDialog != null && progressDialog.isShowing()) {
            progressDialog.dismiss();
        }
    }

    protected abstract int layoutResID();

    protected abstract void init(Bundle savedInstanceState);

    protected abstract void setupActivityComponent();

//    private final String ACTION_SERVICE = getPackageName() + "." + getClass().getName();
//
//    protected void startService() {
//        //开启服务
//        Intent intent = new Intent();
//        intent.setAction(ACTION_SERVICE);
//        intent.setPackage(getPackageName());//从Lollipop(API 5.0)开始,service服务必须采用显示方式启动。
//        bindService(intent, mServiceConnection, Context.BIND_AUTO_CREATE);
//    }
//
//    protected void stopService() {
//        Intent intent = new Intent();
//        intent.setAction(ACTION_SERVICE);
//        unbindService(mServiceConnection);
//    }
//
//    private ServiceConnection mServiceConnection = new ServiceConnection() {
//
//        @Override
//        public void onServiceDisconnected(ComponentName name) {
//
//        }
//
//        @Override
//        public void onServiceConnected(ComponentName name, IBinder service) {
//
//        }
//    };
}
