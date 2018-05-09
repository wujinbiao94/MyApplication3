package com.example.myapplication;

import android.content.Context;

import com.example.myapplication.util.HttpPostUpLoadFile;
import com.example.myapplication.util.PropertiesSovle;

import org.json.JSONObject;
import org.junit.Test;

import java.io.File;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Properties;

import static org.junit.Assert.*;

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
public class ExampleUnitTest{
    @Test
    public void addition_isCorrect() throws Exception {
        assertEquals(4, 2 + 2);
    }

    @Test
    public void uploadFile() throws Exception {
        File file = new File("D:/car/333.jpg");
        String jsonObject =HttpPostUpLoadFile.uploadFile(file, "http://192.168.2.216:8080/upLoadFile");
        System.out.println(jsonObject);
    }

    @Test
    public void log(){

            HttpURLConnection conn = null;

            try {
                // 用户名 密码

                String IP ="192.168.2.216:8080/login";
                // URL 地址
                String path = "http://" + IP;
                path = path + "?username=" + "123" + "&password=" + "2213";
                conn = (HttpURLConnection) new URL(path).openConnection();
                conn.setConnectTimeout(3000); // 设置超时时间
                conn.setReadTimeout(3000);
                conn.setDoInput(true);
                conn.setRequestMethod("GET"); // 设置获取信息方式
                conn.setRequestProperty("Charset", "UTF-8"); // 设置接收数据编码格式
                if (conn.getResponseCode() == 200){
                    System.out.println(222);
                }

            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                // 意外退出时进行连接关闭保护
                if (conn != null) {
                    conn.disconnect();
                }
            }


    }
}