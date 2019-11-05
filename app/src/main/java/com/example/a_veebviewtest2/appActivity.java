package com.example.a_veebviewtest2;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.webkit.WebView;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.HashMap;

import algorithm.KNNLocalization;

public class appActivity extends Activity {
    private ImageView imageViewapp; //图片
    private Button buttonMeapp;     //我
    private Button buttonAppapp;    //平面
    private Button buttonMapapp;    // 3D
    private Button buttonDingwei;    // 定位


    public static appActivity mainactivity;
    private TextView StatusTextView;
    private TextView resultsTextView;
    private ImageButton myPositionImageButton;
    private ImageView planMapImageView;
    public float result_x0 = 0f;
    public float result_y0 = 0f;
    public int result_num0 = 0;
    int width; // 屏幕宽度（像素）
    int height; // 屏幕高度（像素）
    float density; // 屏幕密度（0.75 / 1.0 / 1.5）
    int densityDpi; // 屏幕密度DPI（120 / 160 / 240）
    public int Wifi_Num_Min = 3;// 定位时wifi热点的最小数量

    Button.OnClickListener control = new View.OnClickListener() {
        public void onClick(View view) {
            Intent intent;
            switch (view.getId()) {
                case R.id.meapp://数据采集
                    intent = new Intent(appActivity.this, position.class);
                    startActivity(intent);
                    break;
                case R.id.appapp://平面
//                    intent = new Intent (MainActivity.this, appActivity.class);
//                    startActivity (intent);
                    break;
                case R.id.mapapp://3D
                    intent = new Intent(appActivity.this, MainActivity.class);
                    startActivity(intent);
                    break;
                case R.id.dingwei:
                    // 生成rssScan数组
                    //float rssScan[] = new float[229];
                    int bsscount = 0;
                    //if (scanResults != null) {
                    // 初始化
                    for (int i = 0; i < WiFiDataManager.rssScan.length; i++) {
                        WiFiDataManager.rssScan[i] = -200;
                        // }
                        //for (int j = 0; j < 4; j++) {
                        String bssid1 = "0c:da:41:24:dd:60";
                        String bssid2 = "0c:da:41:25:6a:00";
                        String bssid3 = "0c:da:41:25:69:20";
                        String bssid4 = "0c:da:41:25:6a:60";
                        String bssid5 = "0c:da:41:24:dc:a0";
                        String bssid6 = "0c:da:41:24:db:c0";
                        String bssid7 = "0c:da:41:25:6a:70";
                        String bssid8 = "0c:da:41:24:db:d0";
                        String bssid9 = "ac:74:09:63:db:60";
                        String bssid10 = "0c:da:41:24:dd:70";
                        String bssid11 = "0c:da:41:25:6a:10";
                        String bssid12 = "0c:da:41:25:69:30";
                        //1 4
                        if (RadioMapModel.bssids.containsKey(bssid1)) {
                            int idx = RadioMapModel.bssids.get(bssid1);
                            WiFiDataManager.rssScan[idx] = -63;
                            bsscount++;
                        }
                        if (RadioMapModel.bssids.containsKey(bssid2)) {
                            int idx = RadioMapModel.bssids.get(bssid2);
                            WiFiDataManager.rssScan[idx] = -77;
                            bsscount++;
                        }
                        if (RadioMapModel.bssids.containsKey(bssid3)) {
                            int idx = RadioMapModel.bssids.get(bssid3);
                            WiFiDataManager.rssScan[idx] = -60;
                            bsscount++;
                        }
                        if (RadioMapModel.bssids.containsKey(bssid4)) {
                            int idx = RadioMapModel.bssids.get(bssid4);
                            WiFiDataManager.rssScan[idx] = -67;
                            bsscount++;
                        }
                        if (RadioMapModel.bssids.containsKey(bssid5)) {
                            int idx = RadioMapModel.bssids.get(bssid5);
                            WiFiDataManager.rssScan[idx] = -49;
                            bsscount++;
                        }
                        if (RadioMapModel.bssids.containsKey(bssid6)) {
                            int idx = RadioMapModel.bssids.get(bssid6);
                            WiFiDataManager.rssScan[idx] = -93;
                            bsscount++;
                        }
                        if (RadioMapModel.bssids.containsKey(bssid7)) {
                            int idx = RadioMapModel.bssids.get(bssid7);
                            WiFiDataManager.rssScan[idx] = -63;
                            bsscount++;
                        }
                        if (RadioMapModel.bssids.containsKey(bssid8)) {
                            int idx = RadioMapModel.bssids.get(bssid8);
                            WiFiDataManager.rssScan[idx] = -79;
                            bsscount++;
                        }
                        if (RadioMapModel.bssids.containsKey(bssid9)) {
                            int idx = RadioMapModel.bssids.get(bssid9);
                            WiFiDataManager.rssScan[idx] = -95;
                            bsscount++;
                        }
                        if (RadioMapModel.bssids.containsKey(bssid10)) {
                            int idx = RadioMapModel.bssids.get(bssid10);
                            WiFiDataManager.rssScan[idx] = -60;
                            bsscount++;
                        }
                        if (RadioMapModel.bssids.containsKey(bssid11)) {
                            int idx = RadioMapModel.bssids.get(bssid11);
                            WiFiDataManager.rssScan[idx] = -63;
                            bsscount++;
                        }
                        if (RadioMapModel.bssids.containsKey(bssid12)) {
                            int idx = RadioMapModel.bssids.get(bssid12);
                            WiFiDataManager.rssScan[idx] = -54;
                            bsscount++;
                        }
                    }
                    if (bsscount < appActivity.mainactivity.Wifi_Num_Min) {
                        WiFiDataManager.getInstance().isNormal = false;
                    } else {
                        WiFiDataManager.getInstance().isNormal = true;
                    }
                    new KNNLocalization().start();
            }
        }
    };

