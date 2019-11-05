package com.example.a_veebviewtest2;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.hardware.Sensor;
import android.hardware.SensorManager;
import android.net.ConnectivityManager;
import android.net.DhcpInfo;
import android.net.NetworkInfo;
import android.net.wifi.ScanResult;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

public class position extends Activity {
    public static MainActivity activity;
    TextView textConnected, textIp, textSsid, textBssid, textMac, textRssi;
    public static TextView accelerometerData, orientationData, pressData, stepCounterData, stepDetectorData, currentDate;

    private SaveSensorData saveSensorData;
    private SensorManager sensor;
    private SaveAllData saveAllData;

    public WifiManager wifiManager;             //管理wifi
    public ConnectivityManager connectManager;  //管理网络连接

    public WifiInfo wifiInfo;  //wifi
    public DhcpInfo dhcpInfo;  //动态主机配置协议信息的对象，获得IP等网关信息

    private ToggleButton rssiColBtn; //采集数据按钮
    private Button saveSensorsBtn;//保存sensors数据按钮
    private Button saveBtn;//保存wifi数据按钮

    private Button backBtn;//保存wifi数据按钮


    private Thread timeThread;
    private Thread timeThread1;

    Boolean rssiColFlag = false;
    Boolean sensorColFlag = false;

    private boolean cancelThread = false;

    ArrayList<ScanResult> list = null;

    private boolean startCollect = false;

    public static ArrayList<HashMap<Integer, Integer>> dataRssi = new ArrayList<HashMap<Integer, Integer>> (); // 每行代表一个Wifi热点，对应一个map，map的第一个值是数据的index，第二个值是rssi
    public static HashMap<String, Integer> dataBssid = new HashMap<String, Integer> ();
    public static ArrayList<String> dataWifiNames = new ArrayList<String> ();
    public static int dataCount = 0;

    public static StringBuilder sbSensor = new StringBuilder ();

    SimpleDateFormat sdf = new SimpleDateFormat ("yyyy-MM-dd HH:mm:ss:SSS");

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate (savedInstanceState);
        setContentView (R.layout.position);

        textConnected = (TextView) findViewById (R.id.Connected);
        textIp = (TextView) findViewById (R.id.Ip);

        textSsid = (TextView) findViewById (R.id.Ssid);
        textBssid = (TextView) findViewById (R.id.Bssid);
        textMac = (TextView) findViewById (R.id.Mac);
        textRssi = (TextView) findViewById (R.id.Rssi);

        saveBtn = (Button) findViewById (R.id.saveBtn);
        saveBtn.setOnClickListener (saveOnRssClickListener);

        saveSensorsBtn = (Button) findViewById (R.id.saveSensorsBtn);
        saveSensorsBtn.setOnClickListener (saveOnSensorsClickListener);

        rssiColBtn = (ToggleButton) findViewById (R.id.rssiColBtn);
        rssiColBtn.setOnCheckedChangeListener (new NewOnCheckedChangeListener ());

        backBtn = (Button) findViewById (R.id.backBtn);
        backBtn.setOnClickListener (OnbackClickListener);

        orientationData = (TextView) findViewById (R.id.orientationData);
        accelerometerData = (TextView) findViewById (R.id.accelerometerData);
        pressData = (TextView) findViewById (R.id.pressData);
//        currentDate = (TextView) findViewById(R.id.currentDate);
        stepCounterData = (TextView) findViewById (R.id.stepCounterData);
//        stepDetectorData = (TextView) findViewById(R.id.stepDetectorData);

        saveSensorData = SaveSensorData.getInstance ();

        saveSensorData.init (this);

        sensor = (SensorManager) getSystemService (SENSOR_SERVICE);
        List<Sensor> sensorsList = sensor.getSensorList (Sensor.TYPE_ALL);
//        System.out.println("共有 "+sensorsList.size()+" 个传感器");
        allSensors (sensorsList);

        displayWifiState ();

        //采集所有wifi的数据信息
        timeThread = new Thread (new RssiTimeRunnable ());
        timeThread.start ();

