package com.example.myapplication;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.GridView;
import android.widget.SimpleAdapter;

import java.util.ArrayList;
import java.util.HashMap;

public class DetailActivity extends AppCompatActivity {

    private GridView gridView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);

        /*gridView = (GridView) findViewById(R.id.gridView);
        String[] image = {"1","2","3"};
        String[] text = {"第一列","第二列","第三列"};
        ArrayList<HashMap<String, Object>> imagelist = new ArrayList<HashMap<String, Object>>();
        for (int i = 0; i < 3; i++) {
            HashMap<String, Object> map = new HashMap<String, Object>();
            map.put("image", image[i]);
            map.put("text", text[i]);
            imagelist.add(map);
        }

        SimpleAdapter simpleAdapter = new SimpleAdapter(this, imagelist,
                R.layout.items, new String[] { "image", "text" }, new int[] {
                R.id.image, R.id.title });
        // 设置GridView的适配器为新建的simpleAdapter
        gridView.setAdapter(simpleAdapter);*/
    }
}
