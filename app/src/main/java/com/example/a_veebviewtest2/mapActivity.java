package com.example.a_veebviewtest2;

import android.app.Activity;
import android.os.Bundle;
import android.widget.ListView;
import android.widget.SimpleAdapter;

import java.util.ArrayList;
import java.util.HashMap;

public class mapActivity extends Activity {
    ListView lv;

    protected void onCreate(Bundle save) {
        super.onCreate (save);
        setContentView (R.layout.maplayout);
        lv = (ListView) findViewById (R.id.lv);

        //定义动态数组与视图组件绑定数据
        ArrayList<HashMap<String, Object>> listItem = new ArrayList<HashMap<String, Object>> ();

        //存入数据
        for (int i = 0; i < 6; i++) {
            HashMap<String, Object> map = new HashMap<> ();
            switch (i) {
                case 0:
                    map.put ("ItemImage", R.drawable.asfff);
                    map.put ("ItemTitle", "室内地图展示");
                    listItem.add (map);
                    break;
                case 1:
                    map.put ("ItemImage", R.drawable.asfff);
                    map.put ("ItemTitle", "定位");
                    listItem.add (map);
                    break;
                case 2:
                    map.put ("ItemImage", R.drawable.asfff);
                    map.put ("ItemTitle", "导航");
                    listItem.add (map);
                    break;
                case 3:
                    map.put ("ItemImage", R.drawable.asfff);
                    map.put ("ItemTitle", "课程签到");
                    listItem.add (map);
                    break;
                case 4:
                    map.put ("ItemImage", R.drawable.asfff);
                    map.put ("ItemTitle", "空教室查询");
                    listItem.add (map);
                    break;
                case 5:
                    map.put ("ItemImage", R.drawable.asfff);
                    map.put ("ItemTitle", "老是动态");
                    listItem.add (map);
                    break;
            }
        }
        //需要绑定的数据
        //每一行的布局
        //动态数组中的数据源的键对应到定义布局的View中
        SimpleAdapter mSimpleAdapter = new SimpleAdapter (this, listItem, R.layout.meitem, new String[]{"ItemImage", "ItemTitle"}, new int[]{R.id.ItemImage, R.id.ItemTitle});

        lv.setAdapter (mSimpleAdapter);//为ListView绑定适配器
    }

}

