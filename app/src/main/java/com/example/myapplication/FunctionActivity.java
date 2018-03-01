package com.example.myapplication;

import android.content.Context;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.amap.api.maps.model.LatLng;
import com.amap.api.maps.model.Poi;
import com.amap.api.navi.AMapNavi;
import com.amap.api.navi.AmapNaviPage;
import com.amap.api.navi.AmapNaviParams;
import com.amap.api.navi.AmapNaviType;
import com.amap.api.navi.INaviInfoCallback;
import com.amap.api.navi.model.AMapNaviLocation;
import com.example.myapplication.util.AmapTTSController;

public class FunctionActivity extends AppCompatActivity implements INaviInfoCallback {
    private Context mContext;
    private AmapTTSController amapTTSController;
    //导航按钮
    Button navigation;
    // 返回主线程更新数据
    private static Handler handler = new Handler();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_function);

        navigation = (Button) findViewById(R.id.navigation);
        //导航
        navigation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // System.out.println(password.getText());
                new Thread(new FunctionActivity.NavigationThread()).start();
            }
        });

    }

    @Override
    public void onInitNaviFailure() {

    }

    /**
     * 导航语音播报
     * @param s
     */
    @Override
    public void onGetNavigationText(String s) {
        AMapNavi mAMapNavi = null;
        mAMapNavi = AMapNavi.getInstance(this);
        mAMapNavi.setUseInnerVoice(true);
        //amapTTSController.onGetNavigationText(s);
    }

    @Override
    public void onLocationChange(AMapNaviLocation aMapNaviLocation) {

    }

    @Override
    public void onArriveDestination(boolean b) {

    }

    @Override
    public void onStartNavi(int i) {

    }

    @Override
    public void onCalculateRouteSuccess(int[] ints) {

    }

    @Override
    public void onCalculateRouteFailure(int i) {

    }

    @Override
    public void onStopSpeaking() {

    }

    @Override
    public void onReCalculateRoute(int i) {

    }

    @Override
    public void onExitPage(int i) {

    }

    /**
     * 导航页面跳转子线程
     */
    public class NavigationThread implements Runnable {
        @Override
        public void run() {
            handler.post(new Runnable() {
                @Override
                public void run() {
                    Poi start = new Poi("三元桥", new LatLng(39.96087,116.45798), "");
                    /**终点传入的是北京站坐标,但是POI的ID "B000A83M61"对应的是北京西站，所以实际算路以北京西站作为终点**/
                    Poi end = new Poi("北京站", new LatLng(39.904556, 116.427231), "B000A83M61");
                    AmapNaviPage.getInstance().showRouteActivity(getApplicationContext(), new AmapNaviParams(start, null, end, AmapNaviType.DRIVER), FunctionActivity.this);
                   // AmapNaviPage.getInstance().showRouteActivity(getApplicationContext(), new AmapNaviParams(null), FunctionActivity.this);
                    /*Intent intent = new Intent(FunctionActivity.this, MapActivity.class);
                    startActivity(intent);*/
                }
            });
        }
    }
}
