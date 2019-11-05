package com.example.a_veebviewtest2;

import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.wifi.ScanResult;
import android.net.wifi.WifiManager;
import android.util.Log;
import android.annotation.SuppressLint;
import algorithm.KNNLocalization;

import static android.content.Context.WIFI_SERVICE;

//WiFi扫描数据的管理
public class WiFiDataManager {
    public static final long WIFI_SCAN_DELAY = 1000;
    private WifiManager wifiManager;
    public List<ScanResult> scanResults = null;
    private Timer wifiScanTimer;
    private TimerTask wifiScanTimerTask;
    static public float rssScan[]  =new float[12];
    public boolean isNormal = true;
    public ConnectivityManager connectManager;  //管理网络连接

    private volatile static WiFiDataManager wiFiDataManager = null;

    public static WiFiDataManager getInstance() {
        if (wiFiDataManager == null) {
            synchronized (WiFiDataManager.class) {
                if (wiFiDataManager == null) {
                    wiFiDataManager = new WiFiDataManager ();
                }
            }
        }
        return wiFiDataManager;
    }

    // 初始化WIFI，开启
    public void initWifi() {
        Log.i ("1", "initWifi1");
        wifiManager = (WifiManager) appActivity.mainactivity.getApplicationContext ()
                .getSystemService (WIFI_SERVICE);
//        Toast.makeText(appActivity.mainactivity, "正在开启WiFi...",
//                Toast.LENGTH_SHORT).show();
        wifiManager.setWifiEnabled (true);
        while (wifiManager.getWifiState () != WifiManager.WIFI_STATE_ENABLED) {
            // TipsTextView.setText("正在开启WiFi，请稍候");
        }
        Log.i ("1", "initWifi2");
        // TipsTextView.setText("WiFi已开启");
    }

    // 设置Timer任务，开始Wifi扫描
    @SuppressLint("WifiManagerLeak")
    public void startScanWifi() {
        Log.i ("1", "startScanWifi1");
//        appActivity.mainactivity.registerReceiver (wifiReceiver,
//                new IntentFilter (WifiManager.SCAN_RESULTS_AVAILABLE_ACTION));

//        IntentFilter intentFilter = new IntentFilter();
//        intentFilter.addAction("android.net.wifi.SCAN_RESULTS");
//        BroadcastReceiver context.registerReceiver(new InOutWifiScanResultsReceiver(), intentFilter);

//
//        wifiManager = (wifiManager)getSystemService (WIFI_SERVICE);
//        connectManager = (ConnectivityManager)getSystemService (CONNECTIVITY_SERVICE);
//        NetworkInfo anmy = connectManager.getNetworkInfo (ConnectivityManager.TYPE_WIFI);
//        wifiManager.startScan ();
//        ArrayList<ScanResult> list = (ArrayList<ScanResult>)wifiManager.getScanResults ();

        wifiScanTimer = new Timer ();
        wifiScanTimerTask = new TimerTask () {
            public void run() {
                wifiManager.startScan();
                Log.i ("1", "wifi个数" + wifiManager.getScanResults ());
            }
        };
        wifiScanTimer.schedule (wifiScanTimerTask, 60, WIFI_SCAN_DELAY);

        List<ScanResult> scanResults = wifiManager.getScanResults ();
        Log.i ("1", "wifi个数=" + scanResults.size ());
    }

    private final BroadcastReceiver wifiReceiver = new BroadcastReceiver () {
        @Override
        public void onReceive(Context context, Intent intent) {
            Log.i ("1", "wifiReceiver1");
            scanResults = wifiManager.getScanResults ();
            Log.i ("1", "scanResults");
            rssScan = new TransformUtil ().scanResults2vector (scanResults,
                    RadioMapModel.getInstance ().bssids);
            context.unregisterReceiver (this);
            Log.i ("1", "wifiReceiver2");
            new KNNLocalization ().start ();
        }
    };

}
//package com.example.a_veebviewtest2;
//
//        import android.app.Activity;
//        import android.content.Intent;
//        import android.os.Bundle;
//        import android.os.Handler;
//        import android.view.Window;
//
//public class SplashActivity extends Activity {
//    public static SplashActivity splashActivity;
//    private final long SPLASH_LENGTH = 400;
//    Handler handler = new Handler ();
//
//    /**
//     * Called when the activity is first created.
//     */
//    @Override
//    public void onCreate(Bundle savedInstanceState) {
//        super.onCreate (savedInstanceState);
//        requestWindowFeature (Window.FEATURE_NO_TITLE);
////        setContentView(R.layout.splash);
//        splashActivity = this;
//        handler.postDelayed (new Runnable () {
//            @Override
//            public void run() {
//                RadioMapModel.getInstance ().init ();
//                Intent intent = new Intent (SplashActivity.this,
//                        appActivity.class);
//                SplashActivity.this.startActivity (intent);
//                SplashActivity.this.finish ();
//            }
//        }, SPLASH_LENGTH);// 2秒后跳转
//    }
//}
