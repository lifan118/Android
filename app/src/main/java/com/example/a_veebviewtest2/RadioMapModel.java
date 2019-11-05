package com.example.a_veebviewtest2;

import android.util.Log;
import android.widget.Toast;

import java.util.HashMap;

//加载Radio Map，包括bssid，长度、个数
public class RadioMapModel {
    public float radioMap1[][];// 存储指纹库
    public float radioMap2[][];
    static public HashMap<String, Integer> bssids; // 记录Bssid的顺序，用来查找
    public int N_fp = 0;// 指纹库的指纹长度
    public int M_fp = 0;// 指纹库的指纹个数

    private volatile static RadioMapModel radioMapModel = null;

    public static RadioMapModel getInstance() {
        if (radioMapModel == null) {
            synchronized (RadioMapModel.class) {
                if (radioMapModel == null) {
                    radioMapModel = new RadioMapModel ();
                }
            }
        }
        return radioMapModel;
    }

    public void init() {
        Log.i ("1", "123");
//        radioMap1 = getRadioMap("map2.txt");// 第一个Radio map
        radioMap1 = getRadioMap ("map2.txt");// 第二个Radio map
        Log.i ("1", "124");
        radioMap2 = getRadioMap ("map2.txt");// 第二个Radio map
        bssids = getBssids ("bssid.txt");// 对应的bssid
        Log.i ("1", "125");

        Log.i ("1", "Toast1");
//        Toast.makeText (appActivity.mainactivity, "读取成功",
//                Toast.LENGTH_SHORT).show ();
        Log.i ("1", "Toast2");
    }

    /**
     * 从assets里的文件中读取数据，string数组，第一个string代表每组指纹的长度，第二个string代表共有多少组数据
     * 将数据转换成float型，放到一个二维数组 (注意：各个string间由空格分开，不要有回车)
     */
    private float[][] getRadioMap(String fileName) {
        float radioMap[][] = null;
//        String[] strarray = new FileManager ().readFileStrings (fileName);
        // 转换成float
        String[] strarray = {
                "38", "2", "-54", "-75", "-69", "-72", "-54", "-95", "-65", "-81", "-90", "-71", "-74", "-74", "-79", "-84", "-91", "-89", "-81", "-93", "-93", "0", "0", "0", "0",
                "0", "0", "0", "0", "0", "0", "0", "0", "0", "0", "0", "0", "0", "1", "0",
                "-55", "-76", "-66", "-71", "-55", "-95", "-66", "-79", "-89", "-62", "-64", "-63", "-79", "-84", "-87", "-89", "-46", "-80", "-80", "-90", "-84", "-90", "-89", "-95", "0", "0", "0", "0", "0", "0", "0", "0", "0", "0", "0", "0", "0", "0"
        };

        N_fp = Integer.parseInt (strarray[0]);
        M_fp = Integer.parseInt (strarray[1]);


        radioMap = new float[M_fp][N_fp];
        int k = 2;
        for (int i = 0; i < M_fp; i++) {
            for (int j = 0; j < N_fp; j++) {
                try {
                    radioMap[i][j] = Float.parseFloat (strarray[k]);
                    System.out.println (radioMap[i][j]);
                    k++;
                } catch (Exception e) {
                }
            }
        }
        return radioMap;
    }

    private HashMap<String, Integer> getBssids(String fileName) {
        HashMap<String, Integer> bssids = new HashMap<String, Integer> ();
//        String[] strarray = new FileManager ().readFileStrings (fileName);
        String[] strarray = {
                "0c:da:41:24:dd:60", "0c:da:41:25:6a:00", "0c:da:41:25:69:20", "0c:da:41:25:6a:60",
                "0c:da:41:24:dc:a0", "0c:da:41:24:db:c0", "0c:da:41:25:6a:70", "0c:da:41:24:db:d0",
                "ac:74:09:63:db:60", "0c:da:41:24:dd:70", "0c:da:41:25:6a:10", "0c:da:41:25:69:30"
        };
        for (int i = 0; i < strarray.length; i++) {
            bssids.put (strarray[i], i);
            Log.i ("1", "key:" + strarray[i] + "  value:" + i);
        }
        return bssids;
    }
}
