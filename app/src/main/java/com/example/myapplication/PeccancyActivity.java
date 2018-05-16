package com.example.myapplication;

import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.SoundEffectConstants;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.myapplication.util.Constons;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class PeccancyActivity extends AppCompatActivity {

    ImageButton imageButton;
    LinearLayout linear;
    TextView peccancyInfoSearch;
    ImageButton cleanBtn;
    private static Handler handler = new Handler();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_peccancy);

        imageButton = (ImageButton) findViewById(R.id.imageButton);
        linear = (LinearLayout) findViewById(R.id.layoutPanel);
        peccancyInfoSearch = (TextView) findViewById(R.id.peccancyInfoSearch);
        cleanBtn = (ImageButton) findViewById(R.id.cleanBtn);
        imageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new Thread(new RequestThread()).start();
            }

        });


        cleanBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                linear.removeAllViews();
                peccancyInfoSearch.setText("");
            }

        });


    }

    private class RequestThread implements Runnable {
        @Override
        public void run() {
            List<NameValuePair> list = new ArrayList<>();
            NameValuePair pair  = new BasicNameValuePair("searchParam",peccancyInfoSearch.getText().toString());
            list.add(pair);
            String json = WebServiceRequest.postRequest(list,"http://"+ Constons.IP+"/"+Constons.peccancySearch);
            System.out.println("服务器返回的json="+json);
            try {
               final JSONArray  strings = new JSONArray(json);
                if (json != null){
                    handler.post(new Runnable() {
                        @Override
                        public void run() {
                            for (int i= 0;i<strings.length();i++) {
                                try {
                                    TextView tv = new TextView(getApplicationContext());
                                    tv.setText(strings.get(i).toString());
                                    tv.setTextAlignment(View.TEXT_ALIGNMENT_TEXT_START);
                                    tv.setTextSize(20);
                                    linear.addView(tv);
                                } catch (Exception e){
                                    System.out.println("第"+i+"个数据赋值失败！");
                                }
                            }
                        }
                    });
                }
            } catch (Exception e){
                System.out.println("json转换失败");
            }

        }
    }
}
