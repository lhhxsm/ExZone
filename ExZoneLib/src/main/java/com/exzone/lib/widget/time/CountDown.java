package com.exzone.lib.widget.time;

import android.os.Handler;
import android.widget.TextView;

import com.exzone.lib.util.Logger;

/**
 * 作者:李鸿浩
 * 描述:倒计时
 * 时间: 2016/10/9.
 */
public class CountDown implements Runnable {
    private int mRemainingTime;//剩余时间
    private int mCurrentTime;
    private boolean mRunning;
    private String mDefaultText;
    private String mCountDownText;
    private TextView mTvShow;
    private Handler mHandler;
    private CountDownListener mCountDownListener;
    private TextViewListener mTextViewListener;

    /**
     * 创建一个倒计时器
     *
     * @param tvShow        显示倒计时的文本视图
     * @param countDownText 倒计时中显示的内容,例如："%s秒后重新获取验证码",在倒计时的过程中会用剩余描述替换%s
     * @param remainingTime 倒计时秒数,例如:60,就是从60开始倒计时一直到0结束
     */
    public CountDown(TextView tvShow, String countDownText, int remainingTime) {
        this.mTvShow = tvShow;
        this.mCountDownText = countDownText;
        this.mRemainingTime = remainingTime;
        mHandler = new Handler();
    }

    /**
     * 创建一个倒计时器
     *
     * @param listener      显示倒计时的文本视图监听器
     * @param countDownText 倒计时中显示的内容,例如："%s秒后重新获取验证码",在倒计时的过程中会用剩余描述替换%s
     * @param remainingTime 倒计时秒数,例如:60,就是从60开始倒计时一直到0结束
     */
    public CountDown(TextViewListener listener, String countDownText, int remainingTime) {
        this.mTextViewListener = listener;
        this.mCountDownText = countDownText;
        this.mRemainingTime = remainingTime;
        mHandler = new Handler();
    }

    /**
     * 创建一个倒计时器,默认60秒
     *
     * @param tvShow        显示倒计时的文本视图
     * @param countDownText 倒计时中显示的内容,例如："%s秒后重新获取验证码",在倒计时的过程中会用剩余描述替换%s
     */
    public CountDown(TextView tvShow, String countDownText) {
        this(tvShow, countDownText, 60);
    }

    /**
     * 创建一个倒计时器,默认60秒
     *
     * @param listener      显示倒计时的文本视图监听器
     * @param countDownText 倒计时中显示的内容,例如："%s秒后重新获取验证码",在倒计时的过程中会用剩余描述替换%s
     */
    public CountDown(TextViewListener listener, String countDownText) {
        this(listener, countDownText, 60);
    }

    private TextView getTvShow() {
        if (mTvShow != null) {
            return mTvShow;
        }
        if (mTextViewListener != null) {
            return mTextViewListener.onShow();
        }
        return null;
    }

    @Override
    public void run() {
        if (getTvShow() == null) {
            Logger.e("文本视图参数有误");
            return;
        }
        if (mCurrentTime > 0) {
            getTvShow().setEnabled(false);
            getTvShow().setText(String.format(mCountDownText, mCurrentTime));
            if (mCountDownListener != null) {
                mCountDownListener.onUpdate(mCurrentTime);
            }
            mCurrentTime--;
            mHandler.postDelayed(this, 1000L);
        } else {
            stop();
        }
    }

    public void start() {
        if (getTvShow() == null) {
            Logger.e("文本视图参数有误");
            return;
        }
        mDefaultText = (String) getTvShow().getText();
        mCurrentTime = mRemainingTime;
        mHandler.removeCallbacks(this);
        mHandler.post(this);
        if (mCountDownListener != null) {
            mCountDownListener.onStart();
        }
        mRunning = true;
    }

    public void stop() {
        if (getTvShow() == null) {
            Logger.e("文本视图参数有误");
            return;
        }
        getTvShow().setEnabled(true);
        getTvShow().setText(mDefaultText);
        mHandler.removeCallbacks(this);
        if (mCountDownListener != null) {
            mCountDownListener.onStop();
        }
        mRunning = false;
    }

    public boolean isRunning() {
        return mRunning;
    }

    public int getRemainingTime() {
        return mRemainingTime;
    }

    public String getCountDownText() {
        return mCountDownText;
    }

    public void setCountDownText(String countDownText) {
        this.mCountDownText = countDownText;
    }

    public void setCurrentTime(int currentTime) {
        this.mCurrentTime = currentTime;
    }

    public void setCountDownListener(CountDownListener countDownListener) {
        this.mCountDownListener = countDownListener;
    }

    public interface CountDownListener {
        /**
         * 倒计时开始
         */
        void onStart();

        /**
         * 倒计时结束
         */
        void onStop();

        /**
         * 更新UI
         *
         * @param time 剩余时间
         */
        void onUpdate(int time);
    }

    public interface TextViewListener {
        TextView onShow();
    }
}
