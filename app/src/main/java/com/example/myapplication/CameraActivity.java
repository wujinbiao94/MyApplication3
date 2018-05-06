package com.example.myapplication;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.ThumbnailUtils;
import android.net.Uri;
import android.os.Environment;
import android.os.StrictMode;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.myapplication.util.Constons;
import com.example.myapplication.util.HttpPostUpLoadFile;
import com.example.myapplication.util.ParseJsonToMap;
import com.example.myapplication.util.PropertiesSovle;
import com.iflytek.cloud.thirdparty.S;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.logging.Logger;

/**
 * 车辆查询页面
 */
public class CameraActivity extends AppCompatActivity {

    //车牌号输入框
    TextView textView;
    //拍照按钮
    ImageButton cameraBtn;
    //搜索按钮
    ImageButton caeraSearch;
    //定义一个保存图片的File变量
    private File currentImageFile = null;
    ImageView img_show;
    //配置文件输入流
    InputStream in=null;
    //等待窗口
    ProgressDialog waitingDialog;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_camera);
        in = getResources().openRawResource(R.raw.weburl);
        cameraBtn = (ImageButton) findViewById(R.id.camera_photo);
        img_show = (ImageView) findViewById(R.id.img_show);
        textView = (TextView) findViewById(R.id.camera_textView);
        caeraSearch = (ImageButton) findViewById(R.id.camera_search);
        waitingDialog = new ProgressDialog(CameraActivity.this);
        cameraBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                File dir = new File(Environment.getExternalStorageDirectory(),"/plateNumberPic");
                if(dir.exists()){
                    dir.mkdirs();
                }
                currentImageFile = new File(dir,System.currentTimeMillis() + ".jpg");
                Log.i("照片路径",currentImageFile.getAbsolutePath());
                Log.i("照片名称",currentImageFile.getName());
                if(!currentImageFile.exists()){
                    try {
                       Boolean flag = currentImageFile.createNewFile();
                       Log.i("文件创建结果",flag.toString());
                    } catch (IOException e) {
                        Log.e("文件创建失败！"," ");
                        e.printStackTrace();
                    }
                }

                StrictMode.VmPolicy.Builder builder = new StrictMode.VmPolicy.Builder();
                StrictMode.setVmPolicy(builder.build());
                builder.detectFileUriExposure();

                Intent it = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                it.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(currentImageFile));
                startActivityForResult(it, Activity.DEFAULT_KEYS_DIALER);



            }

        });

        caeraSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(CameraActivity.this, DetailActivity.class);
                startActivity(intent);
            }
        });
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == Activity.DEFAULT_KEYS_DIALER) {
            String sdStatus = Environment.getExternalStorageState();
            if (!sdStatus.equals(Environment.MEDIA_MOUNTED)) { // 检测sd是否可用
                Log.i("TestFile", "SD card is not avaiable/writeable right now.");
                return;
            }
            //原图
            String filePath = currentImageFile.getAbsolutePath();
            Log.i("获取路径",filePath);
            File file = new File(filePath);
            Properties pro= PropertiesSovle.getProperties(in);
            //IP地址
            String URL = "http://"+ Constons.IP +":8080/upLoadFile";
            Log.i("车牌请求url=",URL);
            Bitmap bitmap = BitmapFactory.decodeFile(filePath);
            //利用Bitmap对象创建缩略图
            Bitmap showbitmap = ThumbnailUtils.extractThumbnail(bitmap, 250, 250);
            img_show.setImageBitmap(showbitmap);
            showWaitingDialog();
            new Thread(new CarRequestThread(file, URL,textView)).start();

        }
    }

    /**
     * http请求方法查询车牌信息
     */
    public class CarRequestThread implements Runnable {
        File file;
        String url;
        TextView textView;
        String carP;
        public CarRequestThread(File file,String url,TextView textView){
            this.file=file;
            this.url = url;
            this.textView = textView;
        }
        @Override
        public void run() {
            String json = "";
            try {
                json = HttpPostUpLoadFile.uploadFile(file, url);
                ParseJsonToMap parseJsonToMap = new ParseJsonToMap();
                List<Map<String,Object>> jsonMapList = parseJsonToMap.parseJSONObject(json);

                Map<String,Object> map = jsonMapList.get(0);
                if (map.containsKey("error_msg")){
                    carP = "识别失败！请重新拍照！";
                } else {
                    String wordsResult = map.get("words_result").toString();
                    List<Map<String, Object>> car = parseJsonToMap.parseJSONObject(wordsResult);
                    Log.i("车牌=", "" + car.get(0).get("number"));
                    carP = car.get(0).get("number").toString();
                }
            } catch (Exception e){
                Log.e("车牌查询请求服务器异常！","原因："+e);
                e.printStackTrace();
            }
            Log.i("返回的车牌信息",json);

            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                   //更新ui线程
                    textView.setText(carP);
                    waitingDialog.hide();
                }
            });
        }
    }

    /* 等待Dialog具有屏蔽其他控件的交互能力
         * @setCancelable 为使屏幕不可点击，设置为不可取消(false)
         * 下载等事件完成后，主动调用函数关闭该Dialog
         */
    private void showWaitingDialog() {
        waitingDialog.setTitle("查询车牌信息");
        waitingDialog.setMessage("等待中...");
        waitingDialog.setIndeterminate(true);
        waitingDialog.setCancelable(false);
        waitingDialog.show();
    }
}
