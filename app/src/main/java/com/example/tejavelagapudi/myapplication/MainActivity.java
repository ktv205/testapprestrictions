package com.example.tejavelagapudi.myapplication;

import android.annotation.TargetApi;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.RestrictionEntry;
import android.content.RestrictionsManager;
import android.os.Build;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

import java.util.List;
import java.util.Set;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = MainActivity.class.getSimpleName();
    boolean appCanUseCellular;
    RestrictionsManager myRestrictionsMgr;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    /*
          Getting the app restrictions and checking their values every time this activity resumes
          so that we can apply those restrictions to the app. Also we are registering a dynamic listener
          to listen to app restrictions and  unregistered it in onPause of the activity.
     */

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected void onResume() {
        super.onResume();
        Log.d(TAG, "onResume");
        myRestrictionsMgr =
                (RestrictionsManager) this
                        .getSystemService(Context.RESTRICTIONS_SERVICE);
        Bundle appRestrictions = myRestrictionsMgr.getApplicationRestrictions();
        Set<String> restrictionList = appRestrictions.keySet();
        Log.d(TAG, "set size->" + restrictionList.size());
        for (String restriction : restrictionList) {
            Log.d(TAG, "inside restriction key->" + restriction);
        }

        List<RestrictionEntry> entries = myRestrictionsMgr.getManifestRestrictions(
                this.getApplicationContext().getPackageName());


        Log.d(TAG, "size entries->" + entries.size());
        for (RestrictionEntry entry : entries) {
            Log.d(TAG, "entry key->" + entry.getKey());
        }
        Log.d(TAG, "appRestrictions boolean->"
                + appRestrictions.getBoolean("downloadOnCellular"));
        if (appRestrictions.containsKey("downloadOnCellular")) {
            appCanUseCellular = appRestrictions.getBoolean("downloadOnCellular");
            Log.d(TAG, "appRestrictionsContainsKey");
        } else {
            appCanUseCellular = true;
            Log.d(TAG, "appRestrictionsDonotContainKey");
        }

        if (!appCanUseCellular) {
            Log.d(TAG, "appCannotUseCellularData");
        } else {
            Log.d(TAG, "appCanUseCellularData");
        }
        registerReceiver(restrictionsReceiver, restrictionsFilter);
    }

    IntentFilter restrictionsFilter =
            new IntentFilter(Intent.ACTION_APPLICATION_RESTRICTIONS_CHANGED);

    BroadcastReceiver restrictionsReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {

            // Get the current restrictions bundle
            Bundle appRestrictions =

                    myRestrictionsMgr.getApplicationRestrictions();
            Log.d(TAG, "dymanic restrictions");

            // Check current restrictions settings, change your app's UI and
            // functionality as necessary.

        }

    };


    @Override
    protected void onPause() {
        super.onPause();
        unregisterReceiver(restrictionsReceiver);
    }
}
