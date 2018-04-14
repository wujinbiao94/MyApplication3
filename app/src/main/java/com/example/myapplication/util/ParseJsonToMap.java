package com.example.myapplication.util;

import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

/**
 * Created by 你敬爱的彪哥 on 2018/4/14.
 * 将json转换成list<map>
 */

public class ParseJsonToMap {
    public List<Map<String, Object>> parseJSONObject(String json) {
        List<Map<String, Object>> res = new ArrayList<>();
        Log.i("json转换函数：", "待转换json=" + json);
        try {
            JSONObject jsonObject = new JSONObject(json);
            Map<String, Object> resMap = new HashMap<>();
            Iterator<String> keys = jsonObject.keys();
            while (keys.hasNext()) {
                String key = keys.next();
                Log.i("Json转换：",  key+ "=" + jsonObject.getString(key));
                resMap.put(key, jsonObject.getString(key));
            }
            res.add(resMap);
        } catch (Exception e) {
            Log.e("json转换失败！", "原因：" + e);
        }
        return res;
    }
}
