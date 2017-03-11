package com.exzone.lib.view;

import android.content.Context;
import android.graphics.drawable.Animatable;
import android.util.AttributeSet;
import android.view.View;
import android.widget.ImageView;

/**
 * 作者:李鸿浩
 * 描述:
 * 时间: 2017/2/8.
 */

public class ProgressImageView extends ImageView {

    public ProgressImageView(Context context) {
        super(context);
    }

    public ProgressImageView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public ProgressImageView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public void start() {
        setVisibility(View.VISIBLE);
        Animatable animatable = (Animatable) getDrawable();
        if (!animatable.isRunning()) {
            animatable.start();
        }
    }

    public void stop() {
        Animatable animatable = (Animatable) getDrawable();
        if (animatable.isRunning()) {
            animatable.stop();
        }
        setVisibility(View.GONE);
    }
}
