package com.amulkakadiya.AdsCalling;

import android.annotation.SuppressLint;
import android.app.Application;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Handler;

import static com.amulkakadiya.AdsCalling.CommonAdsUtility.admob_openAds_ID;
import static com.amulkakadiya.AdsCalling.CommonAdsUtility.admobinitilizepreload;
import static com.amulkakadiya.AdsCalling.CommonAdsUtility.list_admobinstrial;
import static com.amulkakadiya.AdsCalling.CommonAdsUtility.show_ads_current;


public class SpinMastApp extends Application {

    AppOpenManager appOpenManager;
    Handler handler;
    Handler handlepopup;
    private static SpinMastApp mInstance;

    public static synchronized SpinMastApp getInstance() {
        SpinMastApp oSpinMastApp;
        synchronized (SpinMastApp.class) {
            synchronized (SpinMastApp.class) {
                oSpinMastApp = mInstance;
            }
            return oSpinMastApp;
        }
    }


    @Override
    public void onCreate() {
        super.onCreate();

        try {
            mInstance = this;
        } catch (Exception e) {
            e.printStackTrace();
        }
        handler = new Handler();
        handlepopup = new Handler();
        startinitfullscreenpreload();
        startinitopenads();

    }

    public void startinitopenads() {
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {

                if (admob_openAds_ID != null && !admob_openAds_ID.equals("")) {
                    if (!show_ads_current) {
                        return;
                    }
                    appOpenManager = new AppOpenManager(SpinMastApp.this);
                    handler.removeCallbacks(this);
                    return;
                }
                startinitopenads();
            }
        }, 1000);
    }

    public void startinitfullscreenpreload() {
        handlepopup.postDelayed(new Runnable() {
            @Override
            public void run() {
                if (list_admobinstrial != null && list_admobinstrial.size() > 0) {
                    if (!show_ads_current) {
                        return;
                    }
                    admobinitilizepreload(SpinMastApp.this);
                    handlepopup.removeCallbacks(this);
                    return;
                }
                startinitfullscreenpreload();
            }
        }, 1000);
    }



    public static boolean hasNetwork ()
    {
        return mInstance.checkIfHasNetwork();
    }

    public boolean checkIfHasNetwork()
    {
        ConnectivityManager cm = (ConnectivityManager) getSystemService( Context.CONNECTIVITY_SERVICE);
        @SuppressLint("MissingPermission")
        NetworkInfo networkInfo = cm.getActiveNetworkInfo();
        return networkInfo != null && networkInfo.isConnected();
    }

}
