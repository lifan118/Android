package com.example.a_veebviewtest2;

import android.os.Environment;
import android.util.Log;

import java.io.*;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * @description 存储采集好的数据
 */
public class SaveAllData {
    /**
     */

    private volatile static SaveAllData saveAllData = null;

    public static SaveAllData getInstance() {
        if (saveAllData == null) {
            synchronized (SaveAllData.class) {
                if (saveAllData == null) {
                    saveAllData = new SaveAllData ();
                }
            }
        }
        return saveAllData;
    }

    public int saveRSSIData() {
        return saveRssiInfos ();
    }

    public int saveSensorData() {
        return saveSensorInfos ();
    }


    SimpleDateFormat sdf = new SimpleDateFormat ("yyyyMMddHHmmss");

    private int saveRssiInfos() {
        try {
            File sdCard = Environment.getExternalStorageDirectory ();
            File directory = new File (sdCard.getAbsolutePath () + "/ssssss");
//            File pathasad = Environment.getDataDirectory ();
//            File directory = new File (pathasad + "/ssssss");


            System.out.println ("directory is: " + directory);
            directory.mkdirs ();
//            File file = new File(directory, "dataRssi_" + sdf.format(new Date()) + ".txt");
            File file = new File (directory, "dataRssi.txt");
            FileOutputStream fOut = new FileOutputStream (file, true);
            OutputStream fos = fOut;
            DataOutputStream dos = new DataOutputStream (fos);

            dos.write (("collect data at " + sdf.format (new Date ()) + "\n").getBytes ());

            String bssids = allBssidInfo (position.dataBssid);
            System.out.println ("----bssids are :" + bssids);
            dos.write (bssids.getBytes ());

            for (int i = 0; i < position.dataCount; i++) {
                String num = (i + 1) + ".\t";
                dos.write (num.getBytes ());

                // 读取并保存各wifi的Rssi数据
                for (int j = 0; j < position.dataBssid.size (); j++) {
                    if (position.dataRssi.get (j).containsKey (i)) {
                        dos.write ((position.dataRssi.get (j).get (i) + "\t\t").getBytes ());
                    } else {
                        dos.write ((100 + "\t\t").getBytes ()); //没有则存100,方便格式对齐
                    }
                }

                dos.write ("\n".getBytes ());
                // 存传感器数据// TODO: 18/11/19
            }
            dos.close ();
            return 0; //success
        } catch (FileNotFoundException e) {
            System.out.println ("file not found exception...");
            Log.v("Save","file not found exception...");
            return 1;
        } catch (IOException e) {
            System.out.println ("IO Exception exception...");
            Log.v("Save","IO Exception exception..");
            return 2;
        }
    }

    private String allBssidInfo(HashMap<String, Integer> bssidMap) {
        StringBuilder sb = new StringBuilder ();
        int counts = bssidMap.size ();

        System.out.println ("----bssid size is:---- " + counts);

        // 升序比较器
        Comparator<Map.Entry<String, Integer>> valueComparator = new Comparator<Map.Entry<String, Integer>> () {
            @Override
            public int compare(Map.Entry<String, Integer> o1, Map.Entry<String, Integer> o2) {
                // TODO Auto-generated method stub
                return o1.getValue () - o2.getValue ();
            }
        };

        // map转换成list进行排序
        List<Map.Entry<String, Integer>> list = new ArrayList<Map.Entry<String, Integer>> (bssidMap.entrySet ());

        // 排序
        Collections.sort (list, valueComparator);

        for (Map.Entry<String, Integer> entry : list) {
            System.out.println (entry.getKey () + ":" + entry.getValue ());
            sb.append (entry.getKey () + "\t\t");
            System.out.println ("bssid2 " + entry.getKey ());
        }

        sb.append ('\n');
        return sb.toString ();
    }

    private int saveSensorInfos() {
        try {
            File sdCard = Environment.getExternalStorageDirectory ();
            File directory = new File (sdCard.getAbsolutePath () + "/app");
            System.out.println ("directory is: " + directory);
            directory.mkdirs ();
            File file = new File (directory, "dataSensor_" + sdf.format (new Date ()) + ".txt");
            FileOutputStream fOut = new FileOutputStream (file, true);
            OutputStream fos = fOut;
            DataOutputStream dos = new DataOutputStream (fos);

            dos.write (("collect sensor data at " + sdf.format (new Date ()) + "\n").getBytes ());

            String sensors = "时间" + '\t' + "步数总数" + '\t' + "压力" + '\t' + "方向" + '\t' + "加速度";
            dos.write (sensors.getBytes ());
            dos.write ("\r\n".getBytes ());

            StringBuilder sbSensorInfo = position.sbSensor;

            if (sbSensorInfo.toString () != "" && sbSensorInfo.toString () != "") {
                dos.write (sbSensorInfo.toString ().getBytes ());
            }

            dos.close ();
            return 0; //success
        } catch (FileNotFoundException e) {
            System.out.println ("file not found exception...");
            return 1;
        } catch (IOException e) {
            System.out.println ("IO Exception exception...");
            return 2;
        }
    }
}
