package com.example.a_veebviewtest2;

import android.app.Activity;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.SimpleAdapter;

import org.xml.sax.helpers.AttributeListImpl;

import java.util.ArrayList;
import java.util.HashMap;

public class meActivity extends Activity {
    ImageView imageView;
    ListView lv;

    protected void onCreate(Bundle save) {
        super.onCreate (save);
        setContentView (R.layout.melayout);
        imageView = (ImageView) findViewById (R.id.imageView5);
        imageView.setImageResource (R.drawable.user);

        lv = (ListView) findViewById (R.id.lv);

        //定义动态数组与视图组件绑定数据
        ArrayList<HashMap<String, Object>> listItem = new ArrayList<HashMap<String, Object>> ();

        //存入数据
        for (int i = 0; i < 6; i++) {
            HashMap<String, Object> map = new HashMap<> ();
            switch (i) {
                case 0:
                    map.put ("ItemImage", R.drawable.asfff);
                    map.put ("ItemTitle", "个人信息");
                    listItem.add (map);
                    break;
                case 1:
                    map.put ("ItemImage", R.drawable.asfff);
                    map.put ("ItemTitle", "签到记录");
                    listItem.add (map);
                    break;
                case 2:
                    map.put ("ItemImage", R.drawable.asfff);
                    map.put ("ItemTitle", "教室预约记录");
                    listItem.add (map);
                    break;
                case 3:
                    map.put ("ItemImage", R.drawable.asfff);
                    map.put ("ItemTitle", "待办事项");
                    listItem.add (map);
                    break;
                case 4:
                    map.put ("ItemImage", R.drawable.asfff);
                    map.put ("ItemTitle", "问题反馈");
                    listItem.add (map);
                    break;
                case 5:
                    map.put ("ItemImage", R.drawable.asfff);
                    map.put ("ItemTitle", "当前版本  1.0.0");
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
