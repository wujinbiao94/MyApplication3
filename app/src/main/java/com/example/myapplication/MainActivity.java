package com.example.myapplication;

import android.app.Application;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.*;

import com.amap.api.navi.INaviInfoCallback;
import com.amap.api.navi.model.AMapNaviLocation;

import java.io.InputStream;

public class MainActivity extends AppCompatActivity {

    public WebServiceRequest webServiceRequest;

    // 返回的数据
    private String info;
    private SharedPreferences sp;
    private WebServiceRequest webService;
    //配置文件输入流
    InputStream in = getResources().openRawResource(R.raw.weburl);
    //用户名
    TextView userName;
    //请求服务器
    private String userNameStr;
    //密码
     TextView password ;
     //请求服务器
     private String passwordStr;
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

        //获取登陆状态避免重复登陆
        sp = getSharedPreferences("User", MODE_PRIVATE);

        userNameStr = sp.getString("userName", null);
        passwordStr = sp.getString("password", null);
        if (userNameStr != null && passwordStr != null) {
            Log.i("No.0","保存了用户登录状态！");
            //自动登陆
            userName.setText(userNameStr);
            password.setText(passwordStr);
            System.out.println("用户名"+userName.getText().toString()+"密码"+password.getText().toString());
            new Thread(new RequestThread()).start();
        }
        //登录按钮点击
        loginBut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.i("No.0","按钮点击登陆");
                // 登录请求服务器
                new Thread(new RequestThread()).start();
            }
        });

    }

    public class RequestThread implements Runnable {
        @Override
        public void run() {
            Log.i("No.1","判断用户名密码是否为空！");
            if ("".equals(userName.getText().toString()) || "".equals(password.getText().toString())) {
                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        infov.setText("用户名或密码为空！");
                    }
                });
            } else {
                Log.i("No.2 ","请求服务器");
                try {
                    info = webService.executeHttpGet(in,userName.getText().toString(), password.getText().toString());

                    if (info == null){
                        Log.e("No.3","服务器返回为空！");
                        return;
                    }
                    Log.i("请求参数", userName.getText().toString() + " " + password.getText().toString());
                    handler.post(new Runnable() {
                        @Override
                        public void run() {
                            if ("true".equals(info)) {
                                Log.i("No.3 ", "密码验证通过");
                                Intent intent = new Intent(MainActivity.this, FunctionActivity.class);
                                startActivity(intent);
                                //首次登陆成功的保存到本地数据库
                                String localUserName = sp.getString("userName", null);
                                String localPassword = sp.getString("pasword", null);
                                if (localPassword == null || localUserName == null) {
                                    SharedPreferences.Editor editor = sp.edit(); //SharedPreferences 本身不能读写数据，需要使用Editor
                                    editor.putString("userName", userName.getText().toString());
                                    editor.putString("password", password.getText().toString());
                                    editor.commit(); //提交
                                }
                            } else {
                                Log.i("No.3 ", "密码验证失败! 原因：" + info);
                                infov.setText(info);
                            }
                        }
                    });
                } catch (Exception e){
                    Log.e("服务器请求错误","",e);
                }
            }
        }
    }

}
