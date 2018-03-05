package com.example.myapplication.util;

import android.content.Context;
import android.util.Log;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.util.Properties;

/**
 * Created by 你敬爱的彪哥 on 2018/3/4.
 */

public class PropertiesSovle {

    private static Context context ;

    /**
     * 解析properties文件
     * @param in 文件的输入流
     * @return
     */
    public static Properties getProperties(InputStream in){
        Log.i("getProperties","解析配置文件");
        Properties pro=new Properties();
        try {
            pro.load(in);
        } catch (Exception e){
            Log.e("PropertiesSovle.24","配置文件解析错误",e);
        }
        return pro;
    }
}
