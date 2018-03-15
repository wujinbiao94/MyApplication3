package com.example.myapplication;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.ThumbnailUtils;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import java.io.File;
import java.io.IOException;
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
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_camera);

        cameraBtn = (ImageButton) findViewById(R.id.camera_photo);
        img_show = (ImageView) findViewById(R.id.img_show);
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
                        Log.e("文件创建失败！",null);
                        e.printStackTrace();
                    }
                }

                Intent it = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                it.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(currentImageFile));
                startActivityForResult(it, Activity.DEFAULT_KEYS_DIALER);
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
            Bitmap bitmap = BitmapFactory.decodeFile(filePath);
            //利用Bitmap对象创建缩略图
            Bitmap showbitmap = ThumbnailUtils.extractThumbnail(bitmap, 250, 250);
            img_show.setImageBitmap(showbitmap);
        }
    }
}