        //采集传感器的数据信息
        timeThread1 = new Thread (new SensorTimeRunnable ());
        timeThread1.start ();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        //获取菜单？
        //getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    // 两次返回退出
    private long exitTime = 0;

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK
                && event.getAction () == KeyEvent.ACTION_DOWN) {
            if ((System.currentTimeMillis () - exitTime) > 2000) {
                Toast.makeText (getApplicationContext (), "再次点击“返回”退出",
                        Toast.LENGTH_SHORT).show ();
                exitTime = System.currentTimeMillis ();
            } else {
                finish ();
                System.exit (0);
            }
            return true;
        }
        return super.onKeyDown (keyCode, event);
    }

    protected void onStop() {
//        saveSensorData.unregist();
//        saveSensorData.dataClear();
        cancelThread = true;
        super.onStop ();
    }

    private class RssiTimeRunnable implements Runnable {
        @Override
        public void run() {
            while (!cancelThread) {
                runOnUiThread (new Runnable () {
                    @Override
                    public void run() {
//                        System.out.println("rssi collect suspend..."+rssiColFlag);
                        if (rssiColFlag == true)//没有中断
                        {
                            displayWifiState ();
                            collectWiFiData ();
                        }
                    }
                });
                try {
                    Thread.sleep (3000); //3s刷新一次
                } catch (InterruptedException e) {
                    e.printStackTrace ();
                }
            } //end while
        }//end run
    }

    private class SensorTimeRunnable implements Runnable {
        @Override
        public void run() {
            while (!cancelThread) {
                runOnUiThread (new Runnable () {
                    @Override
                    public void run() {
//                        System.out.println("sensors collect suspend..."+sensorColFlag);
                        if (sensorColFlag == true) {
                            if (saveSensorData != null) {
                                System.out.println ("---Start to collect sensor---");
                                sbSensor.append (sdf.format (new Date ()) + '\t');
                                if (saveSensorData.temp_stepC != 0) {
                                    sbSensor.append (saveSensorData.temp_stepC + ";" + '\t');
                                } else {
                                    sbSensor.append ("Stop;" + '\t');
                                }

                                sbSensor.append (String.format ("%.2f", saveSensorData.temp_press[0]) + ";" + '\t');
                                sbSensor.append (String.format ("%.2f", saveSensorData.temp_o[0]) + "," + String.format ("%.2f", saveSensorData.temp_o[1]) + "," + String.format ("%.2f", saveSensorData.temp_o[2]) + ";" + '\t');
                                sbSensor.append (String.format ("%.2f", saveSensorData.temp_a[0]) + "," + String.format ("%.2f", saveSensorData.temp_a[1]) + "," + String.format ("%.2f", saveSensorData.temp_a[2]) + ";");
                                sbSensor.append ("\r\n");
                            } else {
                                saveSensorData = saveSensorData.getInstance ();
                            }
                        }//end if
                    }//end run
                });
                try {
                    Thread.sleep (3000); //3s刷新一次
                } catch (InterruptedException e) {
                    e.printStackTrace ();
                }
            } //end while
        }//end run
    }

    private void displayWifiState() {
        wifiManager = (WifiManager) getApplicationContext ().getSystemService (WIFI_SERVICE); //获得系统wifi服务

        connectManager = (ConnectivityManager) getSystemService (CONNECTIVITY_SERVICE);

        NetworkInfo anmyNetworkInfo = connectManager.getNetworkInfo (ConnectivityManager.TYPE_WIFI);

        wifiManager.startScan (); //启动扫描
        list = (ArrayList<ScanResult>) wifiManager.getScanResults ();

        System.out.println ("rssi list size is:" + list.size ());

        //如果连接成功,则获取相关信息
        if (anmyNetworkInfo.isConnected ()) {
            textConnected.setText ("Connect to HNU BaseLab");

            dhcpInfo = wifiManager.getDhcpInfo ();
            wifiInfo = wifiManager.getConnectionInfo ();
            wifiInfo.getSSID ();

            String wifiProperty = "---- The connected wifi info is:----" + '\n' + wifiInfo.getSSID () + '\n' +
                    "ip:" + FormatString (dhcpInfo.ipAddress) + '\n' +
                    "mac:" + wifiInfo.getBSSID () + '\n' +
                    "mask:" + FormatString (dhcpInfo.netmask) + '\n' +
                    "netgate:" + FormatString (dhcpInfo.gateway) + '\n' +
                    "rssi:" + wifiInfo.getRssi () + " dBm" + '\n' +
                    "dns:" + FormatString (dhcpInfo.dns1);

            System.out.println (wifiProperty);

            textSsid.setText ("AP Name: " + wifiInfo.getSSID ());
            textIp.setText ("IP Addr: " + FormatString (dhcpInfo.ipAddress));
            textMac.setText ("Mask Addr: " + FormatString (dhcpInfo.netmask));
            textBssid.setText ("Mac Addr: " + wifiInfo.getBSSID ());
            textRssi.setText ("RSSI:" + String.valueOf (wifiInfo.getRssi ()) + " dBm");
        } else {
            textConnected.setText ("Not connected...");
            textIp.setText ("None");
            textSsid.setText ("None");
            textMac.setText ("None");
            textRssi.setText ("None");
        }
    }

    public void collectWiFiData() {
        if (list != null) {
            System.out.println ("---Start to collect rssi---");
            // 更新热点Mac地址的列表，只增不减，顺序不变，同时将RSSI记录下来
            for (int i = 0; i < list.size (); i++) {
                String strSsid = list.get (i).SSID;

                //只收集HNU AP的信息
                if (strSsid.equals ("HNU")) {
                    String strBssid = list.get (i).BSSID;
                    int strLevel = list.get (i).level; //RSSI

                    if (!dataBssid.containsKey (strBssid)) { // 新增一个wifi热点
                        dataBssid.put (strBssid, dataBssid.size ());
                        dataWifiNames.add (strSsid);
                        HashMap<Integer, Integer> tmp = new HashMap<Integer, Integer> ();
                        tmp.put (dataCount, strLevel);
                        dataRssi.add (tmp);
                        System.out.println ("new BSSID: " + strBssid + ", " + "RSSI: " + strLevel + " dbm;\n");
                    } else { // wifi热点已存在
                        dataRssi.get (dataBssid.get (strBssid)).put (dataCount, strLevel);
                        System.out.println ("BSSID: " + strBssid + ", " + "RSSI: " + strLevel + " dbm;\n");
                    }
                }
            }//end for

            dataCount++;
            System.out.println ("data count is: " + dataCount);
            Toast.makeText (position.this, "WiFi数据采集第 " + dataCount + " 次^_^", Toast.LENGTH_SHORT).show ();
        }//end if
        else {
            System.out.println ("No wifi AP...");
        }
    }

    public String FormatString(int value) {
        String strValue = "";
        byte[] ary = intToByteArray (value);
        for (int i = ary.length - 1; i >= 0; i--) {
            strValue += (ary[i] & 0xFF);
            if (i > 0) {
                strValue += ".";
            }
        }
        return strValue;
    }

    public byte[] intToByteArray(int value) {
        byte[] b = new byte[4];
        for (int i = 0; i < 4; i++) {
            int offset = (b.length - 1 - i) * 8;
            b[i] = (byte) ((value >>> offset) & 0xFF);
        }
        return b;
    }

    private class NewOnCheckedChangeListener implements CompoundButton.OnCheckedChangeListener {
        @Override
        public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
            startCollect = isChecked;
            if (startCollect) //按钮被选中,即开始收集
            {
                System.out.println ("------------Below are all WiFi infos------------");
                Toast.makeText (position.this, "开始数据采集",
                        Toast.LENGTH_SHORT).show ();

//                registerReceiver(cycleWifiReceiver,new IntentFilter(wifiManager.SCAN_RESULTS_AVAILABLE_ACTION));

                if (rssiColFlag == false) {
                    rssiColFlag = true;
                }
                if (sensorColFlag == false) {
                    sensorColFlag = true;
                }
            } else {
                System.out.println ("------------Stop the collecting...------------");
                Toast.makeText (position.this, "关闭数据采集",
                        Toast.LENGTH_SHORT).show ();
//                unregisterReceiver(cycleWifiReceiver);
                rssiColFlag = false;
                sensorColFlag = false;

                System.out.println ("----Stop successful...----");
            }
        }
    }

    private final BroadcastReceiver cycleWifiReceiver = new BroadcastReceiver () {
        @SuppressLint("UseSparseArrays")
        @Override
        public void onReceive(Context context, Intent intent) {
            System.out.println ("wifi list size is:" + list.size ());
            collectWiFiData ();
        }
    };

    //保存RSS数据
    private View.OnClickListener saveOnRssClickListener = new View.OnClickListener () {
        @Override
        public void onClick(View v) {
            if (rssiColBtn.isChecked ()) {
                Toast.makeText (position.this, "请先关闭数据采集再保存", Toast.LENGTH_SHORT).show ();
            } else {
                System.out.println ("collect data count is: " + dataCount);
                int saveResult = saveAllData.getInstance ().saveRSSIData ();
                if (saveResult == 0) {
                    Toast toast = Toast.makeText (position.this, "存储成功,请查阅AnmyDataCollect目录", Toast.LENGTH_SHORT);
                    toast.setGravity (Gravity.CENTER, 0, 0); //屏幕居中显示，X轴和Y轴偏移量都是0
                    toast.show ();
                } else {
                    Toast.makeText (position.this, "存储失败,请查询日志", Toast.LENGTH_SHORT).show ();
                }
            }
        }
    };

    //保存Sensors数据
    private View.OnClickListener saveOnSensorsClickListener = new View.OnClickListener () {
        @Override
        public void onClick(View v) {
            if (rssiColBtn.isChecked ()) {
                Toast.makeText (position.this, "请先关闭数据采集再保存", Toast.LENGTH_SHORT).show ();
            } else {
                int saveSensorResult = saveAllData.getInstance ().saveSensorData ();
                if (saveSensorResult == 0) {
                    Toast toast = Toast.makeText (position.this, "存储成功,请查阅AnmyDataCollect目录", Toast.LENGTH_SHORT);
                    toast.setGravity (Gravity.CENTER, 0, 0); //屏幕居中显示，X轴和Y轴偏移量都是0
                    toast.show ();
                } else {
                    Toast.makeText (position.this, "存储失败,请查询日志", Toast.LENGTH_SHORT).show ();
                }
            }
        }
    };
    //返回
    private View.OnClickListener OnbackClickListener = new View.OnClickListener () {
        @Override
        public void onClick(View v) {
            Intent intent;
            intent = new Intent(position.this, MainActivity.class);
            startActivity(intent);
        }
    };


    public void allSensors(List<Sensor> sensorsList) {
        StringBuilder sensorSb = new StringBuilder ();
        for (Sensor ss : sensorsList) {
            String msg = "类型:" + ss.getType () + ", 名字:" + ss.getName () + ", 版本:" + ss.getVersion () +
                    ", 供应商:" + ss.getVendor () + "\n";

            sensorSb.append (msg);
        }
        System.out.println (sensorSb.toString ());
    }

}

