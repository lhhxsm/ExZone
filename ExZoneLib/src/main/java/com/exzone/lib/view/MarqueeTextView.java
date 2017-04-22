package com.exzone.lib.view;

import android.content.Context;
import android.support.v7.widget.AppCompatTextView;
import android.text.TextUtils;
import android.util.AttributeSet;

/**
 * 作者:李鸿浩
 * 描述:一直跑马灯的textView
 * 时间：2016/10/8.
 */
public class MarqueeTextView extends AppCompatTextView {
    public MarqueeTextView(Context context) {
        super(context);
        setEllipsize(TextUtils.TruncateAt.MARQUEE);
    }

    public MarqueeTextView(Context context, AttributeSet attrs) {
        super(context, attrs);
        setEllipsize(TextUtils.TruncateAt.MARQUEE);
    }

    @Override
    public boolean isFocused() {
        return true;
    }
}
