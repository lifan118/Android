package com.example.a_veebviewtest2;

import android.app.Activity;
import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;

import java.text.SimpleDateFormat;
import java.util.ArrayList;

/**
 * 传感器数据的处理，步数、方向、加速度和压力
 */
public class SaveSensorData {
    private SensorManager sensorManager;
    private Sensor oSensor;//方向传感器
    private Sensor aSensor; //加速度传感器
    private Sensor preSensor; //压力传感器
    private Sensor stepCounterSensor; //步数总数传感器
    private Sensor stepDetectorSensor; //步数单数传感器
    private SensorEventListener oSensorListener; //方向
    private SensorEventListener aSensorListener; //加速度
    private SensorEventListener pressSensorListener; //压力
    private SensorEventListener stepCounterListener;
    private SensorEventListener stepDetectorListener;

    private SimpleDateFormat simpleDateFormat;

    public float[] temp_o = new float[3];
    public float[] temp_a = new float[3];
    public float[] temp_press = new float[1];
    public int temp_stepC = 0;
    public int temp_stepD = 0;

    public ArrayList<ArrayList<Integer>> dataOrientation = new ArrayList<ArrayList<Integer>> (3);
    public ArrayList<ArrayList<Integer>> dataAccelerate = new ArrayList<ArrayList<Integer>> (3);
    public ArrayList<ArrayList<Integer>> dataPress = new ArrayList<ArrayList<Integer>> (1);
    public ArrayList<ArrayList<Integer>> dataStepCounter = new ArrayList<ArrayList<Integer>> (1);
    public ArrayList<ArrayList<Integer>> dataStepDetector = new ArrayList<ArrayList<Integer>> (1);

    private volatile static SaveSensorData saveSensorData = null;

    public static SaveSensorData getInstance() {
        if (saveSensorData == null) {
            synchronized (SaveSensorData.class) {
                if (saveSensorData == null) {
                    saveSensorData = new SaveSensorData ();
                }
            }
        }
        return saveSensorData;
    }

    public void init(Activity mainActivity) {
        // 下面采集相关传感器数据
        sensorManager = (SensorManager) mainActivity.getSystemService (Context.SENSOR_SERVICE);
        oSensor = sensorManager.getDefaultSensor (Sensor.TYPE_ORIENTATION);
        aSensor = sensorManager.getDefaultSensor (Sensor.TYPE_ACCELEROMETER);
        preSensor = sensorManager.getDefaultSensor (Sensor.TYPE_PRESSURE);
        stepCounterSensor = sensorManager.getDefaultSensor (Sensor.TYPE_STEP_COUNTER);
        stepDetectorSensor = sensorManager.getDefaultSensor (Sensor.TYPE_STEP_DETECTOR);

        dataInit ();
        oSensorListener = new OSensorListener ();//方向
        aSensorListener = new ASensorListener ();//加速度
        pressSensorListener = new PSensorListener (); //压力
        stepCounterListener = new SCSensorListener (); //总步数
//        stepDetectorListener = new SDSensorListener();//单步数

        sensorManager.registerListener (oSensorListener, oSensor, SensorManager.SENSOR_DELAY_NORMAL);
        sensorManager.registerListener (aSensorListener, aSensor, SensorManager.SENSOR_DELAY_NORMAL);
        sensorManager.registerListener (pressSensorListener, preSensor, SensorManager.SENSOR_DELAY_NORMAL);
        sensorManager.registerListener (stepCounterListener, stepCounterSensor, SensorManager.SENSOR_DELAY_FASTEST);
        sensorManager.registerListener (stepDetectorListener, stepDetectorSensor, SensorManager.SENSOR_DELAY_FASTEST);

        simpleDateFormat = new SimpleDateFormat ("yyyy-MM-dd HH:mm:ss:SSS");

//        System.out.println("是否支持StepCounter:"+ String.valueOf(mainActivity.getPackageManager().hasSystemFeature(PackageManager.FEATURE_SENSOR_STEP_COUNTER)));
//        System.out.println("是否支持StepDector:"+ String.valueOf(mainActivity.getPackageManager().hasSystemFeature(PackageManager.FEATURE_SENSOR_STEP_DETECTOR)));
    }

    private void dataInit() {
        for (int i = 0; i < 3; i++) {
            dataOrientation.add (new ArrayList<Integer> ());
            dataAccelerate.add (new ArrayList<Integer> ());
        }
        dataPress.add (new ArrayList<Integer> ());
        dataStepCounter.add (new ArrayList<Integer> ());
        dataStepDetector.add (new ArrayList<Integer> ());
    }

