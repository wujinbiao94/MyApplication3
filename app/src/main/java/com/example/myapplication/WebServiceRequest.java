package com.example.myapplication;

import android.util.Log;

import com.example.myapplication.util.Constons;
import com.example.myapplication.util.PropertiesSovle;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.List;
import java.util.Properties;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
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

    /**
     * post请求
     * @param params 请求参数
     * @param Url 请求url
     * @return 返回字符串
     */
    public static String postRequest(List<NameValuePair> params, String Url){
        String result = "";
        HttpClient httpClient = null;
        System.out.println("请求url="+Url);
        try{
            HttpEntity requestHttpEntity = new UrlEncodedFormEntity(
                    params,"UTF-8");
            // URL使用基本URL即可，其中不需要加参数
            HttpPost httpPost = new HttpPost(Url);
            // 将请求体内容加入请求中
            httpPost.setEntity(requestHttpEntity);
            // 需要客户端对象来发送请求
            httpClient = new DefaultHttpClient();
            // 发送请求
            HttpResponse response = httpClient.execute(httpPost);
            // 显示响应
            if (null == response)
            {
                System.out.println("url="+Url+"请求服务器返回的数据为空！");
                return "";
            }
            //转换返回返回的数据
            HttpEntity httpEntity = response.getEntity();
            try
            {
                InputStream inputStream = httpEntity.getContent();
                BufferedReader reader = new BufferedReader(new InputStreamReader(
                        inputStream));
                String line = "";
                while (null != (line = reader.readLine()))
                {
                    result += line;
                }

                System.out.println("服务器返回的数据="+result);
            }
            catch (Exception e)
            {
                System.out.println("服务器返回数据转换异常");
                e.printStackTrace();
            }

        }catch (Exception e){
            System.out.println("请求服务器失败！");
            e.printStackTrace();
        }
        return result;
    }
}
