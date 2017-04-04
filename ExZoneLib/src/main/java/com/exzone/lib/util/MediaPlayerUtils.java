package com.exzone.lib.util;

import android.media.MediaPlayer;

import java.io.IOException;

/**
 * 作者:李鸿浩
 * 描述:音视频播放工具类
 * 时间:2017/4/3.
 */
public class MediaPlayerUtils {
    private boolean mPlayState;
    private MediaPlayer mMediaPlayer;
    private PlayVoiceListener mPlayVoiceListener;
    //    private String mRecordPath = "/sdcard/KaiXin/Record/1f38c3cd-f4be-4113-a91c-6ae191d83957.amr";
    private String mRecordPath;

    public void setRecordPath(String path) {
        mRecordPath = path;
    }

    public void setPlayVoiceListener(PlayVoiceListener mPlayVoiceListener) {
        this.mPlayVoiceListener = mPlayVoiceListener;
        playVoice();
    }

    public int getDuration() {
        if (mMediaPlayer != null)
            return mMediaPlayer.getDuration();
        return 100;
    }

    public int getCurrentPosition() {
        if (mMediaPlayer != null)
            return mMediaPlayer.getCurrentPosition();
        return 0;
    }

    public void stopVoice() {
        if (mMediaPlayer != null) {
            if (mMediaPlayer.isPlaying()) {
                mPlayState = false;
                mMediaPlayer.stop();
                mPlayVoiceListener.stopRecord();
            } else {
                mPlayState = false;
                mPlayVoiceListener.stopRecord();
            }
        }
    }

    public void playVoice() {
        if (!mPlayState) {
            mMediaPlayer = new MediaPlayer();
            try {
                mMediaPlayer.setDataSource(mRecordPath);
                mMediaPlayer.prepare();
                mMediaPlayer.start();
                mPlayVoiceListener.startRecord();
                mPlayState = true;
                mMediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                    public void onCompletion(MediaPlayer mp) {
                        mMediaPlayer.stop();
                        mPlayState = false;
                        mPlayVoiceListener.stopRecord();
                    }
                });
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else {
            if (mMediaPlayer != null) {
                if (mMediaPlayer.isPlaying()) {
                    mPlayState = false;
                    mMediaPlayer.stop();
                    mPlayVoiceListener.stopRecord();
                } else {
                    mPlayState = false;
                    mPlayVoiceListener.stopRecord();
                }
            }
        }
    }

    public interface PlayVoiceListener {
        void startRecord();

        void stopRecord();
    }

}
