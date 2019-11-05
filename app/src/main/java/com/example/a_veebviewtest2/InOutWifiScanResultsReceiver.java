package com.example.a_veebviewtest2;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.wifi.ScanResult;
import android.net.wifi.WifiManager;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

public class InOutWifiScanResultsReceiver extends BroadcastReceiver {
    private static final String LOG_TAG = "ScanResults";

    @Override
    public void onReceive(Context context, Intent intent) {
        //super.onReceive(context, intent); // Nunca entra aquí con Android 8 y targetSdkVersion 26+
        List<ScanResult> results = getWifiResults(context);
        Log.d(LOG_TAG, "Received results (" +  results.size() + " AP's)");
    }

    private static List<ScanResult> getWifiResults(Context context) {
        WifiManager wm = (WifiManager) context.getSystemService(Context.WIFI_SERVICE);
        try {
            return wm.getScanResults();
        } catch (SecurityException e) {
            return new ArrayList<> ();
        }
    }
}