package com.exzone.demo;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

import com.exzone.lib.util.Logger;
import com.exzone.lib.util.PropertiesUtils;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        String value = PropertiesUtils.readAssetsProp("config.properties", "OUTPUT_FILE");
        Logger.e(value);
    }
}
