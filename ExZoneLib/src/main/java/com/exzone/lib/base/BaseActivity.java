package com.exzone.lib.base;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;

import com.exzone.lib.R;
import com.exzone.lib.manager.ActManager;
import com.exzone.lib.util.ToastUtils;

import java.util.concurrent.TimeUnit;

import rx.Observable;
import rx.functions.Action1;


/**
 * 作者:李鸿浩
 * 描述:
 * 时间:2016/7/5.
 */
public abstract class BaseActivity extends AppCompatActivity {

    private ProgressDialog mProgressDialog;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ActManager.getInstance().addActivity(this);
        this.setContentView(layoutResID());
        this.initView();
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        ActManager.getInstance().finishActivity(this);
    }

    @Override
    public void finish() {
        super.finish();
        overridePendingTransition(R.anim.anim_slide_in_left, R.anim.anim_slide_out_right);
    }

    private boolean couldDoubleBackExit;
    private boolean doubleBackExitPressedOnce;

    /**
     * 设置是否可以双击返回退出，需要有该功能的页面set true即可
     *
     * @param couldDoubleBackExit true-开启双击退出
     */
    public void setCouldDoubleBackExit(boolean couldDoubleBackExit) {
        this.couldDoubleBackExit = couldDoubleBackExit;
    }

    @Override
    public void onBackPressed() {
        if (!couldDoubleBackExit) {
            // 非双击退出状态，使用原back逻辑
            super.onBackPressed();
            return;
        }

        // 双击返回键关闭程序
        // 如果两秒重置时间内再次点击返回,则退出程序
        if (doubleBackExitPressedOnce) {
            ActManager.getInstance().exit(this);
            return;
        }

        doubleBackExitPressedOnce = true;
        ToastUtils.showShort(this, this.getResources().getString(R.string.press_the_key_again_to_close_the_program));
        Observable.just(null)
                .delay(2, TimeUnit.SECONDS)
                .subscribe(new Action1<Object>() {
                    @Override
                    public void call(Object o) {
                        // 延迟两秒后重置标志位为false
                        doubleBackExitPressedOnce = false;
                    }
                });
    }


    public void showProgressDialog(String message) {
        if (mProgressDialog == null) {
            mProgressDialog = new ProgressDialog(this);
            mProgressDialog.setMessage(message);
            mProgressDialog.setCanceledOnTouchOutside(false);
        }
        mProgressDialog.show();
    }

    public void hideProgressDialog() {
        if (mProgressDialog != null && mProgressDialog.isShowing())
            mProgressDialog.dismiss();
    }

    public void finishActivity() {
        ActManager.getInstance().finishActivity(this);
        finish();
    }

    protected abstract int layoutResID();

    protected abstract void initView();
}
