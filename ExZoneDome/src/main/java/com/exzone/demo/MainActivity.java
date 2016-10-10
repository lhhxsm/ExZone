package com.exzone.demo;

import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;

import com.exzone.lib.util.ColorUtils;
import com.exzone.lib.util.NetWorkUtils;
import com.exzone.lib.view.OutlineTextView;

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
