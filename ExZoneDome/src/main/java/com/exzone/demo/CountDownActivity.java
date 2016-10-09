package com.exzone.demo;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.TextView;

import com.exzone.lib.widget.time.CountDown;

/**
 * 倒计时Demo
 */
public class CountDownActivity extends AppCompatActivity implements View.OnClickListener {
    private CountDown mCountDown1;
    private CountDown mCountDown2;
    private CountDown mCountDown3;
    private CountDown mCountDown4;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        TextView mTextView1 = (TextView) findViewById(R.id.text1);
        final TextView mTextView2 = (TextView) findViewById(R.id.text2);
        TextView mTextView3 = (TextView) findViewById(R.id.text3);
        final TextView mTextView4 = (TextView) findViewById(R.id.text4);
        mTextView1.setOnClickListener(this);
        mTextView2.setOnClickListener(this);
        mTextView3.setOnClickListener(this);
        mTextView4.setOnClickListener(this);
        mCountDown1 = new CountDown(mTextView1, "%s秒后重新获取验证码", 10);
        mCountDown4 = new CountDown(new CountDown.TextViewListener() {
            @Override
            public TextView onShow() {
                return mTextView2;
            }
        }, "%s秒后重新获取验证码", 10);
        mCountDown3 = new CountDown(mTextView3, "%s秒后重新获取验证码");
        mCountDown4 = new CountDown(new CountDown.TextViewListener() {
            @Override
            public TextView onShow() {
                return mTextView4;
            }
        }, "%s秒后重新获取验证码");
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.text1:
                mCountDown1.start();
                break;
            case R.id.text2:
                mCountDown2.start();
                break;
            case R.id.text3:
                mCountDown3.start();
                break;
            case R.id.text4:
                mCountDown4.start();
                break;
        }
    }
}
