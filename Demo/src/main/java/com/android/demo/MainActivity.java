package com.android.demo;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;
import com.android.lib.ioc.CheckNet;
import com.android.lib.ioc.OnClick;
import com.android.lib.ioc.ViewById;
import com.android.lib.ioc.ViewUtils;

public class MainActivity extends AppCompatActivity {

  @ViewById(R.id.tv_hello) private TextView mTvHello;

  @Override protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_main);
    ViewUtils.inject(this);
    mTvHello.setText("IOC");
  }

  @OnClick(R.id.tv_hello) @CheckNet private void onClick(View view) {
    Toast.makeText(this, "test", Toast.LENGTH_SHORT).show();
  }
}