    protected void onCreate(Bundle save) {
        super.onCreate(save);
        setContentView(R.layout.applayout);
        imageViewapp = findViewById(R.id.img);
        buttonMeapp = (Button) findViewById(R.id.meapp);
        buttonMeapp.setOnClickListener(control);
        buttonAppapp = (Button) findViewById(R.id.appapp);
        buttonAppapp.setOnClickListener(control);
        buttonMapapp = (Button) findViewById(R.id.mapapp);
        buttonMapapp.setOnClickListener(control);
//        buttonPosition = findViewById (R.id.position);
//        buttonPosition.setOnClickListener (control);
        buttonDingwei = (Button) findViewById(R.id.dingwei);
        buttonDingwei.setOnClickListener(control);
        if (!isWifiConnect()) {
            AlertDialog.Builder dialog = new AlertDialog.Builder (this);
            dialog.setTitle ("温馨提示").setMessage ("请连接wifi");
            dialog.setCancelable(false);
            //点击确定就退出程序
            dialog.setPositiveButton ("确定", new DialogInterface.OnClickListener () {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    if(!isWifiConnect()){
                    Intent intent = new Intent();
                    intent.setAction("android.net.wifi.PICK_WIFI_NETWORK");
                    startActivity(intent); }
                    appActivity.this.recreate();
                }
            });
            dialog.setNegativeButton ("取消", new DialogInterface.OnClickListener () {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    Intent intent;
                    intent = new Intent(appActivity.this, MainActivity.class);
                    startActivity(intent);
                }
            });
            dialog.show ();
        }else {
            mainactivity = this;
            intiResources();
            SensorsDataManager.getInstance().initSensors();
            WiFiDataManager.getInstance().initWifi();
            WiFiDataManager.getInstance().startScanWifi();
        }
    }

    protected void onStop() {
 //       SensorsDataManager.getInstance().unregist();
        super.onStop();
    }

    private void intiResources() {

        planMapImageView = (ImageView) findViewById(R.id.img);

        myPositionImageButton = (ImageButton) findViewById(R.id.imageButton1);
        // 点击定位点图标的监听程序
        myPositionImageButton.setOnClickListener(new NewImageOnClickListener());
        DisplayMetrics metric = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(metric);
        width = metric.widthPixels; // 屏幕宽度（像素）
        height = metric.heightPixels; // 屏幕高度（像素）
        density = metric.density; // 屏幕密度（0.75 / 1.0 / 1.5）
        densityDpi = metric.densityDpi; // 屏幕密度DPI（120 / 160 / 240）
    }
// 两次返回退出
//    private long exitTime = 0;
//    @Override
//    public boolean onKeyDown(int keyCode, KeyEvent event) {
//        if (keyCode == KeyEvent.KEYCODE_BACK
//                && event.getAction() == KeyEvent.ACTION_DOWN) {
//            if ((System.currentTimeMillis() - exitTime) > 2000) {
//                Toast.makeText(getApplicationContext(), "再次点击“返回”退出",
//                        Toast.LENGTH_SHORT).show();
//                exitTime = System.currentTimeMillis();
//            } else {
//                finish();
//                System.exit(0);
//            }
//            return true;
//        }
//        return super.onKeyDown(keyCode, event);
//    }

    public void uiUpdate() {
        String outString = "定位结果:\n";


        int l_left = planMapImageView.getLeft();
        int l_right = planMapImageView.getRight();
        int l_bottom = planMapImageView.getBottom();
        int l_top = planMapImageView.getTop();

        outString = outString + "num: " + result_num0 + " x: " + result_x0
                + " y: " + result_y0;
        outString = outString + "\nwidth： " + width + " height: " + height
                + " density: " + density + " densityDip: " + densityDpi;
        outString = outString + "\n上下左右： " + l_top + " " + l_bottom + " "
                + l_left + " " + l_right;
        // TipsTextView.setText(outString);

        float map_width = 63;
        float map_height = 85;
        float offest_x = 3;
        float offset_y = 3;
        float ui_x = l_left + (l_right - l_left) * (57 - result_y0 + offest_x)
                / map_width;
        float ui_y = l_top + (l_bottom - l_top) * (79 - result_x0 + offset_y)
                / map_height;

        myPositionImageButton.setX(ui_x - myPositionImageButton.getWidth() / 2);
        myPositionImageButton.setY(ui_y - myPositionImageButton.getHeight());

        String outString2 = "定位坐标 x:  " + result_x0 + "  y: " + result_y0;
//        resultsTextView.setText (outString2);

        String tips;
        if (WiFiDataManager.getInstance().isNormal) {
            tips = "正常";
        } else {
            tips = "数据库中匹配不到足够的wifi";
        }
//        outString2 = "当前Wifi个数:"
//                + WiFiDataManager.getInstance ().scanResults.size () + " " + tips;
//        StatusTextView.setText (outString2);

    }

    private class NewImageOnClickListener implements View.OnClickListener {
        @Override
        // 在当前onClick方法中监听点击Button的动作
        public void onClick(View v) {
            String outString = " x: " + result_x0 + "\n" + " y: " + result_y0;
            new AlertDialog.Builder(mainactivity).setTitle("位置坐标")
                    .setMessage(outString).setPositiveButton("确定", null).show();
        }
    }

    public boolean isWifiConnect() {
        ConnectivityManager connManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo mWifiInfo = connManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI);
        return mWifiInfo.isConnected();
    }

    public void checkWifiState() {


        }

}