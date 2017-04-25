package com.exzone.demo;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

/**
 * 作者:李鸿浩
 * 描述:
 * 时间:2017/4/4.
 */
public class StickyActivity extends AppCompatActivity {

  @Override protected void onCreate(@Nullable Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_sticky);
    Button mButton = (Button) findViewById(R.id.button);
    mButton.setOnClickListener(new View.OnClickListener() {

      @Override public void onClick(View v) {
        Toast.makeText(StickyActivity.this, "clicked !", Toast.LENGTH_SHORT).show();
      }
    });
  }

  @Override protected void onStart() {
    super.onStart();
  }

  @Override protected void onDestroy() {
    super.onDestroy();
  }
}
