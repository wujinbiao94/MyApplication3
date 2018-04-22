package com.example.myapplication;

import android.util.Log;

import com.example.myapplication.util.Constons;
import com.example.myapplication.util.PropertiesSovle;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Properties;

/**
 * Created by 你敬爱的彪哥 on 2018/2/11.
 */

public class WebServiceRequest  {

    //登陆服务器请求ip请将ip更改为本机的ip，服务器代码详见portal，没有权限请联系476887261@qq.com申请
    private PropertiesSovle propertiesSovle = new PropertiesSovle();

    // 通过Get方式获取HTTP服务器数据
    public static String executeHttpGet(InputStream in, String username, String password) {
        Log.i("服务器请求","WebServiceRequest.executeHttpGet请求开始");
        HttpURLConnection conn = null;
        InputStream is = null;

        try {
            // 用户名 密码
            Properties pro=PropertiesSovle.getProperties(in);
            String IP = Constons.IP + ":8080/login";//pro.getProperty("LOGINURL");
            // URL 地址
            String path = "http://" + IP;
            path = path + "?username=" + username + "&password=" + password;
            Log.i("url",path);
            conn = (HttpURLConnection) new URL(path).openConnection();
           // conn.setConnectTimeout(3000); // 设置超时时间
           // conn.setReadTimeout(3000);
            conn.setDoInput(true);
            conn.setRequestMethod("GET"); // 设置获取信息方式
            conn.setRequestProperty("Charset", "UTF-8"); // 设置接收数据编码格式

            if (conn.getResponseCode() == 200) {
                is = conn.getInputStream();
                return parseInfo(is);
            }

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            // 意外退出时进行连接关闭保护
            if (conn != null) {
                conn.disconnect();
            }
            if (is != null) {
                try {
                    is.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

        }
        return null;
    }

    // 将输入流转化为 String 型
    public static String parseInfo(InputStream inStream) throws Exception {
        byte[] data = read(inStream);
        // 转化为字符串
        return new String(data, "UTF-8");
    }

    // 将输入流转化为byte型
    public static byte[] read(InputStream inStream) throws Exception {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        byte[] buffer = new byte[1024];
        int len = 0;
        while ((len = inStream.read(buffer)) != -1) {
            outputStream.write(buffer, 0, len);
        }
        inStream.close();
        return outputStream.toByteArray();

    }
}
