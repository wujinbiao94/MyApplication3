package com.example.myapplication;

import android.content.Intent;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.example.myapplication.util.Constons;
import com.example.myapplication.util.ParseJsonToMap;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class LicenseActivity extends AppCompatActivity {

    TextView driverLicense;
    Button licenseBtn;
    TextView driverName;
    TextView phone;
    TextView adress;
    TextView information;
    TextView age;
    TextView sex;
    Button cleanBtn;
    private static Handler handler = new Handler();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_license);
        driverLicense = (TextView) findViewById(R.id.driverLicense);
        licenseBtn = (Button) findViewById(R.id.licenseBut);
        driverName = (TextView) findViewById(R.id.driverName);
        phone = (TextView) findViewById(R.id.phone);
        adress = (TextView) findViewById(R.id.address);
        information = (TextView) findViewById(R.id.information);
        age = (TextView) findViewById(R.id.age);
        sex = (TextView) findViewById(R.id.sex);
        licenseBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new Thread(new requestByDriverLicenseThread()).start();
            }
        });

        cleanBtn = (Button) findViewById(R.id.cleanBtn);
        cleanBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                driverLicense.setText("");
                driverName.setText("");
                phone.setText("");
                adress.setText("");
                information.setText("");
                age.setText("");
                sex.setText("");
            }

        });
    }

    public class requestByDriverLicenseThread implements Runnable {
        @Override
        public void run() {
            String driverLicenseStr = driverLicense.getText().toString();
            NameValuePair pair  = new BasicNameValuePair("driverLisence",driverLicenseStr);
            List<NameValuePair> list = new ArrayList<>();
            list.add(pair);
            String json = WebServiceRequest.postRequest(list,"http://"+ Constons.IP+"/"+Constons.driverLicenseSearch);
            System.out.println("服务器返回的json="+json);
            final List<Map<String, Object>> result = ParseJsonToMap.parseJSONObject(json);
            handler.post(new Runnable() {
                @Override
                public void run() {
                    if (result.size() == 0){
                        System.out.println("未查询到数据");
                        return;
                    }
                    Map<String,Object> map = result.get(0);
                    driverName.setText(map.get("driver_name").toString());
                    phone.setText(map.get("phone").toString());
                    adress.setText(map.get("region_name").toString());
                    information.setText(map.get("peccancyInfo").toString().replaceAll(",","\n"));
                    age.setText(map.get("age").toString());
                    sex.setText(map.get("sex").toString());
                }
            });
        }
    }

}
