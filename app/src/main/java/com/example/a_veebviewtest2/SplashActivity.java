package com.example.a_veebviewtest2;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.Window;

public class SplashActivity extends Activity {
    public static SplashActivity splashActivity;
    private final long SPLASH_LENGTH = 400;
    Handler handler = new Handler ();

    /**
     * Called when the activity is first created.
     */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate (savedInstanceState);
        requestWindowFeature (Window.FEATURE_NO_TITLE);
//        setContentView(R.layout.splash);
        splashActivity = this;
        handler.postDelayed (new Runnable () {
            @Override
            public void run() {
                RadioMapModel.getInstance ().init ();
                Intent intent = new Intent (SplashActivity.this,
                        appActivity.class);
                SplashActivity.this.startActivity (intent);
                SplashActivity.this.finish ();
            }
        }, SPLASH_LENGTH);// 2秒后跳转
    }
}
