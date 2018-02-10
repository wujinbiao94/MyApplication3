package com.example.myapplication;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.*;

public class MainActivity extends AppCompatActivity {



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //用户名
        TextView userName = (TextView) findViewById(R.id.userName);
        //密码
        final TextView password = (TextView) findViewById(R.id.password);
        //登陆
        Button loginBut = (Button) findViewById(R.id.loginButton);
        //按钮点击时间
        loginBut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
               // System.out.println(password.getText());
                Toast.makeText(MainActivity.this,"你点击登陆啦",Toast.LENGTH_SHORT).show();
            }
        });
    }
}
