package com.exzone.demo;

import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.exzone.lib.view.OutlineTextView;
/**
 * 作者:李鸿浩
 * 描述:
 * 时间：2016/10/7.
 */
public class MainActivity extends AppCompatActivity {

    private OutlineTextView mOutlineTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mOutlineTextView = (OutlineTextView) findViewById(R.id.text);
        OutlineTextView textView = (OutlineTextView) findViewById(R.id.text0);
        mOutlineTextView.setShadowLayer(0, 5, 5, Color.BLACK);
        textView.setShadowLayer(0, 0, 0, Color.BLACK);
    }
}
