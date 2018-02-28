package com.example.myapplication;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.*;

import com.amap.api.navi.INaviInfoCallback;
import com.amap.api.navi.model.AMapNaviLocation;

public class MainActivity extends AppCompatActivity {

    public WebServiceRequest webServiceRequest;

    // 返回的数据
    private String info;
    //用户名
    TextView userName;
    //密码
     TextView password ;
    //登陆
      Button loginBut ;
    // 创建等待框
    private ProgressDialog dialog;
    // 返回主线程更新数据
    private static Handler handler = new Handler();
     TextView infov;

    public MainActivity() {
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        userName = (TextView) findViewById(R.id.userName);
        password = (TextView) findViewById(R.id.password);
        infov = (TextView) findViewById(R.id.infoV);
        loginBut = (Button) findViewById(R.id.loginButton);
        //按钮点击时间
        loginBut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
               // System.out.println(password.getText());
                new Thread(new RequestThread()).start();
            }
        });
    }

    public class RequestThread implements Runnable {
        public WebServiceRequest webService;

        @Override
        public void run() {
            if ("".equals(userName.getText().toString()) || "".equals(password.getText().toString())) {
                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        infov.setText("用户名或密码为空！");
                    }
                });
            } else {
                info = webService.executeHttpGet(userName.getText().toString(), password.getText().toString());
                // info = WebServicePost.executeHttpPost(username.getText().toString(), password.getText().toString());
                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        //判断跳转

                        Intent intent = new Intent(MainActivity.this, FunctionActivity.class);
                        startActivity(intent);


                        /*if ("true".equals(info)) {
                            Intent intent = new Intent(MainActivity.this, FunctionActivity.class);
                            startActivity(intent);
                        } else {
                            infov.setText(info);
                        }*/
                    }
                });
            }
        }
    }

}
