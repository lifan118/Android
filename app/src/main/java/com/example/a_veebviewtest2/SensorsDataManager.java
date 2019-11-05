package com.example.a_veebviewtest2;

import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.util.Log;

//传感器获取的数据的管理
public class SensorsDataManager {
    private SensorManager sensorManager;
    private Sensor osensor;
    private SensorEventListener oSensorListener;
    // 以后和惯导进行混合定位的时候用
    private Sensor msensor;
    private SensorEventListener mSensorListener;

    public float[] temp_m = new float[3];
    public float[] temp_r = new float[3];

    private volatile static SensorsDataManager sensorsDataManager = null;

    public static SensorsDataManager getInstance() {
        if (sensorsDataManager == null) {
            synchronized (SensorsDataManager.class) {
                if (sensorsDataManager == null) {
                    sensorsDataManager = new SensorsDataManager ();
                }
            }
        }
        return sensorsDataManager;
    }

    // 初始化各种Sensors的操作
    public void initSensors() {
        Log.i ("1","initSensors1");
        sensorManager = (SensorManager) appActivity.mainactivity
                .getSystemService (Context.SENSOR_SERVICE);
        msensor = sensorManager.getDefaultSensor (Sensor.TYPE_MAGNETIC_FIELD);
        osensor = sensorManager.getDefaultSensor (Sensor.TYPE_ORIENTATION);
        Log.i ("1","initSensors2");
        mSensorListener = new MSensorListener ();
        oSensorListener = new OSensorListener ();
        // sensorManager.registerListener(mSensorListener, msensor,
        // SensorManager.SENSOR_DELAY_NORMAL);
        sensorManager.registerListener (oSensorListener, osensor,
                SensorManager.SENSOR_DELAY_UI);
        Log.i ("1","initSensors3");
    }

    private class MSensorListener implements SensorEventListener {
        @Override
        public void onSensorChanged(SensorEvent event) {
            temp_m[0] = event.values[0];
            temp_m[1] = event.values[1];
            temp_m[2] = event.values[2];
        }

        @Override
        public void onAccuracyChanged(Sensor sensor, int accuracy) {
        }
    }

    private class OSensorListener implements SensorEventListener {
        @Override
        public void onSensorChanged(SensorEvent event) {
            temp_r[0] = event.values[0];
            temp_r[1] = event.values[1];
            temp_r[2] = event.values[2];
        }

        @Override
        public void onAccuracyChanged(Sensor sensor, int accuracy) {
        }
    }

    public void unregist() {
        sensorManager.unregisterListener (oSensorListener);
    }
}
