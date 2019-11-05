package algorithm;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.TreeMap;

public class KNNPosition {
    public static String rssValues = null;
    public static Map<Float, Integer> map = new TreeMap<Float, Integer> ();

    public static void main(String[] args) {
        try {
            FileReader fr = new FileReader ("C:/Users/zhw/Desktop/bisai/data4AP01.txt");
            BufferedReader br = new BufferedReader (fr);

            Map<String, Integer> colRssMap = new HashMap<String, Integer> (); //colRssMap为实时采集的当前楼层的各AP MAC地址和RSS值
            colRssMap.put ("0c:da:41:24:dd:60", -55);
            colRssMap.put ("0c:da:41:24:dc:b0", 0);
            colRssMap.put ("0c:da:41:25:14:70", -51);
            colRssMap.put ("0c:da:41:25:6a:70", -59);
//            colRssMap.put("0c:da:41:24:dd:70",-56);
//            colRssMap.put("0c:da:41:25:6a:00",-84);

            try {
                rssValues = br.readLine ();
            } catch (IOException e1) {
                e1.printStackTrace ();
            }

            getPosition (colRssMap, br);

            try {
                br.close ();
            } catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace ();
            }
            try {
                fr.close ();
            } catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace ();
            }
        } catch (FileNotFoundException e) {
            System.out.println ("The file not exist..");
        }
    }

    public static void getPosition(Map<String, Integer> colRssMap, BufferedReader br) {

        int count = 0; //记录文件中指纹点对应的标号
        float[] currentRss = new float[4]; //记录当前检测到所在楼层的各AP RSS值

        for (String key : colRssMap.keySet ()) {
            if (key == "0c:da:41:24:dd:60") {
                currentRss[0] = colRssMap.get (key);
            } else if (key == "0c:da:41:24:dc:b0") {
                currentRss[1] = colRssMap.get (key);
            } else if (key == "0c:da:41:25:14:70") {
                currentRss[2] = colRssMap.get (key);
            } else if (key == "0c:da:41:25:6a:70") {
                currentRss[3] = colRssMap.get (key);
            }
        }

        while (rssValues != null) {
            String[] rss = new String[4];
//                float[] currentRss = {-55,0,-51,-59};//当前检测到所在楼层的各AP RSS值
            float[] fpValue = new float[4];//存放指纹库中每个指纹对应的各AP RSS值
            double value = 0.0f;
            double[] gridRssValues = new double[25];//每层中采集的指纹总数

            if (rssValues.contains (",")) {
                rss = rssValues.split (",");
                float tmp = 0.0f;
                for (int i = 0; i < rss.length; i++) {
                    tmp = Float.parseFloat (rss[i]);
                    fpValue[i] = tmp;
                }

                //计算当前值与指纹点对应的各AP RSS值
                for (int i = 0; i < rss.length; i++) {
                    if (currentRss[i] != 0) //检测到此AP的RSS值
                    {
                        value += (currentRss[i] - fpValue[i]) * (currentRss[i] - fpValue[i]);
                    } else //未检测到此AP的RSS值,则置其为 -100dBm
                    {
                        value += (-100 - fpValue[i]) * (-100 - fpValue[i]);
                    }
                }
                value = Math.sqrt (value);
                gridRssValues[count] = value;
            }

            System.out.println ("The value is:" + String.format ("%.2f", value));

            map.put ((float) value, count);//将计算的对应各指纹点以及欧式距离值存放到treemap集合(按照键值从小到大进行排序)中

            try {
                rssValues = br.readLine ();
            } catch (IOException e) {
                e.printStackTrace ();
            }

            count++;
        }

        System.out.println ("--------------");

        int x = 0;
        float[] vlus = new float[4];
        int[] pos = new int[4];
        float sum = 0.0f, w1 = 0.0f, w2 = 0.0f, w3 = 0.0f, w4 = 0.0f; //sum为当前检测点与各指纹点中各RSS值的欧氏距离，wi为最近的四个点的权重值

        Iterator<Map.Entry<Float, Integer>> it = map.entrySet ().iterator ();

        while (it.hasNext () && x < 4) {
            Map.Entry<Float, Integer> entry = it.next ();
            sum += entry.getKey ();
            vlus[x] = entry.getKey ();//距离指纹参考点的欧氏距离值
            pos[x] = entry.getValue ();//对应参考点的位置
            x++;
            System.out.println (entry.getKey () + ":" + entry.getValue ());
        }

        System.out.println ("sum is:" + sum);

        w1 = vlus[3] / sum;
        w2 = vlus[2] / sum;
        w3 = vlus[1] / sum;
        w4 = vlus[0] / sum;
        System.out.println ("weights are:" + w1 + "," + w2 + "," + w3 + "," + w4);

        int px = 0, py = 0;
        float cal_x = 0.0f, cal_y = 0.0f;
        float x1 = 0.0f, x2 = 0.0f, x3 = 0.0f, x4 = 0.0f, y1 = 0.0f, y2 = 0.0f, y3 = 0.0f, y4 = 0.0f;

        for (int i = 0; i < 4; i++) {
            //参考点位置与坐标点位置进行转换 5*5的格子,长宽均为3m, 0-4对应 x轴坐标为0,y轴坐标为(0-4)*3m
            if (pos[i] < 5) {
                px = 0;
                py = pos[i] * 3; //3为格子的宽度3m
            } else if (pos[i] < 10) {
                px = 3;
                py = (pos[i] - 5) * 3;
            } else if (pos[i] < 15) {
                px = 6;
                py = (pos[i] - 10) * 3;
            } else if (pos[i] < 20) {
                px = 9;
                py = (pos[i] - 15) * 3;
            } else if (pos[i] < 25) {
                px = 12;
                py = (pos[i] - 20) * 3;
            }

            //计算各个坐标点对应的x轴和y轴权重值
            if (i == 0) {
                x1 = w1 * px;
                y1 = w1 * py;
            }

            if (i == 1) {
                x2 = w2 * px;
                y2 = w2 * py;
            }

            if (i == 2) {
                x3 = w3 * px;
                y3 = w3 * py;
            }

            if (i == 3) {
                x4 = w4 * px;
                y4 = w4 * py;
            }
        }

        cal_x = x1 + x2 + x3 + x4;
        cal_y = y1 + y2 + y3 + y4;

        System.out.println ("x pos:" + String.format ("%.2f", x1) + "," + String.format ("%.2f", x2) + "," + String.format ("%.2f", x3) + "," + String.format ("%.2f", x4));
        System.out.println ("y pos:" + String.format ("%.2f", y1) + "," + String.format ("%.2f", y2) + "," + String.format ("%.2f", y3) + "," + String.format ("%.2f", y4));
        System.out.println ("The cal_x and cal_y are:" + String.format ("%.2f", cal_x) + "," + String.format ("%.2f", cal_y));

        double error = Math.sqrt ((cal_x - 6) * (cal_x - 6) + (cal_y - 0) * (cal_y - 0));
        System.out.println ("The error is:" + String.format ("%.2f", error));
    }
}
