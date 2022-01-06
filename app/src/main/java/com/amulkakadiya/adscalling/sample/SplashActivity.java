package com.amulkakadiya.adscalling.sample;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.view.Window;



import com.amulkakadiya.AdsCalling.GlobSplashActivity;
import com.amulkakadiya.AdsCalling.PrefStorageManager;
import com.amulkakadiya.adscalling.BuildConfig;
import com.amulkakadiya.adscalling.R;


public class SplashActivity extends GlobSplashActivity {


    PrefStorageManager pref;


    @Override
    protected int setLayoutResourceId() {
        return R.layout.activity_splash;
    }

    @Override
    protected View setParentView() {
        return findViewById(R.id.plash_ll);
    }

    @Override
    protected String setParentUrl() {
        return BuildConfig.server_url;
    }

    @Override
    protected callingbacktointent setParentcallnext() {
        return new callingbacktointent() {
            @Override
            public void callgetdataback() {
                startActivity(new Intent(SplashActivity.this, AdStartActivity.class));
                finishAffinity();
            }
        };
    }

    @Override
    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        if (Build.VERSION.SDK_INT >= 24) {
            Window window = getWindow();
            window.addFlags(Integer.MIN_VALUE);
            window.setStatusBarColor(0);
            window.setNavigationBarColor(0);
        }
        pref = new PrefStorageManager(SplashActivity.this);

    }
}


