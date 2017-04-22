package com.exzone.demo;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.exzone.lib.view.largeimage.LargeImageView;

import java.io.IOException;
import java.io.InputStream;

/**
 * 作者:李鸿浩
 * 描述:
 * 时间：2016/10/7.
 */
public class LargeImageViewActivity extends AppCompatActivity {
    private LargeImageView mLargeImageView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_large_image_view);

        mLargeImageView = (LargeImageView) findViewById(R.id.id_largetImageview);
        try {
            InputStream inputStream = getAssets().open("qm.jpg");
            mLargeImageView.setInputStream(inputStream);

        } catch (IOException e) {
            e.printStackTrace();
        }


    }

}