    public void dataClear() {
        for (int i = 0; i < 3; i++) {
            dataOrientation.get (i).clear ();
            dataAccelerate.get (i).clear ();
        }
        dataPress.get (0).clear ();
        dataStepCounter.get (0).clear ();
        dataStepDetector.get (0).clear ();
    }

    public void updateSensorsData() {
//        if (MainActivity.dataCount > dataAccelerate.get(0).size()) {
        dataOrientation.get (0).add (Integer.valueOf ((int) Math.floor (temp_o[0] * 100)));
        dataOrientation.get (1).add (Integer.valueOf ((int) Math.floor (temp_o[1] * 100)));
        dataOrientation.get (2).add (Integer.valueOf ((int) Math.floor (temp_o[2] * 100)));
        dataAccelerate.get (0).add (Integer.valueOf ((int) Math.floor (temp_a[0] * 100)));
        dataAccelerate.get (1).add (Integer.valueOf ((int) Math.floor (temp_a[1] * 100)));
        dataAccelerate.get (2).add (Integer.valueOf ((int) Math.floor (temp_a[2] * 100)));

        dataPress.get (0).add (Integer.valueOf ((int) temp_press[0]));
        dataStepCounter.get (0).add (temp_stepC);
        dataStepDetector.get (0).add (temp_stepD);
//        }
    }

    //压力传感器
    private class PSensorListener implements SensorEventListener {
        @Override
        public void onSensorChanged(SensorEvent event) {
            temp_press[0] = event.values[0];
            position.pressData.setText ("压力数据: " + String.format ("%.2f", temp_press[0]) + "hPa");
//            System.out.println("压力数据: " + String.format("%.2f",temp_press[0]) + "hPa");
            updateSensorsData ();
        }

        @Override
        public void onAccuracyChanged(Sensor sensor, int accuracy) {
        }
    }

    //方向传感器
    private class OSensorListener implements SensorEventListener {
        @Override
        public void onSensorChanged(SensorEvent event) {
            temp_o[0] = event.values[0];
            temp_o[1] = event.values[1];
            temp_o[2] = event.values[2];
            position.orientationData.setText ("方向数据: " + String.format ("%.2f", temp_o[0]) + ", "
                    + String.format ("%.2f", temp_o[1]) + ", " + String.format ("%.2f", temp_o[2]));
            System.out.println ("方向数据: " + String.format ("%.2f", temp_o[0]) + " " + String.format ("%.2f", temp_o[1]) + " " + String.format ("%.2f", temp_o[2]));

            updateSensorsData ();
        }

        @Override
        public void onAccuracyChanged(Sensor sensor, int accuracy) {

        }
    }

    //加速度传感器
    private class ASensorListener implements SensorEventListener {
        @Override
        public void onSensorChanged(SensorEvent event) {
            temp_a[0] = event.values[0];
            temp_a[1] = event.values[1];
            temp_a[2] = event.values[2];
            position.accelerometerData.setText ("加速数据: " + String.format ("%.2f", temp_a[0]) + ", "
                    + String.format ("%.2f", temp_a[1]) + ", " + String.format ("%.2f", temp_a[2]));
            System.out.println ("加速数据: " + String.format ("%.2f", temp_a[0]) + " " + String.format ("%.2f", temp_a[1]) + " " + String.format ("%.2f", temp_a[2]));
            updateSensorsData ();
        }

        @Override
        public void onAccuracyChanged(Sensor sensor, int accuracy) {
        }
    }

    //步数总数传感器
    private class SCSensorListener implements SensorEventListener {
        public void onSensorChanged(SensorEvent event) {
            temp_stepC = (int) event.values[0];
            position.stepCounterData.setText ("总步数: " + temp_stepC);
            System.out.println ("总步数: " + temp_stepC);
            updateSensorsData ();
        }

        @Override
        public void onAccuracyChanged(Sensor sensor, int accuracy) {
        }
    }

    //步数单数传感器
//    private class SDSensorListener implements SensorEventListener{
//        public void onSensorChanged(SensorEvent event){
//            temp_stepD = (int) event.values[0];
//            MainActivity.stepDetectorData.setText("单步数: "+temp_stepD+", at "+simpleDateFormat.format(new Date()));
//            System.out.println("Current time: "+ simpleDateFormat.format(new Date()));
//
//            updateSensorsData();
//        }
//
//        @Override
//        public void onAccuracyChanged(Sensor sensor, int accuracy) {
//        }
//    }

    public void unregist() {
        sensorManager.unregisterListener (oSensorListener);
        sensorManager.unregisterListener (aSensorListener);
        sensorManager.unregisterListener (stepCounterListener);
        sensorManager.unregisterListener (stepDetectorListener);
        sensorManager.unregisterListener (pressSensorListener);
    }
}
