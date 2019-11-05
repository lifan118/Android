package com.example.a_veebviewtest2;

import java.io.IOException;
import java.io.InputStream;

import android.widget.Toast;


//读取文件
public class FileManager {
    public String[] readFileStrings(String fileName) {
        try {
            InputStream in ;
            in = SplashActivity.splashActivity.getResources().getAssets()
                    .open(fileName);
            int length = in.available();
            byte[] buffer = new byte[length];
            in.read(buffer);
            in.close();
            String txtString = new String(buffer);
            String[] strarray = txtString.split(" ");
            return strarray;
        } catch (IOException e) {
            Toast.makeText(appActivity.mainactivity, "文件读取失败",
                    Toast.LENGTH_SHORT).show();
            e.printStackTrace();
        }
        return null;
    }
}
