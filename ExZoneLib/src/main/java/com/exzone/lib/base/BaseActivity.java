package com.exzone.lib.base;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;

import com.exzone.lib.R;

import butterknife.ButterKnife;


/**
 * 作者:李鸿浩
 * 描述:
 * 时间:2016/7/5.
 */
public abstract class BaseActivity extends AppCompatActivity {

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


    public void finishActivity() {
        ActManager.getInstance().finishActivity(this);
        finish();
    }

    protected abstract int layoutResID();

    protected abstract void initView();
}
