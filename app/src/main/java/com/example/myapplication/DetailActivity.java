package com.example.myapplication;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.GridView;
import android.widget.SimpleAdapter;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class DetailActivity extends AppCompatActivity {

    TextView carNum1;
    TextView region;
    TextView driver;
    TextView driverNum;
    TextView carInfo;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);
        Intent intent = getIntent();
        List<Map<String,Object>> result = (List<Map<String,Object>>)  intent.getSerializableExtra("data");
        Map<String,Object> map = result.get(0);

        carNum1 = (TextView) findViewById(R.id.carNum1);
        region = (TextView) findViewById(R.id.region);
        driver = (TextView) findViewById(R.id.driver);
        driverNum = (TextView) findViewById(R.id.driverNum);
        carInfo = (TextView) findViewById(R.id.carInfo);

        carNum1.setText(map.get("plate_number").toString());
        carInfo.setText(map.get("car_brand").toString());
        driver.setText(map.get("driver_name").toString());
        driverNum.setText(map.get("driver_license_id").toString());
        region.setText(map.get("region_name").toString());

    }
}
