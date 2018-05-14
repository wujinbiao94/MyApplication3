package com.example.myapplication;

import android.app.AlertDialog;
import android.content.SharedPreferences;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.example.myapplication.util.Constons;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;

import java.util.ArrayList;
import java.util.List;

public class PeccancyInsertActivity extends AppCompatActivity {

    TextView license;
    TextView carPlateNum;
    TextView peccancyType;
    TextView peccancyDate;
    TextView peccancyPlace;
    Button submitBtn;
    Button cleanBtn;
    private SharedPreferences sp;
    // 返回主线程更新数据
    private static Handler handler = new Handler();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_peccancy_insert);

        license = (TextView) findViewById(R.id.license);
        carPlateNum = (TextView) findViewById(R.id.carPlateNum);
        peccancyType = (TextView) findViewById(R.id.peccancyType);
        peccancyDate = (TextView) findViewById(R.id.peccancyTime);
        peccancyPlace = (TextView) findViewById(R.id.peccancyPlace);
        submitBtn = (Button) findViewById(R.id.submitBtn);

        submitBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new Thread(new RequestThread()).start();
            }

        });

        cleanBtn = (Button) findViewById(R.id.cleanBtn);
        cleanBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                license.setText("");
                carPlateNum.setText("");
                peccancyType.setText("");
                peccancyDate.setText("");
                peccancyPlace.setText("");
            }

        });
    }

    public class RequestThread implements Runnable {
        @Override
        public void run() {
            List<NameValuePair> list = new ArrayList<>();
            NameValuePair pair  = new BasicNameValuePair("license",license.getText().toString());
            list.add(pair);
            pair = new  BasicNameValuePair("carPlateNum",carPlateNum.getText().toString());
            list.add(pair);
            pair = new  BasicNameValuePair("peccancyType",peccancyType.getText().toString());
            list.add(pair);
            pair = new  BasicNameValuePair("peccancyDate",peccancyDate.getText().toString());
            list.add(pair);
            pair = new  BasicNameValuePair("peccancyPlace",peccancyPlace.getText().toString());
            list.add(pair);
            sp = getSharedPreferences("User", MODE_PRIVATE);
            pair = new BasicNameValuePair("dealPersion",sp.getString("userName",""));
            list.add(pair);
            String json = WebServiceRequest.postRequest(list,"http://"+ Constons.IP+"/"+Constons.peccancyInsert);
            System.out.println("服务器返回的json="+json);
            if (json != null){
                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        license.setText("");
                        carPlateNum.setText("");
                        peccancyType.setText("");
                        peccancyDate.setText("");
                        peccancyPlace.setText("");
                        showExitDialog01();
                    }
                });
            }
        }
    }

    private void showExitDialog01(){
        new AlertDialog.Builder(this)
                .setTitle("提示")
                .setMessage("保存完成！")
                .setPositiveButton("确定", null)
                .show();
    }
}