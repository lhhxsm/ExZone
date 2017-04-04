package com.exzone.demo;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.exzone.lib.view.panel.SlideBottomPanel;

import java.util.ArrayList;

/**
 * 作者:李鸿浩
 * 描述:
 * 时间:2017/4/4.
 */
public class SlideBottomPanelActivity extends AppCompatActivity {

    private ArrayList<String> list = new ArrayList<>();
    private SlideBottomPanel sbv;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_slide_bottom_panel);

        sbv = (SlideBottomPanel) findViewById(R.id.sbv);
        ListView listView = (ListView) findViewById(R.id.list_view);
        listView.setAdapter(new ArrayAdapter<>(getApplicationContext(), R.layout.list_item, getData()));
    }

    private ArrayList<String> getData() {
        for (int i = 0; i < 20; i++) {
            list.add("Item " + i);
        }
        return list;
    }

    @Override
    public void onBackPressed() {
        if (sbv.isPanelShowing()) {
            sbv.hide();
            return;
        }
        super.onBackPressed();
    }
}
